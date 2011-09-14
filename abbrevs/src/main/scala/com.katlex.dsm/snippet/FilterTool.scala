package com.katlex.dsm.snippet

import net.liftweb._
import common.Logger
import http.js.jquery.JqJsCmds.JqSetHtml
import http.{SessionVar, SHtml}
import util.Helpers._
import SHtml.ElemAttr._
import com.katlex.dsm.abbrevs.TDISSiteAbbrevs
import scala.util.matching.Regex
import xml.{Elem, XML, Text}

object FilterTool extends Logger {

  class Replacer(target : String) {
    def ~~(conf : Array[(Regex, String)]) = conf.foldLeft(target) { case (res, (regex, repl)) =>
      regex.replaceAllIn(res, repl)
    }
  }

  lazy val resContainerId = "filterResultsContainer"

  private object textInput extends SessionVar[String]("")
  private object translateResults extends SessionVar[Elem](decorateResults("Результат отобразится здесь"))

  private lazy val fixInputReplacements = Array("""\u2018|\u2019""".r -> "'")
  def abbrsReplacements = {
    def mkAbbr(abbr : String, title : String) = "<abbr fullText=\"%s\">%s</abbr>" format (title, abbr)
    def safeRegexp(s : String) = List("-", "*", "+").foldLeft(s) { (s, specChar) =>
        s.replaceAll("\\" + specChar, "\\\\" + specChar)
      }
    TDISSiteAbbrevs.abbrs.sortBy(- _._1.size).map { case (abbr, title) =>
      val fwdLookup = if (abbr.endsWith("-")) "" else """(?=\.|,|\s|$|\))"""
      ("""(?<=^|\s|\()%s%s""".format(safeRegexp(abbr), fwdLookup)).r -> mkAbbr(abbr, title)
    }
  }

  def render = ".text" #> SHtml.ajaxTextarea(textInput.get, { input =>
                        val res = decorateResults(input ~~ fixInputReplacements ~~ abbrsReplacements)
                        textInput.set(input)
                        translateResults.set(res)
                        JqSetHtml(resContainerId, res)
                      }, "rows" -> "50", "cols" -> "600") &
               ".results" #> {
                 "* *" #> translateResults.get &
                 "* [id]" #> resContainerId
               }

  implicit def string2replacer(target : String) : Replacer = new Replacer(target)

  private def decorateResults(res : String) = XML.loadString("""<div>%s</div>""" format res)
}