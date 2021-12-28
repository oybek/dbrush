package io.github.oybek.dbrush.compiler

trait Compiler[A, B] {
  def compile(a: A): B
}
