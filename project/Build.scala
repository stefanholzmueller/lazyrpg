import sbt._
import Keys._
import play.Project._
import cloudbees.Plugin._

object ApplicationBuild extends Build {

	val appName = "lazyrpg"
	val appVersion = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		// Add your project dependencies here,
		jdbc,
		anorm,
		"com.typesafe.akka" % "akka-testkit_2.10" % "2.1.1" % "test",
		"org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test")

	val main = play.Project(appName, appVersion, appDependencies)
		.settings(scalacOptions ++= Seq("-encoding", "UTF-8"))
		.settings(Keys.fork in (Test) := false)
		.settings(cloudBeesSettings: _*)

}
