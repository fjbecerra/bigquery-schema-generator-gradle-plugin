package com.pakius

import org.gradle.api.Plugin
import org.gradle.api.Project

class BqSchemaGeneratorPlugin extends Plugin[Project]{

  override def apply(project: Project): Unit = {
     project.getExtensions.create("BqSchemaGeneratorPlugin", classOf[BqSchemaGeneratorPluginExtension])
      project.getTasks.create("bqSchemaGenerator", classOf[BqTask]);
  }

}
