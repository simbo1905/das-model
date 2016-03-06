import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "zkoss"
  val buildVersion      = "0.7.5"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    name := "zkscala",
    organization := buildOrganization,
    version := buildVersion
  )
}

object Resolvers {
  val zkcerepo = "ZK CE" at "http://mavensync.zkoss.org/maven2"
  val zkevalreop = "ZK Evaluation Repository" at "http://mavensync.zkoss.org/eval"
}

object Dependencies {

  val zkversion = "7.0.5-Eval"
  val zkchartsversion= "2.0.0.1-Eval"
  val zkzssversion = "3.8.2-Eval"

  val zkmax = "org.zkoss.zk" % "zkmax" % zkversion
  val zkcharts = "org.zkoss.chart" % "zkcharts" % zkchartsversion
  val zkzss = "org.zkoss.zss" % "zssex" % zkzssversion

  val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"

  val zkDeps = Seq(
    zkmax,
    zkcharts,
    zkzss
  )

  val scalaTest = Seq(
    scalatest
  )
}

object ZKScalaBuild extends Build {
  import BuildSettings._
  import Resolvers._
  import Dependencies._

  lazy val zkscala = Project(
    "zkscala",
    file("."),
    settings = buildSettings ++ Seq(
      resolvers := Seq(zkcerepo, zkevalreop),
      libraryDependencies ++= zkDeps ++ scalaTest
    )
  )
}