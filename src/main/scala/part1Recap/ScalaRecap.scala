package part1Recap

import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

object ScalaRecap extends App {

  println("Hi")

  // instructions vs expressions
  // instructions return nothing (Unit in Scala)
  // expressions are evaluated to a value
  // everything is an expression in Scala
  // i.e. they are evaluated to a value
  // we cannot write just an `if` statement without the else
  val a: String = if (2 < 3) "it's true" else "false!!"
  println(a)

  // functions
  def myInt(x: Int) = x.toString
  println(myInt(2))

  // OOP
  class Animal
  trait Carnivore {
    def eat: Unit
  }
  trait Herbivore {
    def eat: Unit
  }
  class Crocodile extends Animal with Carnivore {
    override def eat: Unit = println("Need Meat!!")
  }
  class Deer extends Animal with Herbivore {
    override def eat: Unit = println("Green Grass!!")
  }
  class Man extends Animal with Carnivore with Herbivore {
    override def eat: Unit = println("I eat everything!!")
  }

  val croc = new Crocodile
  croc.eat

  val deer = new Deer
  deer.eat

  val man = new Man
  man.eat

  // Singleton Objects
  object Singleton {
    val whoAmI = s"I'm the only instance of ${Singleton.toString}!!"
  }
  println(Singleton.whoAmI)
  // Companion Object
  object Carnivore {
    def singleMethod: String = s"I'm Companion of Carnivore, and avialable to all instances of Carnivore"
  }
  println(Carnivore.singleMethod)

  // Generics
  trait MyList[+A] // Co-variant Trait

  // Method notation
  println(1 + 2)
  println(1.+(2))

  // Functional programming
  val incrementer = (x: Int) => x + 1
  val incremented = incrementer(40)
  println(incremented)

  val processedList = List(1, 2, 3, 4).map(incrementer)
  println(processedList)

  // Pattern Matching
  val unknown: Any = 45
  val ordinal = unknown match {
    case _: Int    => "It's an Int"
    case _: String => "It's a String"
    case _         => "It's something else"
  }
  println(ordinal)

  // try-catch
  val throwIt = try {
    throw new NullPointerException("I AM NULL POINTER EXCEPTION!!")
  } catch {
    case e: NullPointerException => e.getMessage
    case _: Throwable            => "I'm some exception!!"
  }
  println(throwIt)

  // Futures
  val aFuture = Future {
    // some expensive computation which runs on another thread
    99
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
  val appliedFunction = (x: Int) => aPartial(x)
  println(appliedFunction(3))

  // Implicits
  // auto-injection by compiler
  def methodWithImplicit(implicit x:Int) =  x + 1
  implicit val implicitInt: Int = 67
  // compiler will automatically inject the argument as 67
  println(methodWithImplicit)

  // Implicit Conversions - implicit def
  case class Person(name: String) {
    def greet = println(s"Hi my name is $name")
  }
  implicit def fromStringToPerson(x: String): Person = Person(x)
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
