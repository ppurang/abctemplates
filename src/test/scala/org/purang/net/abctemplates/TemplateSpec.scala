package org.purang.net.abctemplates

import org.scalatest._


// Note: we test fragment merges only because for now that validates the underlying merge
class TemplateSpec extends FlatSpec with Matchers {

  "ABC Templates" should "allow fragment merge with a single key" in {
    val h =
      """|<div id="content" doh-content>
         |  <p>
         |    some imaginary data for instant visualization in the browser
         |  </p>
         |</div>
      """.stripMargin

    val template = Template(h)

    val content = "<p>the real content!</p>"
    val attribute = "doh-content"
    val result = template.fragmentMerge(Map("div[doh-content]" -> content))

    result should include(content)
    result should not include(attribute)
  }

  it should "allow fragment merge with a single key and line breaks" in {
    val h =
      """|<div id="content" doh-content>
         |  <p>
         |    some imaginary data for instant visualization in the browser
         |  </p>
         |</div>
      """.stripMargin

    val template = Template(h)

    val content = "<p>the real \n content!</p>"
    val attribute = "doh-content"
    val result = template.fragmentMerge(Map("div[doh-content]" -> content))

    result should include(content)
    result should not include(attribute)
  }

  it should "allow fragment merges with multiple keys" in {

    val h =
      """
        |<div class="entry">
        |  <h1 abc:title>some title</h1>
        |  <div class="body" abc:body>
        |    some body
        |  </div>
        |</div>
        |
      """.stripMargin

    val template = Template(h)

    val title = "altered title"
    val body  = "altered body"
    val result = template.fragmentMerge(Map("[abc:title]" -> title, "[abc:body]" -> body))

    result should include(title)
    result should include(body)
    result should not include("abc:")
  }

