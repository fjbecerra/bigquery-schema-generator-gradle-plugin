package com.pakius.parser

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.bigquery.model.TableFieldSchema

sealed trait Bq
case class BqSchema(get: List[TableFieldSchema]) extends Bq {
  override def toString: String = {
    val jsonFactory = new JacksonFactory()
    val lstString = this.get.map(i => {
      i.setFactory(jsonFactory)
      i.toPrettyString
    })
    lstString.mkString("[", ",", "]")
  }
}


object Bq {
  implicit class BqOps[T](data: Class[_]) {
    def toBqSchema(implicit w: BqWriter[Class[_]]): Bq =
      w.writer(data)
  }
}