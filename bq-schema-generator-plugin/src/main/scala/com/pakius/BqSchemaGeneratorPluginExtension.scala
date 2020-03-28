package com.pakius

class BqSchemaGeneratorPluginExtension{

  private var classNameList:java.util.List[String] = _
  private var outputName:String = _
  private var jarLocation:String = _

  def getClassNameList():java.util.List[String] = classNameList
  def setClassNameList(classNameList:java.util.List[String]) = this.classNameList = classNameList

  def getOutput():String = outputName
  def setOutput(output:String) = this.outputName = output

  def getJarLocation():String = jarLocation
  def setJarLocation(jarLocation:String) = this.jarLocation = jarLocation

}