  it should "allow fragment merges with attribute replacement" in {

    val h =
      """
        |  <link rel="stylesheet" type="text/css" media="print" href="stylesheets/print.css" abc:href/>
        |  <h1 abc:title>title </h1>
      """.stripMargin

    val template = Template(h)

    val href = "stylesheets/print-sgshgshg.css"
    val title  = "altered title"
    val result = template.fragmentMerge(Map("a.[abc:href]" -> href, "[abc:title]" -> title))

    result should include(s"""href="$href"""")
    result should include(title)
    result should not include "abc:"
  }

  it should "allow fragment merges with attribute replacement and inner html replacement" in {

    val h =
      """
        |<a href="somelink.txt" abc:href abc:link-text>
        | some link text
        |</a>
      """.stripMargin

    val template = Template(h)

    val href = "altered-link.txt"
    val text  = "altered text"
    val result = template.fragmentMerge(Map("a.[abc:href]" -> href, "[abc:link-text]" -> text))


    result should include(s"""href="$href"""")
    result should include(text)
    result should not include "abc:"
  }

  it should "allow for Nested expressions" in {
    sealed trait Sex
    case object Male extends Sex {
      override def toString = "M"
    }
    case object Female extends Sex {
      override def toString = "F"
    }

    case class Location(city: Vector[String])

    case class Person(fn: String, ln: String, sex: Sex, locs: Location)
    val ps = Vector(
      Person("GI", "Joe", Male, Location(Vector("sfo", "chi"))),
      Person("Dane", "Joe", Female, Location(Vector("nyc"))),
      Person("Baby", "Jane", Female, Location(Vector("lon")))
    )

    val h =
      """
        |<div class="bla">
        |  <div class="entry">
        |    <span abc:sex>M</span>
        |    <h1 abc:name>Max Musterman</h1>
        |    <div class="location">
        |      <ul abc:loc>
        |        <li>ber</li>
        |        <li>muc</li>
        |      </ul>
        |    </div>
        |  </div>
        |</div>
      """.stripMargin

    val li = """<li abc:loc-li>ber</li>"""

    val templateLi = Template(li)
    def map(p: Person): Map[String, String] = Map(
      "[abc:sex]" -> p.sex.toString,
      "[abc:name]" -> (p.fn + " " + p.ln),
      "[abc:loc]" -> (for {l <- p.locs.city} yield templateLi.fragmentMerge(Map("[abc:loc-li]" -> l))).mkString)

    val result = (for {
      i <- ps
      x = map(i)
      y = Template(h).fragmentMerge(x)
    } yield y).mkString


    result should include("Dane Joe")
    result should include("Baby Jane")
    result should include("chi")
    result should include("nyc")
    result should include("lon")
    result should not include "abc:"
   }

  it should "allow for Block expressions" in {
   sealed trait Sex
   case object Male extends Sex {
    override def toString = "M"
   }
   case object Female extends Sex {
     override def toString = "F"
   }

   case class Location(city: Vector[String])

   case class Person(fn: String, ln: String, sex: Sex, locs: Location)
   val ps = Vector(
    Person("GI","Joe", Male, Location(Vector("sfo", "chi"))),
    Person("Dane","Joe", Female, Location(Vector("nyc"))),
    Person("Baby","Jane", Female, Location(Vector("lon")))
   )

    val h =
      """
        |<div class="entry">
        |  <span abc:sex>M</span>
        |  <h1 abc:name>Max Musterman</h1>
        |  <ul abc:loc>
        |    <li>ber</li>
        |    <li>muc</li>
        |  </ul>
        |</div>
        |
      """.stripMargin

    val li = """<li abc:loc>ber</li>"""

   val templateLi = Template(li)
    def map(p: Person): Map[String, String] = Map(
     "[abc:sex]" -> p.sex.toString,
     "[abc:name]" -> (p.fn + " "+ p.ln),
     "[abc:loc]" -> (for {l <- p.locs.city } yield templateLi.fragmentMerge(Map("[abc:loc]" -> l))).mkString)

   val result = (for {
      i <- ps
      x = map(i)
      y = Template(h).fragmentMerge(x)
    } yield y).mkString

   result should include("Dane Joe")
   result should include("Baby Jane")
   result should include("chi")
   result should include("nyc")
   result should include("lon")
   result should not include "abc:"
  }

  it should "allow for some form of template and context reuse" in {
    //This is a contrived example .. templates and contextes should be simple
    sealed trait Sex
    case object Male extends Sex {
      override def toString = "M"
    }
    case object Female extends Sex {
      override def toString = "F"
    }

    case class Location(city: Vector[String])

    case class Person(fn: String, ln: String, sex: Sex, locs: Location)
    val ps = Vector(
      Person("GI", "Joe", Male, Location(Vector("sfo", "chi"))),
      Person("Dane", "Joe", Female, Location(Vector("nyc"))),
      Person("Baby", "Jane", Female, Location(Vector("lon")))
    )

    val container =
      """
        |<div class="entry" abc:container>
        | <span abc:sex>M</span>
        | <h1 abc:name>Max Musterman</h1>
        | <div class="location">
        |  <ul abc:loc>
        |    <li>ber</li>
        |    <li>muc</li>
        |  </ul>
        | </div>
        |</div>
      """.stripMargin

    val mini =
      """
        |<span abc:sex>M</span>
        |<h1 abc:name>Max Musterman</h1>
      """.stripMargin


    val more =
      """
        |<div class="location">
        | <ul abc:loc>
        |   <li>ber</li>
        |   <li>muc</li>
        | </ul>
        |</div>
      """.stripMargin

    val li = """<li abc:loc-li>ber</li>"""

    val tc = Template(container)
    val tmini = Template(mini)

    def miniM(p: Person): Map[String, String] = Map(
      "[abc:sex]" -> p.sex.toString,
      "[abc:name]" -> (p.fn + " " + p.ln))

    val tli = Template(li)
    def moreM(p: Person): Map[String, String] = Map(
      "[abc:loc]" -> (for {l <- p.locs.city} yield tli.fragmentMerge(Map("[abc:loc-li]" -> l))).mkString)

    val resultMini = (for {
      i <- ps
      x = miniM(i)
      y = Map("[abc:container]" -> tmini.merge(x))
    } yield tc.fragmentMerge(y)).mkString

    val resultMore = (for {
      i <- ps
      tm = Template(mini + more)
      x = miniM(i) ++ moreM(i)
      y = Map("[abc:container]" -> tm.merge(x))
    } yield tc.fragmentMerge(y)).mkString

    resultMini should include("Baby Jane")
    resultMini should not include "nyc"
    resultMore should include("nyc")
  }
}
