package org.purang.net.abctemplates

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

class Template(private val contents: String) {

  import Template._
  
  def merge(m: Map[String, String]) : String = {
    val document = Jsoup.parse(contents, "", Parser.xmlParser()).outputSettings(settings)
    m.foreach {
      case (k, v) => if(k.startsWith(ATTRIBUTE + ".") ) {
        val a = k.replaceFirst(ATTRIBUTE + ".", "")
        val NS_ATTRIBUTE(el, ns, pattr) = a // div[abc:href] => div && abc:href && href
        document.select(a).attr(pattr, v).removeAttr(s"$ns:$pattr")  //select("div[abc:href]").attr("href", "somev").remove("abc:href")
      } else {
        val ELAT(el, attr) = k
        document.select(k).html(v).removeAttr(attr)
      }
    }
    document.html()
  }
}

object Template {
  val ELEMENT = "e" // is the default hence never really needed
  val ATTRIBUTE = "a"
  private val ELAT = """(.*)\[(.*)\]""".r
  private val NS_ATTRIBUTE = """(.*)\[(.*):(.*)\]""".r

  private val settings = new Document.OutputSettings().prettyPrint(false)

  def apply(s: String) = new Template(s)

  def valid(result: String, namespace : String)  : Boolean = !result.contains(namespace)
}
