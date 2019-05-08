# Brainfuck Interpreter

This is an implementation of [Brainfuck](https://en.wikipedia.org/wiki/Brainfuck) using Scala's [parser combinator library](https://github.com/scala/scala-parser-combinators).
It is not meant to be the most efficient or fastest implementation but as an example of how to 
implement a language using the parser combinator library.

## To Run

You can use SBT to run it, either with a filename:

```scala
sbt "run filename.bf"
```

or brainfuck code:

```scala
sbt "run [brainfuck code]+-."
```