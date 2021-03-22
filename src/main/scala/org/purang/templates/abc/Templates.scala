package org.purang.templates.abc

import cats._

trait Templates[+F[+_]] {
  def template[A](contents: String): F[Template]

  def merge(
      template: Template,
      context: => Map[SubstitutionPattern, String]
  ): F[Result]

  def mergeSimple(
      template: Template,
      context: => Map[String, String]
  ): F[Result]

  def embeddedTemplate(
      template: Template,
      cssQuery: String
  ): F[Option[Template]]

  def embeddedTemplates(
      template: Template,
      cssQueries: List[String]
  ): F[Option[Template]]

  def validate(result: Result, ns: Namespace): F[Boolean]
}

//A thought, hold it!
//trait Templates2[+F[+_, +_], +E] {
//  def template[A](contents: String): F[E, Template]
//
//  def merge(template: Template,
//            context: Map[SubstitutionPattern, String]): F[E, Result]
//
//  def validate(result: Result,
//               ns: Namespace): F[E, Boolean]
//}
