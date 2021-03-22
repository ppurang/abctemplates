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
