package com.pakius

import java.io.File
import java.util

import org.gradle.testfixtures.ProjectBuilder
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source


class BqTaskSpec extends FlatSpec with Matchers{

  it should "be able to add task to project" in {
    val project = ProjectBuilder.builder().build()
    project.getPlugins.apply("com.pakius.bqSchemaGenerator")
    val extension = project.getExtensions.getByName("BqSchemaGeneratorPlugin").asInstanceOf[BqSchemaGeneratorPluginExtension]
    extension.setClassNameList(util.Arrays.asList("com.pojos.BqPojo"))
    extension.setOutput("src/test/resources/schemas")
    extension.setJarLocation(new File("src/test/resources/example-1.0.0-SNAPSHOT.jar").getAbsolutePath)
    val bqTask = project.getTasks.getByPath("bqSchemaGenerator").asInstanceOf[BqTask]
    bqTask.parse()
    val content = Source.fromFile("src/test/resources/schemas/BqPojo.json").mkString
    val expected = """[{
                     |  "mode" : "NULLABLE",
                     |  "name" : "id",
                     |  "type" : "STRING"
                     |},{
                     |  "mode" : "NULLABLE",
                     |  "name" : "name",
                     |  "type" : "STRING"
                     |}]""".stripMargin
    content should be (expected)
  }

}
