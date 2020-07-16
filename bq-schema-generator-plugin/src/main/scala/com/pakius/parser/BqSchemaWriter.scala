package com.pakius.parser

import java.lang.reflect.{Field, ParameterizedType}
import java.time.LocalDate

import com.google.api.services.bigquery.model.TableFieldSchema

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

trait BqWriter[A] {
  def writer(value: A): Bq
}

object BqWriterInstance {

  implicit val ClassWriter: BqWriter[Class[_]] = {
    input: Class[_] => {
      val buffer = new ListBuffer[Field]()
      buffer.appendAll(getFieldsSuperClass(input))
      buffer.appendAll(input.getDeclaredFields.toList)
      BqSchema(lstFields(buffer.toList))
    }
  }

  def getFieldsSuperClass(clazz : Class[_]) : List[Field] = {
    val c = clazz.getGenericSuperclass.asInstanceOf[Class[_]]
    val lst:List[Field]  = if(c != null && !c.isAssignableFrom(classOf[java.lang.Object])){
      c.getDeclaredFields.toList
    }else List()

    lst
  }

  def lstFields(lst : List[Field]): List[TableFieldSchema] = {
     lst.filterNot(_.getName == "serialVersionUID").map{
      i => toTableFieldSchema(i.getType, i,  new TableFieldSchema().setName(i.getName).setMode("NULLABLE"))
    }

  }

  @tailrec
  def toTableFieldSchema(clazz: Class[_], field: Field, tf: TableFieldSchema): TableFieldSchema = {
    clazz match {
      case s if s.isAssignableFrom(classOf[java.lang.Integer]) || s.getName == "short" || s.getName == "int" || s.getName == "long" => tf.setType("INTEGER")
      case s if s.isAssignableFrom(classOf[String]) => tf.setType("STRING")
      case s if s.isAssignableFrom(classOf[LocalDate]) => tf.setType("DATE")
      case s if s.isAssignableFrom(classOf[Array[Byte]]) => tf.setType("BYTES")
      case s if s.isAssignableFrom(classOf[java.math.BigDecimal]) => tf.setType("NUMERIC")
      case s if s.isAssignableFrom(classOf[java.lang.Float]) || s.isAssignableFrom(classOf[java.lang.Double]) || s.getName == "float" || s.getName == "double" => tf.setType("FLOAT")
      case s if s.isAssignableFrom(classOf[java.lang.Boolean]) || s.getName == "boolean" => tf.setType("BOOLEAN")
      case s if s.isAssignableFrom(classOf[java.util.List[Any]]) => {
        tf.setMode("REPEATED")
        val listType = field.getGenericType.asInstanceOf[ParameterizedType]
        val t = listType.getActualTypeArguments.head.asInstanceOf[Class[_]]
        toTableFieldSchema(t, field, tf)
      }
      case _ => tf.setType("RECORD")
        val buffer = new ListBuffer[Field]()
        buffer.appendAll(getFieldsSuperClass(field.getType))
        if(clazz.getDeclaredFields.toList.nonEmpty){
          buffer.appendAll(clazz.getDeclaredFields.toList)
          val lst = lstFields(buffer.toList)
          tf.setFields(lst.asJava)
          return tf
        }
        buffer.appendAll(field.getType.getDeclaredFields.toList)
        val lst = lstFields(buffer.toList)
        tf.setFields(lst.asJava)
    }
  }
}











