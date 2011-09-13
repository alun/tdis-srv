package com.katlex.dsm.abbrevs

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.protocol.BasicHttpContext
import scala.io.Source
import net.liftweb.common.Logger

object TDISSiteAbbrevs extends Logger {
  val URI = "http://newasp.omskreg.ru/tdis/short1.htm"
  val client = new DefaultHttpClient

  lazy val abbrs = {
    val context = new BasicHttpContext
    val request = new HttpGet(URI)
    val response = client.execute(request, context).getEntity

    debug("TDIS abbrevs " + URI + " response loaded")

    Some {
      Source.fromInputStream(response.getContent, "CP1251").
        getLines.foldLeft("")(_ + _)
    } .map {
      // make our html more strict xml
      "(</?)(?:p|P)>".r.replaceAllIn(_, _.group(1) + "p>")
    } .map {
      "<p>.*?</p>".r.findAllIn(_).foldLeft("")(_ + "\n" + _)
    } .map {
      _.split("\n").filterNot(_.trim == "").mkString("\n")
    } .map {
      "<p>(.*?)</p>".r.replaceAllIn(_, _.group(1))
    } .map {
      "<.*?>".r.replaceAllIn(_, "")
    } .map {
      _.split("\n").map { line =>
        val kv = line.split(" -").map(_.trim)
        kv(0) -> kv(1)
      } .sortBy(kv => """[А-Я]""".r.findFirstIn(kv._1).getOrElse("Я"))
    } .get
  }

  lazy val abbrsMap = abbrs.toMap

  def whatIs(abbr : String) = abbrsMap.get(abbr).getOrElse {
    Some {
      abbrsMap.filter(_._1.indexOf(abbr) != -1).map(kv => kv._1 + " => " + kv._2).mkString("\n")
    } .flatMap { v =>
      if (v == "") None else Some(v)
    } .getOrElse("Не знаю")
  }

  def abbrsString = {
    abbrs.map(kv => kv._1 + " => " + kv._2).mkString("\n")
  }
}