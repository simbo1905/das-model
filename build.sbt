scalaVersion := "2.11.7"

enablePlugins(JettyPlugin)

enablePlugins(HerokuDeploy)

herokuAppName := "stormy-tundra-98442"

containerLibs in Jetty := Seq("org.eclipse.jetty" % "jetty-runner" % "9.3.7.v20160115" intransitive())

containerMain in Jetty := "org.eclipse.jetty.runner.Runner"

//addCommandAlias("push", "; stage ; herokuDeploy")
//
//lazy val compileAll = taskKey[Unit]("Compiles sources in all configurations.")
//
//compileAll := {
//  val a = (compile in Test).value
//  val b = (compile in IntegrationTest).value
//  ()
//}