package com.pakius

import org.scalatest.{FlatSpec, Matchers}
import com.pakius.SchemaParserInstance._
import com.pakius.SchemaParserImpl._

class SchemaParserSpec extends FlatSpec with Matchers{

  it should "return bq schema given a POJO" in {
     val test1  = pojosTobqSchemaParser.parse(classOf[BQSchemaClass1])
     test1.head.getName should be ("anyfield1")
     test1.head.getMode should be ("NULLABLE")
     test1.head.getType should be ("STRING")

    val test2 = pojosTobqSchemaParser.parse(classOf[BQSchemaClass2])
    test2.head.getName should be ("pc")
    test2.head.getType should be ("RECORD")
    test2.head.getMode should be ("NULLABLE")

    val test3 = pojosTobqSchemaParser.parse(classOf[BQSchemaClass4])
    test3.head.getName should be ("lst")
    test3.head.getMode should be ("REPEATED")
    test3.head.getType should be ("STRING")

    test3(1).getName should be ("lst2")
    test3(1).getMode should be ("REPEATED")
    test3(1).getType should be ("RECORD")
    test3(1).getFields.size() == 1
    test3(1).getFields.get(0).getName should be ("anyfield1")
    test3(1).getFields.get(0).getMode should be ("NULLABLE")
    test3(1).getFields.get(0).getType should be ("STRING")

    val test4 = pojosTobqSchemaParser.parse(classOf[BQSchemaClass3])
    test4.head.getName should be ("bytes")
    test4.head.getMode should be ("NULLABLE")
    test4.head.getType should be ("BYTES")

    val test5 = pojosTobqSchemaParser.parse(classOf[BQSchemaClass6])
    test5.head.getName should be ("field1")
    test5.head.getMode should be ("NULLABLE")
    test5.head.getType should be ("STRING")
    test5(1).getName should be ("field2")
    test5(1).getMode should be ("NULLABLE")
    test5(1).getType should be ("STRING")

  }

  it should "print out tableFieldSchema list as pretty json" in {
    val test = pojosTobqSchemaParser.parse(classOf[BQSchemaClass1])
    val json = toPrettyJson(test.toBuffer)
    val expected = """[{
                     |  "mode" : "NULLABLE",
                     |  "name" : "anyfield1",
                     |  "type" : "STRING"
                     |}]""".stripMargin


    json should be (expected)

  }


}



