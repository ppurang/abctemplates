package org.purang.net.abctemplates

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.util.matching.Regex

class Template(private val contents: String) {

  import Template._


  def fragmentMerge(m: Map[String, String]) : String = {
    val result = merge(m)
    //todo following's ugly as hell
    stripStuff(BODY, Vector("<body>", "</body>"), contents, stripStuff(HEAD, Vector("<head>", "</head>"), contents, stripStuff(HTML, Vector("<html>", "</html>"), contents, result))).trim()
  }

  def merge(m: Map[String, String]) : String = {
    val document = Jsoup.parse(contents)
    for((k, v) <- m) {
      if(k.startsWith(ATTRIBUTE + ".") ) {
        val a = k.replaceFirst(ATTRIBUTE + ".", "")
        val NS_ATTRIBUTE(el, fattr, pattr) = a // div[abc:href] => div && abc:href && href
        document.select(a).attr(pattr, v).removeAttr(fattr) //select("div[abc:href]").attr("href", "somev").remove("abc:href")
      } else {
        val ELAT(el, attr) = k
        document.select(k).html(v).removeAttr(attr)
      }
    }
    document.html()
  }
}

object Template {
  val ELEMENT = "e" // is the defaul hence never really needed
  val ATTRIBUTE = "a"
  private val ELAT = """(.*)\[(.*)\]""".r
  private val NS_ATTRIBUTE = """(.*)\[(abc:(.*))\]""".r
  private val HTML = """  (?s).*<html>.*""".r
  private val HEAD = """(?s).*<head>""".r
  private val BODY = """(?s).*<body>""".r

  @inline private def stripStuff(regex: Regex, vector: Vector[String], contents: String, result: String) = if(regex.findFirstIn(contents).isEmpty) {
      result.replaceAll(vector(0),"").replaceAll(vector(1), "")
    } else {
      result
    }

  def apply(s: String) = new Template(s)

  def valid(result: String, namespace : String = "abc:")  : Boolean = !result.contains(namespace)
}
