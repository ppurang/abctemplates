package org.purang.templates.abc.unsafe

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

import scala.util.matching.Regex
import cats.syntax.all._
//import cats.syntax.traverse._
import cats.Monoid.combineAll
import cats.implicits._

class Template private[unsafe] (private val contents: String) {

  import Template._

  def merge(m: Map[String, String]): String = {
    val document: Document =
      Jsoup.parse(contents, "", Parser.xmlParser()).outputSettings(settings)
    m.foreach { case (k, v) =>
      if (k.startsWith(ATTRIBUTE + ".")) {
        val a: String = k.replaceFirst(ATTRIBUTE + ".", "")
        val NS_ATTRIBUTE(_, ns, pattr) =
          a // div[abc:href] => div && abc:href && href
        document
          .select(a)
          .attr(pattr, v)
          .removeAttr(
            s"$ns:$pattr"
          ) //select("div[abc:href]").attr("href", "somev").remove("abc:href")
      } else {
        val ELAT(_, attr) = k
        document.select(k).html(v).removeAttr(attr)
      }
    }
    document.html()
  }

  def embeddedTemplate(cssQuery: String): Option[String] = {
    Either.catchNonFatal {
      //todo too many documents initialized for #embeddedTemplates
      val document: Document =
        Jsoup.parse(contents, "", Parser.xmlParser()).outputSettings(settings)
      document.select(cssQuery).first().outerHtml()
    }.toOption
  }

  def embeddedTemplates(cssQueries: List[String]): Option[String] = (for {
    cssQuery <- cssQueries
  } yield embeddedTemplate(cssQuery)).sequence.map(l => combineAll(l))

}

object Template {
  val ATTRIBUTE: String = "a"
  private val ELAT: Regex = """(.*)\[(.*)]""".r
  private val NS_ATTRIBUTE: Regex = """(.*)\[(.*):(.*)]""".r

  private val settings: Document.OutputSettings =
    new Document.OutputSettings().prettyPrint(false)

  def apply(s: String): Template = new Template(s)

  def valid(result: String, namespace: String): Boolean =
    !result.contains(namespace)
}
