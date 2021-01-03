package org.purang.templates.abc.unsafe

import org.purang.templates.abc.unsafe._

class UnsafeTemplateSpec extends munit.FunSuite {
  
  test("allow fragment merge with a single key") {
    val h: String =
      """|<div id="content" doh-content>
         |  <p>
         |    some imaginary data for instant visualization in the browser
         |  </p>
         |</div>
      """.stripMargin

    val template = Template(h)

    val content: String = "<p>the real content!</p>"
    val attribute: String = "doh-content"
    val result: String = template.merge(Map("div[doh-content]" -> content))

    assert(result.contains(content))
    assert(!result.contains(attribute))
  }

  test("allow fragment merge with a single key and line breaks") {
    val h: String =
      """|<div id="content" doh-content>
         |  <p>
         |    some imaginary data for instant visualization in the browser
         |  </p>
         |</div>
      """.stripMargin

    val template = Template(h)

    val content: String = "<p>the real \n content!</p>"
    val attribute: String = "doh-content"
    val result: String = template.merge(Map("div[doh-content]" -> content))

    assert(result.contains(content))
    assert(!result.contains(attribute))
  }

  test("allow fragment merges with multiple keys") {

    val h: String =
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

    val title: String = "altered title"
    val body: String = "altered body"
    val result: String =
      template.merge(Map("[abc:title]" -> title, "[abc:body]" -> body))

    assert(result.contains(title))
    assert(result.contains(body))
    assert(!result.contains("abc:"))
  }

  test("allow fragment merges with attribute replacement") {

    val h: String =
      """
        |  <link rel="stylesheet" type="text/css" media="print" href="stylesheets/print.css" abc:href/>
        |  <h1 abc:title>title </h1>
      """.stripMargin

    val template = Template(h)

    val href: String = "stylesheets/print-sgshgshg.css"
    val title: String = "altered title"
    val result: String =
      template.merge(Map("a.[abc:href]" -> href, "[abc:title]" -> title))

    assert(result.contains(s"""href="$href""""))
    assert(result.contains(title))
    assert(!result.contains("abc:"))
  }

  test("allow fragment merges with attribute replacement and inner html replacement") {

    val h: String =
      """
        |<a href="somelink.txt" abc:href abc:link-text>
        | some link text
        |</a>
      """.stripMargin

    val template = Template(h)

    val href: String = "altered-link.txt"
    val text: String = "altered text"
    val result: String =
      template.merge(Map("a.[abc:href]" -> href, "[abc:link-text]" -> text))

    assert(result.contains(s"""href="$href""""))
    assert(result.contains(text))
    assert(!result.contains("abc:"))
  }

  test("allow for Nested expressions") {
    sealed trait Sex
    case object Male extends Sex {
      override def toString = "M"
    }
    case object Female extends Sex {
      override def toString = "F"
    }

    case class Location(city: Vector[String])

    case class Person(fn: String, ln: String, sex: Sex, locs: Location)
    val ps: Vector[Person] = Vector(
      Person("GI", "Joe", Male, Location(Vector("sfo", "chi"))),
      Person("Dane", "Joe", Female, Location(Vector("nyc"))),
      Person("Baby", "Jane", Female, Location(Vector("lon")))
    )

    val h: String =
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

    val li: String = """<li abc:loc-li>ber</li>"""

    val templateLi = Template(li)
    def map(p: Person): Map[String, String] = Map(
      "[abc:sex]" -> p.sex.toString,
      "[abc:name]" -> (p.fn + " " + p.ln),
      "[abc:loc]" -> (for { l <- p.locs.city } yield templateLi.merge(
        Map("[abc:loc-li]" -> l)
      )).mkString
    )

    val result: String = (for {
      i <- ps
      x = map(i)
      y = Template(h).merge(x)
    } yield y).mkString

    assert(result.contains("Dane Joe"))
    assert(result.contains("Baby Jane"))
    assert(result.contains("chi"))
    assert(result.contains("nyc"))
    assert(result.contains("lon"))
    assert(!result.contains("abc:"))
  }

  test("allow for Block expressions") {
    sealed trait Sex
    case object Male extends Sex {
      override def toString = "M"
    }
    case object Female extends Sex {
      override def toString = "F"
    }

    case class Location(city: Vector[String])

    case class Person(fn: String, ln: String, sex: Sex, locs: Location)
    val ps: Vector[Person] = Vector(
      Person("GI", "Joe", Male, Location(Vector("sfo", "chi"))),
      Person("Dane", "Joe", Female, Location(Vector("nyc"))),
      Person("Baby", "Jane", Female, Location(Vector("lon")))
    )

    val h: String =
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

    val li: String = """<li abc:loc>ber</li>"""

    val templateLi = Template(li)
    def map(p: Person): Map[String, String] = Map(
      "[abc:sex]" -> p.sex.toString,
      "[abc:name]" -> (p.fn + " " + p.ln),
      "[abc:loc]" -> (for { l <- p.locs.city } yield templateLi.merge(
        Map("[abc:loc]" -> l)
      )).mkString
    )

    val result: String = (for {
      i <- ps
      x = map(i)
      y = Template(h).merge(x)
    } yield y).mkString

    assert(result.contains("Dane Joe"))
    assert(result.contains("Baby Jane"))
    assert(result.contains("chi"))
    assert(result.contains("nyc"))
    assert(result.contains("lon"))
    assert(!result.contains("abc:"))
  }

  test("allow for some form of template and context reuse") {
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
    val ps: Vector[Person] = Vector(
      Person("GI", "Joe", Male, Location(Vector("sfo", "chi"))),
      Person("Dane", "Joe", Female, Location(Vector("nyc"))),
      Person("Baby", "Jane", Female, Location(Vector("lon")))
    )

    val container: String =
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

    val mini: String =
      """
        |<span abc:sex>M</span>
        |<h1 abc:name>Max Musterman</h1>
      """.stripMargin

    val more: String =
      """
        |<div class="location">
        | <ul abc:loc>
        |   <li>ber</li>
        |   <li>muc</li>
        | </ul>
        |</div>
      """.stripMargin

    val li: String = """<li abc:loc-li>ber</li>"""

    val tc = Template(container)
    val tmini = Template(mini)

    def miniM(p: Person): Map[String, String] =
      Map("[abc:sex]" -> p.sex.toString, "[abc:name]" -> (p.fn + " " + p.ln))

    val tli = Template(li)
    def moreM(p: Person): Map[String, String] = Map("[abc:loc]" -> (for {
      l <- p.locs.city
    } yield tli.merge(Map("[abc:loc-li]" -> l))).mkString)

    val resultMini: String = (for {
      i <- ps
      x = miniM(i)
      y = Map("[abc:container]" -> tmini.merge(x))
    } yield tc.merge(y)).mkString

    val resultMore: String = (for {
      i <- ps
      tm = Template(mini + more)
      x = miniM(i) ++ moreM(i)
      y = Map("[abc:container]" -> tm.merge(x))
    } yield tc.merge(y)).mkString

    assert(resultMini.contains("Baby Jane"))
    assert(!resultMini.contains("chi"))
    assert(resultMore.contains("nyc"))
  }
}
