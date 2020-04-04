package com.pakius.parser

trait SchemaParser[I, O] {
  def parse(input: I):O
}

import java.lang.reflect.{Field, ParameterizedType}
import java.time.LocalDate

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.bigquery.model.TableFieldSchema

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer




object SchemaParserInstance {

  implicit val pojosTobqSchemaParser = new SchemaParser[Class[_], List[TableFieldSchema]]() {

    override def parse(input: Class[_]): List[TableFieldSchema] = {
      val buffer = new ListBuffer[Field]
      val clazz = input.getGenericSuperclass.asInstanceOf[Class[_]]
      if(!clazz.isAssignableFrom(classOf[java.lang.Object])){
        buffer.appendAll(clazz.getDeclaredFields)
      }
      buffer.appendAll(input.getDeclaredFields)

      val tableFieldSchemas = buffer

        .filterNot(_.getName == "serialVersionUID")
        .toList
        .map(i => loop(i, new TableFieldSchema().setName(i.getName).setMode("NULLABLE")))

      tableFieldSchemas
    }


    private def loop(field: Field,  tf: TableFieldSchema): TableFieldSchema = {
      field.getType match {

        case s if s.isAssignableFrom(classOf[java.lang.Integer]) || s.getName == "short" || s.getName == "int" || s.getName == "long" => tf.setType("INTEGER")
        case s if s.isAssignableFrom(classOf[String]) => tf.setType("STRING")
        case s if s.isAssignableFrom(classOf[LocalDate]) => tf.setType("DATE")
        case s if s.isAssignableFrom(classOf[Array[Byte]]) => tf.setType("BYTES")
        case s if s.isAssignableFrom(classOf[java.math.BigDecimal]) => tf.setType("NUMERIC")
        case s if s.isAssignableFrom(classOf[java.lang.Float])|| s.isAssignableFrom(classOf[java.lang.Double]) || s.getName == "float" || s.getName == "double" => tf.setType("FLOAT")
        case s if s.isAssignableFrom(classOf[java.lang.Boolean]) || s.getName == "boolean"  => tf.setType("BOOLEAN")

        case s if s.isInstanceOf[Any] =>
          val lb = new ListBuffer[Field]()
          if(s.getName == "java.util.List"){
            tf.setMode("REPEATED")
            val listType = field.getGenericType.asInstanceOf[ParameterizedType]
            val t = listType.getActualTypeArguments.head.asInstanceOf[Class[_]]

            t match {
              case c if c.isAssignableFrom(classOf[java.lang.Long]) => tf.setType("INTEGER")
              case s if s.isAssignableFrom(classOf[LocalDate]) => tf.setType("DATE")
              case c if c.isAssignableFrom(classOf[java.lang.String]) => tf.setType("STRING")
              case c if c.isAssignableFrom(classOf[java.lang.Integer]) => tf.setType("INTEGER")
              case c if c.isAssignableFrom(classOf[java.math.BigDecimal]) => tf.setType("NUMERIC")
              case c if c.isAssignableFrom(classOf[java.lang.Double]) => tf.setType("FLOAT")
              case _ =>  {
                tf.setType("RECORD")
                val clazz = t.getGenericSuperclass.asInstanceOf[Class[_]]
                if(!clazz.isAssignableFrom(classOf[java.lang.Object])){
                  lb.appendAll(clazz.getDeclaredFields)
                }

                lb.appendAll(t.getDeclaredFields)
              }
            }

          }else{
            tf.setType("RECORD")
            val clazz = field.getType.getGenericSuperclass.asInstanceOf[Class[_]]
            if(!clazz.isAssignableFrom(classOf[java.lang.Object])){
              lb.appendAll(clazz.getDeclaredFields)
            }
            lb.appendAll(field.getType.getDeclaredFields)

          }
          val tfslst = lb.
            filterNot(_.getName == "serialVersionUID").
            toList.
            map(i => loop(i,  new TableFieldSchema().setName(i.getName).setMode("NULLABLE")))

          if (tfslst.nonEmpty) {
            tf.setFields(tfslst.asJava)
          }
      }
      tf
    }
  }
}


object SchemaParserImpl {

  def pojosTobqSchemaPrettyJsonString[T](clazz: Class[T])(implicit schemaParser: SchemaParser[Class[_], List[TableFieldSchema]]): String = {

    val tableFieldSchemas = schemaParser.parse(clazz).toBuffer


    toPrettyJson(tableFieldSchemas)
  }



   val toPrettyJson: mutable.Buffer[TableFieldSchema] => String = (lst: mutable.Buffer[TableFieldSchema]) => {
    val jsonFactory = new JacksonFactory()
    val lstString = lst.map(i => {
      i.setFactory(jsonFactory)
      i.toPrettyString
    })
    lstString.mkString("[", ",", "]")
  }
}
