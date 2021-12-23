package org.purang.templates.abc

import cats._
import org.purang.templates.abc.unsafe.{Template => UnsafeTemplate}

object JsoupTemplates {

  // def default[F[+_]: [M[_]] =>> ApplicativeError[M, Throwable]]: TemplateAlgebra[F] = new TemplateAlgebra[F] {
  def default[F[+_]](implicit F: ApplicativeError[F, Throwable]): Templates[F] =
    new Templates[F] {

      override def template[A](contents: String): F[Template] =
        F.pure(Template(contents))

      override def merge(
          template: Template,
          context: => Map[SubstitutionPattern, String]
      ): F[Result] = F.catchNonFatal {
        Result(UnsafeTemplate(template.c).merge(context.map { case (a, b) =>
          (a.toString, b)
        }))
      }

      override def mergeSimple(
          template: Template,
          context: => Map[String, String]
      ): F[Result] = F.catchNonFatal {
        Result(UnsafeTemplate(template.c).merge(context))
      }

      override def embeddedTemplate(
          template: Template,
          cssQuery: String
      ): F[Option[Template]] = F.catchNonFatal {
        UnsafeTemplate(template.c).embeddedTemplate(cssQuery).map(Template(_))
      }

      override def embeddedTemplates(
          template: Template,
          cssQueries: List[String]
      ): F[Option[Template]] = F.catchNonFatal {
        UnsafeTemplate(template.c)
          .embeddedTemplates(cssQueries)
          .map(Template(_))
      }

      override def validate(result: Result, ns: Namespace): F[Boolean] =
        F.catchNonFatal(UnsafeTemplate.valid(result.r, ns.ns))

    }
}
