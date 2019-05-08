import scala.util.matching.Regex
import scala.util.parsing.combinator.JavaTokenParsers

sealed trait Command
case class IncrementPointer() extends Command
case class DecrementPointer() extends Command
case class IncrementData() extends Command
case class DecrementData() extends Command
case class Print() extends Command
case class PrintState() extends Command
case class Input() extends Command
case class Loop(expressions: List[Command]) extends Command
case class Program(expressions: List[Command])

object BrainfuckParser extends JavaTokenParsers {

  //any character other than a command character is treated as whitespace
  override protected val whiteSpace: Regex = """[^><\+\-\[\]\.#,]*""".r

  def loop: Parser[Loop] = "[" ~> rep(command) <~ "]" ^^ { Loop }

  def command: Parser[Command] = ("<" | ">" | "+" | "-" | "." | "," | "#" | loop) ^^ {
    case ">" => IncrementPointer()
    case "<" => DecrementPointer()
    case "+" => IncrementData()
    case "-" => DecrementData()
    case "." => Print()
    case "," => Input()
    case "#" => PrintState()
    case Loop(expressions) => Loop(expressions)
  }

  def program: Parser[Program] = rep(command) ^^ { Program }

  def parse(s: String): BrainfuckParser.ParseResult[Program] = parseAll(program, s)
}


