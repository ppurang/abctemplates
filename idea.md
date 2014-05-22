












http://www.benmccann.com/blog/java-html-parsing-library-comparison/

http://htmlcleaner.sourceforge.net/download.php (http://alvinalexander.com/scala/scala-html-parsing)

http://jsoup.org/

http://jsoup.org/cookbook/extracting-data/selector-syntax

https://xjaphx.wordpress.com/2012/02/


[^attr]: elements with an attribute name prefix, e.g. [^data-] finds elements with HTML5 dataset attributes

http://jsoup.org/cookbook/modifying-data/set-html


    <div id="content" doh-content>
      <p>
        some imaginary data for instant visualization in the browser
      </p>
    </div>


    val m = Map("content" -> "<p>the real content!</p>")
    html(m)
       <div id="content">
         <p>the real content!</p>
       </div>

