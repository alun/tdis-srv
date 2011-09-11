package com.katlex.dsm.snippet

import com.katlex.dsm.abbrevs.TDISSiteAbbrevs
import xml.NodeSeq
import net.liftweb._
import http.js.jquery.JqJsCmds.JqSetHtml
import http.{SessionVar, SHtml}
import util.Helpers._

class Abbrevs {
  val id = "abbrebsContainer"
  object filterString extends SessionVar[String]("")

  def table = ".abbrevs [id]" #> id & ".abbrevs *" #> {
    ".abbrev" #> TDISSiteAbbrevs.abbrs.filter { kv =>
        kv._1.toLowerCase.indexOf(filterString.get) != -1 ||
        kv._2.toLowerCase.indexOf(filterString.get) != -1
      } .map { kv =>
        "* *" #> kv._1 &
        "* [abbr]" #> kv._2
      } .toSeq
  }

  def test(in : NodeSeq) = table(<span class="lift:embed?what=_abbrevs"/>)

  def search = ".input" #> SHtml.ajaxText(filterString.get, { input =>
    filterString.set(input.toLowerCase)
    JqSetHtml(id, <span class="lift:embed?what=_abbrevs"/>)
  }, "id" -> "quickSearch")
}