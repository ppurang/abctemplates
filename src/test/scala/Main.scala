import org.purang.net.abctemplates._

object Main {

  def main(args: Array[String]) {

    val h = """
              |<div id="content" abc:content>
              |  <p>
              |    some imaginary data for instant visualization in the browser
              |  </p>
              |</div>
            """.stripMargin

    val template = Template(h)

    val context = Map(
          "[abc:content]"  // identifier in the template
          ->
          "<h1>the real content!</h1>" // the new inner html contents
    )

    // merge template with a context
    val result = template.fragmentMerge(context)

    println(result)

    //result should be (note: <p> tags have been dropped)
    //  <div id="content">
    //    <h1>the real content!</h1>
    //  </div>

  }
}
