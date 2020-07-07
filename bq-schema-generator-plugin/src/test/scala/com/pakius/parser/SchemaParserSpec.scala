package com.pakius.parser

import org.scalatest.{FlatSpec, Matchers}
import com.pakius.parser.Bq._
import com.pakius.parser.BqWriterInstance._

class SchemaParserSpec extends FlatSpec with Matchers{

  it should "return bq schema given a POJO" in {


     val test1  = classOf[BQSchemaClass1].toBqSchema.asInstanceOf[BqSchema].get

     test1.head.getName should be ("anyfield1")
     test1.head.getMode should be ("NULLABLE")
     test1.head.getType should be ("STRING")

    val test2 = classOf[BQSchemaClass2].toBqSchema.asInstanceOf[BqSchema].get
    test2.head.getName should be ("pc")
    test2.head.getType should be ("RECORD")
    test2.head.getMode should be ("NULLABLE")

    val test3 = classOf[BQSchemaClass4].toBqSchema.asInstanceOf[BqSchema].get
    test3.head.getName should be ("lst")
    test3.head.getMode should be ("REPEATED")
    test3.head.getType should be ("STRING")

    test3(1).getName should be ("lst2")
    test3(1).getMode should be ("REPEATED")
    test3(1).getType should be ("RECORD")

    test3(1).getFields.size() should be (1)

    test3(1).getFields.get(0).getName should be ("anyfield1")
    test3(1).getFields.get(0).getMode should be ("NULLABLE")
    test3(1).getFields.get(0).getType should be ("STRING")

    val test4 = classOf[BQSchemaClass3].toBqSchema.asInstanceOf[BqSchema].get
    test4.head.getName should be ("bytes")
    test4.head.getMode should be ("NULLABLE")
    test4.head.getType should be ("BYTES")

    val test5 = classOf[BQSchemaClass6].toBqSchema.asInstanceOf[BqSchema].get
    test5.head.getName should be ("field1")
    test5.head.getMode should be ("NULLABLE")
    test5.head.getType should be ("STRING")
    test5(1).getName should be ("field2")
    test5(1).getMode should be ("NULLABLE")
    test5(1).getType should be ("STRING")

    val test6 = classOf[BQSchemaClass7].toBqSchema.asInstanceOf[BqSchema].get
    test6.head.getName should be ("field1")
    test6.head.getMode should be ("NULLABLE")
    test6.head.getType should be ("RECORD")
    test6.head.getFields.get(0).getName should be ("field1")
    test6.head.getFields.get(0).getMode should be ("NULLABLE")
    test6.head.getFields.get(0).getType should be ("STRING")
    test6.head.getFields.get(1).getName should be ("field2")
    test6.head.getFields.get(1).getMode should be ("NULLABLE")
    test6.head.getFields.get(1).getType should be ("STRING")


  }

  it should "print out tableFieldSchema list as pretty json" in {
    val test = classOf[BQSchemaClass1].toBqSchema
    val json = test.toString
    val expected = """[{
                     |  "mode" : "NULLABLE",
                     |  "name" : "anyfield1",
                     |  "type" : "STRING"
                     |}]""".stripMargin


    json should be (expected)

  }

}




