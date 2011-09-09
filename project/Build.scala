import sbt._ 
import Keys._ 
import com.github.siasia.WebPlugin
import WebPlugin._

object BuildSettings { 
  val buildOrganization = "com.katlex" 
  val buildScalaVersion = "2.9.0" 
  val buildVersion      = "0.1.0" 
  val buildSettings = Defaults.defaultSettings ++ Seq(
	organization := buildOrganization,
        scalaVersion := buildScalaVersion,
        version      := buildVersion
  ) 
} 

object Dependencies { 

  //"org.eclipse.jetty" % "jetty-webapp" % "7.3.0.v20110203" % "jetty", // For Jetty 7
  val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty,test" // For Jetty 6, add scope test to make jetty avl. for tests
  val specs = "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test" // For specs.org tests
  val junit = "junit" % "junit" % "4.8" % "test->default" // For JUnit 4 testing
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  val h2db = "com.h2database" % "h2" % "1.2.138" // In-process database, useful for development systems
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default" // Logging
  val httpcomps = "org.apache.httpcomponents" % "httpclient" % "4.1.2" % "compile->default"

  val liftVersion = "2.4-M3" // Put the current/latest lift version here

  val lift_webkit = "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default"
  val lift_mapper = "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default"
  val lift_wizard = "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default"

  val liftDeps = Seq(jetty, servletApi, h2db, logback, lift_webkit, lift_mapper, lift_wizard)
  val liftTest = Seq(junit, specs)
  val allDeps = liftDeps ++ liftTest :+ httpcomps
} 

object MyUtilsBuild extends Build { 
  import Dependencies._ 
  import BuildSettings._ 

  lazy val core = Project("core", file ("core"), 
		settings = buildSettings ++ WebPlugin.webSettings ++ Seq(
			libraryDependencies := allDeps,
			jettyScanDirs := Nil
		)
  )
}
