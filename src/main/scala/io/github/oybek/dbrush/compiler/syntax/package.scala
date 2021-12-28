package io.github.oybek.dbrush.compiler

import doobie.Fragment

package object syntax {
  class CompilerOps[A](val a: A) extends AnyVal {
    def compile(implicit compiler: Compiler[A, Fragment]): Fragment =
      compiler.compile(a)
  }

  implicit def toCompilerOps[A](a: A): CompilerOps[A] =
    new CompilerOps[A](a)
}
