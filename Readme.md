[![Download](https://api.bintray.com/packages/ppurang/maven/asynch/images/download.svg) ](https://bintray.com/ppurang/maven/asynch/_latestVersion)

# ABC Templates

**Simple and un-intrusive templates for Scala and perhaps others.**

## Motivation

HTML templates should be un-intrusive and simple. Ideally they should also be compilable. `ABC Templates` aims to fulfill the first requirement today and postpones the other for the future. 

Un-intrusive templates can be seen in the browser and don't differ a lot from the result of applying actual data (or context) to the template.

Simple templates are easy to learn and have very few control structures. 

To achieve simplicity the templates are kept simple, but the "merge" requires a bit more work. 

## Quick Start 

Include dependency (for `scala 2.10` use version `0.2.2`, `scala 2.12` use `0.4.1`, `scala 2.13` use `0.5.0`). 

```scala
libraryDependencies ++= Seq(
  "org.purang.net" %% "abctemplates" % "3.1.0-M3" withSources()
)
```

available from:

```scala
resolvers += "ppurang bintray" at "http://dl.bintray.com/ppurang/maven"
```

## Usage

Quick and **unsafe**<sup>*</sup> usage - 

```scala
    import org.purang.templates.abc.unsafe.Template._
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
    val result = template.merge(context)

    println(result)
    
    //result should be (note: <p> tags have been dropped)
    //  <div id="content">
    //    <h1>the real content!</h1>  
    //  </div>

```
<sup>__*__</sup> Unsafe api exposes imperative OOP style usage. Things might break unexpectedly. Check **[Safe, cats-effect enabled, usage](#safe-cats-effect-enabled-usage)** section right at the end.


## Only two things and just those two things 

Seriously - just 2 things are allowed.

### 1. Replace the inner html of an element. 

Given some markup like 
```html    
    <div id="content" abc:content>
      <p>some imaginary data for instant visualization in the browser</p>
    </div>
```        
upon merging it with the context 
     
```scala
    Map("div[abc:content]" -> """<p>real data</p>""")
```

the result is

```html
    <div id="content">
      <p>real data</p>
    </div>
```

**Note:** We use `JSoup` element selection semantics. If the `abc:content` is unique then `div[abc:content]` and `[abc:content]` have the same effects. 


### 2. Replace the contents of an attribute

Given some markup like 

```html
    <a href="path/some_level.html" abc:href>
     link to the next level
    </a>
```        
upon merging it with the context 
```scala
    Map("a.[abc:href]" -> """real/path/level-01.html""")
```
the result is
```html
    <a href="real/path/level-01.html">
     link to the next level
    </a>
```

**Note:** The distinction between `element replacement` and `attribute replacement` is <ins>made by the `context`</ins> and not in the `template`. 

The prefix `a.` can be read as - replace an attribute and not the inner html. Hence, the context `Map("a.[abc:href]" -> """real/path/level-01.html""")` is different from ` Map("[abc:href]" -> """real/path/level-01.html""")`: the previous would result in `<a href="real/path/level-01.html">link to the next level</a>` and the latter in `<a href="path/some_level.html">real/path/level-01.html</a>`. Here is the distinction as example code blocks:

```scala
    import Template._
    val t = Template("""<a href="path/some_level.html" abc:href>link to the next level</a>""")
    val c = Map("a.[abc:href]" -> """real/path/level-01.html""")   //note the 'a.' in the key
    
    t.merge(c)
    
    //results in the 'href' attribute being replaced
    //<a href="real/path/level-01.html">link to the next level</a>
```

```scala
    import Template._
    val t = Template("""<a href="path/some_level.html" abc:href>link to the next level</a>""")
    val c = Map("[abc:href]" -> """real/path/level-01.html""")   //note how the 'a.' is missing in the key
    
    t.merge(c)
    
    //results in an unaltered 'href' attribute but the inner html gets replaced
    //<a href="path/some_level.html">real/path/level-01.html</a>
```

Of course, you can combine the two

Given some markup like 

```html
    <a href="path/some_level.html" abc:href abc:link-text>
     link to the next level
    </a>
```
upon merging it with the context 

```scala
    Map(
        "a.[abc:href]" -> """real/path/level-01.html""", 
        "[abc:link-text]" -> """Go to the first level"""
    )
```
the result is
```html
    <a href="real/path/level-01.html">
     Go to the first level
    </a>
```

For advanced usages check out the section **[Advanced Usage](#advanced-usage)** below. 

## Recommendation

1. Use unique ids under the namespace `abc:`

2. Don't cache the Templates. 

3. Validate the result. It is as easy as 

```scala 
   import Template._
   
   val result = ...
   
   // second parameter has the default value of "abc:"
   // below we replace it with "abc___:"   
   valid(result, "abc___:")  
   
   
   // choosing "abc___" as prefix should be ok
   // see http://www.w3.org/TR/REC-xml-names/#NT-Prefix 
   // and http://www.w3.org/TR/REC-xml/#NT-Name
```


## What's missing or included?

1. Load from files (this might never end up here) as it is as easy as 

```scala
io.Source.fromFile("some/file").mkString //this isn't correct usage; will leak resources
                                         //make sure you know how to use io.Source properly 
```

2. Performance: We have paid some attention to making the code as fast as possible. We use "static" members where ever possible. Inline stuff. Have a single syntactic `for` on the immutable map. In the end we can't be faster than Jsoup, selection and modification operations.


## Advanced Usage 


1. Collections:
 
```scala
   sealed trait Sex
   case object Male extends Sex {
    override def toString = "M"
   }
   case object Female extends Sex {
     override def toString = "F"
   }

   case class Location(city: Vector[String])

   case class Person(fn: String, ln: String, sex: Sex, locs: Location)

   //a collection
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

   val li = """<li abc:loc-li>ber</li>""" //a sub template

   // render the sub template as part of the context
   val templateLi = Template(li)
    def map(p: Person): Map[String, String] = Map(
     "[abc:sex]" -> p.sex.toString,
     "[abc:name]" -> (p.fn + " "+ p.ln),
     "[abc:loc]" -> (for {l <- p.locs.city } yield templateLi.merge(Map("[abc:loc-li]" -> l))).mkString)

   val result = (for {
      i <- ps
      x = map(i)
      y = Template(h).merge(x)
    } yield y).mkString
    
    /* results in:
    <div class="entry">
      <span>M</span>
       <h1>GI Joe</h1>
       <ul>
        <li>sfo</li>
        <li>chi</li>
       </ul>
    </div><div class="entry">
      <span>F</span>
       <h1>Dane Joe</h1>
       <ul>
        <li>nyc</li>
       </ul>
      </div><div class="entry">
       <span>F</span>
       <h1>Baby Jane</h1>
       <ul>
        <li>lon</li>
       </ul>
    </div>*/
```

You can always unleash the entire power of `scala` to accomplish complicated usages like this. Almost anything is possible and things remain simple and yet un-intrusive.


## Safe, cats-effect enabled, usage

You can check out  `TemplatesApp` in `test` sources. 

```scala
import org.purang.templates.abc._
import org.purang.templates.abc.unsafe.Template._
import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.flatMap._
import cats.syntax.functor._

object TemplatesApp extends IOApp {

  def prg[F[+_] : Monad](talg: Templates[F]): F[(Result, Boolean)] = {
    for {
      t <- talg.template(
        """|<div id="content" abc:content>
           |  <p>
           |    some imaginary data for instant visualization in the browser
           |  </p>
           |</div>""".stripMargin)
      m <- talg.merge(
        t,
        Map(
          ElementPattern(Pattern("abc:content")) -> "<b>What's Up Folks!<b>"
        )
      )
      v <- talg.validate(m, Namespace("abc"))
    } yield (m, v)
  }

  def run(args: List[String]): IO[ExitCode] = {
    (for {
      t <- prg[IO](JsoupTemplates.default)
      _ <- IO {
        println(t)
      }
    } yield ()).as(ExitCode.Success)
  }
}
```

Running it results in:

```text
sbt:abctemplates-ng> test:runMain TemplatesApp
[warn] multiple main classes detected: run 'show discoveredMainClasses' to see the list
[info] running (fork) TemplatesApp 
[info] (Result(<div id="content"><b>What's Up Folks!<b></b></b></div>),true)
[success] Total time: 1 s, completed Jan 3, 2021, 7:08:23 PM
sbt:abctemplates-ng>
```

