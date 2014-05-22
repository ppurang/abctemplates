# ABC Templates

Simple and unintrusive templates for Scala and perhaps others.

## Motivation

HTML templates should be unintrusive and simple. Ideally they should also be compilable. `ABC Templates` aims to fulfill the first requirement today and postpones the other for the future. 

Unintrusive templates can be seen in the browser and don't differ a lot from the result of applying actual data (or context) to the template.

Simple templates are easy to learn and have very few control structures. 

To achieve simplicity the templates are kept simple but the "merge" requires a bit more work. 

## Quick Start 


```scala


```



## Two things only

### Replace the inner html of an element. 

Given some markup like 
    
    &lt;div id="content" abc:content>
    
      &lt;p>some imaginary data for instant visualization in the browser&lt;/p>
    
    &lt;/div>
        
upon merging it with the context 
     
    Map("div[abc:content]" -> """<p>real data</p>""")

the result is

    &lt;div id="content" abc:content>
    
      &lt;p>real data&lt;/p>
    
    &lt;/div>

**Note:** We use JSoup element selection semantics. If the `abc:content` is unique then `div[abc:content]` and `[abc:content]` have the same effects. 


### Replace the contents of an attribute

Given some markup like 
    
    &lt;a href="path/some_level.html" abc:href>
     link to the next level
    &lt;/a>
        
upon merging it with the context 
     
    Map("a.[abc:href]" -> """real/path/level-01.html""")

the result is

    &lt;a href="real/path/level-01.html">
     link to the next level
    &lt;/a>

**Note:** The distinction between element replacement and attribute replacement is made at the context and not in the template. The prefix `a.` can be read as replace attribute and not the inner html. Hence ` Map("a.[abc:href]" -> """real/path/level-01.html""")` is different from ` Map("[abc:href]" -> """real/path/level-01.html""")`: the previous would result in `<a href="path/some_level.html" abc:href>link to the next level</a>` and the later in `<a href="path/some_level.html" abc:href>link to the next level</a>` 



Of course, you can combine the two

Given some markup like 
    
    &lt;a href="path/some_level.html" abc:href abc:link-text>
     link to the next level
    &lt;/a>
        
upon merging it with the context 
     
    Map("a.[abc:href]" -> """real/path/level-01.html""", "[abc:link-text]" -> """Go to the first level""")

the result is

    &lt;a href="real/path/level-01.html">
     Go to the first level
    &lt;/a>


## Unfortunate 

As we use Jsoup we have an issue with html-fragments (html missing all or any of the `html`, `head` and `body` tags). Jsoup tries to clean an html-fragment by adding `html`, `head` and `body` tags when any or all of them is missing.

A complete html-template, i.e. a template with `html`, `head` and `body` tags, should use the `merge` to merge a context with the template. An html-fragment template should use `mergeFragment`.  


## Recommendation

1. Use unique ids under the namespace `abc:`

2. Validate the result. It is as easy as 

```scala 
   
   import Template._
   
   val result = ...
   
   // second parameter has the default value of "abc:"
   // below we replace it with "abc___:"   
   valid(result, "abc___:")  
   
   
   // choosing "abc___" as prefix should be ok see http://www.w3.org/TR/REC-xml-names/#NT-Prefix and http://www.w3.org/TR/REC-xml/#NT-Name

```

## What's missing?

1. Load from files (this might never end up here) as it is as easy as 

```scala
io.Source.fromFile("some/file").mkString
```

2. Cache the templates. To minimize time to parse the same document again and again it would be good to cache em. Clients could cache the template **but such templates aren't thread safe as the underlying Jsoup document isn't thread safe.** 

3. Performance: We have paid some attention to making the code as fast as possible. We use "static" members where ever possible. Inline stuff. Have a single syntactic `for` on the immutable map. In the end we can't be faster than Jsoup, selection and modification operations.


## Advanced Usage 

Check the `TemplateSpec` spec `allow for Block expressions` in the tests folder.  