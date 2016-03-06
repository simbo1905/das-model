package com.github.simbo1905.dasmodel

class EmbeddedWebapp(val port: Int = 8080, val webappPath: String = "../webapp", val contextPath: String = "/") {

  import org.eclipse.jetty.server.{Server, ServerConnector}
  import org.eclipse.jetty.webapp.WebAppContext
 
  val server = new Server()
  val connector = new ServerConnector(server)
  connector.setPort(port)
  server.addConnector(connector)

  val context = new WebAppContext()
  context.setResourceBase(webappPath)
  context.setDescriptor(webappPath+"/WEB-INF/web.xml")
  context.setContextPath(contextPath)
  context.setParentLoaderPriority(true)
  server.setHandler(context)

  def start() = server.start
  def stop() = server.stop

}

object Runner {
  def main(args: Array[String]) {

    if( args.length != 1) {
      println("Please pass the path to the webapp folder.")
    } else {
      val ej = new EmbeddedWebapp(8080, args(0))
      ej.start()
      System.out.println("Press any key to exit...")
      System.in.read()
      ej.stop()
    }
  }
}