package com.pakius.plugin

import java.io.{File, FileNotFoundException, PrintWriter}
import java.net.{URL, URLClassLoader}

import com.pakius.parser.SchemaParserImpl._
import com.pakius.parser.SchemaParserInstance._
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._


class BqTask extends DefaultTask{

  val LOG: Logger = LoggerFactory.getLogger(this.getClass.getName)

  @TaskAction def parse(): Unit ={
    val extension = getProject.getExtensions.findByType(classOf[BqSchemaGeneratorPluginExtension])
    val clazzLst = Option(extension.getClassNameList()) match {
      case Some(c) => c
      case None =>  throw new Exception("package name empty.")
    }

    val output = Option(extension.getOutput()) match {
      case Some(o) => o
      case None => throw new Exception("bq schema output empty.")
    }

    val f = new File(output)
    if(!f.exists()){
      f.mkdir()
    }

    val jarURL = Option(extension.getJarLocation()) match {
      case None => {
        val version = if(getProject.getVersion.toString == "unspecified") "" else s"-${getProject.getVersion}"
        s"${getProject.getBuildDir.getAbsolutePath}/libs/${getProject.getName}$version.jar"
      }
      case Some(j) => j
    }

    if(new File(jarURL).exists()){
      val urls = Array(new URL(s"file://${jarURL}"))
      val loader = new URLClassLoader(urls)
      clazzLst.asScala.toList
        .foreach{i => {
          val cls = loader.loadClass(i)
          val content = pojosTobqSchemaPrettyJsonString(cls)
          new PrintWriter(s"${output}/${cls.getSimpleName}.json") { write(content); close() }}}
    }else{
      throw new FileNotFoundException("Jar file not found.")
    }
  }
}
