package com.katlex.dsm.snippet

import net.liftweb._
import common.Logger
import http.js.jquery.JqJsCmds.JqSetHtml
import http.{SessionVar, SHtml}
import util.Helpers._
import SHtml.ElemAttr._
import com.katlex.dsm.abbrevs.TDISSiteAbbrevs
import xml.{XML, Text}

object FilterTool extends Logger {

  lazy val resContainerId = "filterResultsContainer"

  object textInput extends SessionVar[String]("")
  object translateResults extends SessionVar[String]("Результат отобразится здесь")

  lazy val abbrRegexpToHtml = {
    def mkAbbr(abbr : String, title : String) = "<abbr title=\"%s\">%s</abbr>" format (title, abbr)
    def safeRegexp(s : String) = List("-", "*", "+").foldLeft(s) { (s, specChar) =>
        s.replaceAll("\\" + specChar, "\\\\" + specChar)
      }
    TDISSiteAbbrevs.abbrs.map { case (abbr, title) =>
      ("""\b%s\b""" format safeRegexp(abbr)).r -> mkAbbr(abbr, title)
    }
  }

  def replaceAbbrs(input : String) = abbrRegexpToHtml.foldLeft(input) { case (res, (regexp, html)) =>
      regexp.replaceAllIn(res, html)
    }
  
  def render = ".text" #> SHtml.ajaxTextarea(textInput.get, { input =>
                        val res = replaceAbbrs(input)
                        textInput.set(input)
                        translateResults.set(res)
                        JqSetHtml(resContainerId, XML.loadString("""<div>%s</div>""" format(res)))
                      }, "rows" -> "50", "cols" -> "600") &
               ".results" #> {
                 "* *" #> translateResults.get &
                 "* [id]" #> resContainerId
               }

}