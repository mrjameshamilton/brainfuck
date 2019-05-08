import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import BrainfuckInterpreter._
import BrainfuckParser._
import org.scalatest.{BeforeAndAfter, FlatSpec}
import scala.io.Source

class AppSpec extends FlatSpec with BeforeAndAfter {

  var env: Environment = _

  before {
    env = new Environment
  }

  "[P]" should "be empty loop" in {
    val program = parse("[P]")
    exec(program.get, env)
    assert(env.get(0) == 0)
  }

  "++++++++" should "increment 0 to 8" in {
    exec(parse("++++++++").get, env)
    assert(env.get(0) == 8)
  }

  "++++----" should "set 0 to 0" in {
    exec(parse("++++----").get, env)
    assert(env.get(0) == 0)
  }

  ">+" should "increment 1 to 1" in {
    exec(parse(">+").get, env)
    assert(env.get(1) == 1)
  }

  ">>>>><<++++-" should "increment 3 to 3" in {
    exec(parse(">>>>><<++++-").get, env)
    assert(env.get(3) == 3)
  }

  "++++[>+<-]" should "increment 1 to 4" in {
    exec(parse("++++[>+<-]").get, env)
    assert(env.get(1) == 4)
  }

  "+++[>+++[>+<---]<---] with nested loops" should "work" in {
    val program = parse("+++[>+++[>+<---]<---]")
    exec(program.get, env)
    assert(env.get(0) == 0)
    assert(env.get(1) == 0)
    assert(env.get(2) == 1)
  }

  "Parser" should "should not parse bad code" in {
    val program = parse("+++++[>+++++++>++<<-]>.>.[")
    assert(!program.successful)
  }

  "Hello world" should "produce Hello World" in {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream) {
      exec(parse("[prints Hello World!]++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.").get, env)
    }
    assert(stream.toString == "Hello World!\n")
  }

  "Obscure problems" should "output H" in {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream) {
      exec(parse("[]++++++++++[>>+>+>++++++[<<+<+++>>>-]<<<<-]\n\"A*$\";?@![#>>+<<]>[>>]<<<<[>++<[-]]>.>.").get, env)
    }
    assert(stream.toString == "H\n")
  }

  "rot13" should "produce correct output" in {
    val source = Source.fromResource("rot13.bf").getLines.mkString
    val inputStream = new ByteArrayInputStream("~mlk zyx".getBytes)
    val outputStream = new ByteArrayOutputStream()
      Console.withOut(outputStream) {
        Console.withIn(inputStream) {
        exec(parse(source).get, env)
      }
    }
    assert(outputStream.toString == "~zyx mlk")
  }

  "Interpreter" must "ignore comments" in {
    exec(parse("[initial comment can contain commands like +, - and []][test]>++--").get, env)
    assert(env.get(1) == 0)
  }

  it should "print state with # command" in {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream) {
      exec(parse("+++[#---#]").get, env)
    }
    assert(stream.toString == "^0, Map(0 -> 3)\n^0, Map(0 -> 0)\n")
  }

  it should "have at least 30000 data cells" in {
    exec(parse("++++[>++++++<-]>[>+++++>+++++++<<-]>>++++<[[>[[>>+<<-]<]>>>-]>-[>+>+<<-]>]+++++[>+++++++<<++>-]").get, env)
    assert(env.get(29999) == '#'.toInt)
  }
}
