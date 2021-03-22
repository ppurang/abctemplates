package org.purang.templates.abc

final case class Pattern(p: String) extends AnyVal
final case class Template(c: String) extends AnyVal
final case class Result(r: String) extends AnyVal
final case class Namespace(ns: String) extends AnyVal

sealed abstract class SubstitutionPattern {
  def isAttributePattern: Boolean
}

final case class AttributePattern(pattern: Pattern)
    extends SubstitutionPattern {
  override val isAttributePattern: Boolean = true
  override def toString(): String = s"a.[${pattern.p}]"
}

final case class ElementPattern(pattern: Pattern) extends SubstitutionPattern {
  override val isAttributePattern: Boolean = false
  override def toString(): String = s"[${pattern.p}]"
}
