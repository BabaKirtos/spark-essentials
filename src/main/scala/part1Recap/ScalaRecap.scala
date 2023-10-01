package part1Recap

import scala.util.{Success, Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaRecap extends App {

  println("Hi")

  // everything is an expression in Scala
  // i.e. they are evaluated to a value
  // we cannot write just a if statement without the else
  val a: String = if (2 < 3) "it's true" else "false!!"

  val b: Int = 2
  println(b)

  // Futures
  val aFuture = Future {
    // some expensive computation which runs on another thread
    42
  }
  aFuture.onComplete {
    case Success(value) => println(s"$value")
    case Failure(exception) => println(s"${exception.getMessage}")
  }

  // Partial Functions
  val aPartial: PartialFunction[Int, Int] = {
    case 1 => 45
    case _ => 0
  }

  // Implicits
  // auto-injection by compiler
  def methodWithImplicit(implicit x:Int) =  x + 1
  implicit val implicitInt = 67
  // compiler will automatically inject the argument as 67
  println(methodWithImplicit)

  // Implicit Conversions - implicit def
  case class Person(name: String) {
    def greet = println(s"Hi my name is $name")
  }
  implicit def fromStringToPerson(x: String) = Person(x)
  // compiler will allow calling the greet method
  // as it implicitly converts String to Person
  "Bob".greet // fromStringToPerson("Bob").greet

  implicit class Dog(name: String) {
    def bark = println(s"$name says Woof!")
  }
  "Loki".bark

  /*
  * The compiler looks for implicits in the following order
  * 1. local scope
  * 2. imported scope (like execution context for futures
  * 3. companion objects of the types involved in the method call
  * */

}
