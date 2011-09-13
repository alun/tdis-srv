package bootstrap.liftweb

import net.liftweb._
import common._
import http._
import sitemap._

class Boot {
  def boot {
    LiftRules.addToPackages("com.katlex.dsm")

    Logger.setup = for {
      logConfig <- Box !! classOf[Boot].getClassLoader.getResource("conf/logconfig.xml")
    } yield {
      Logback.withFile(logConfig) _
    }

    LiftRules.setSiteMap(SiteMap(
      Menu.i("Список аббревиатур") / "index", // the simple way to declare a menu
      Menu.i("Текстовый фильтр") / "filter"
    ))

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))
  }
}
