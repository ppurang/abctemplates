import org.purang.net.abctemplates._

object Main {

  def main(args: Array[String]): Unit = {

    val h: String = """
              |<div id="content" abc:content>
              |  <p>
              |    some imaginary data for instant visualization in the browser
              |  </p>
              |</div>
            """.stripMargin

    val template: Template = Template(h)

    val context: Map[String, String] = Map(
          "[abc:content]"  // identifier in the template
          ->
          "<h1>the real content!</h1>" // the new inner html contents
    )

    // merge template with a context
    val result: String = template.merge(context)

    println(result)

    //result should be (note: <p> tags have been dropped)
    //  <div id="content">
    //    <h1>the real content!</h1>
    //  </div>

  }
}
