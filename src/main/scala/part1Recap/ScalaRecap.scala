package part1Recap

import scala.util.{Failure, Success, Try}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

object ScalaRecap extends App {

  println("Hi")

  // value and variables
  val anImmutableValue: Int = 5 // immutable value, no reassignment
  var aMutableVariable: Int = 8 // mutable variable, can be reassigned
  println(anImmutableValue)
  println(aMutableVariable)

  // instructions vs expressions
  // instructions return nothing (Unit in Scala)
  // expressions are evaluated to a value
  // everything is an expression in Scala
  // i.e. they are evaluated to a value
  // we cannot write just an `if` statement without the else
  val a: String = if (2 < 3) "it's true" else "false!!"
  println(a) // println returns unit

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
  val oldIncrementer: Int => Int = x => x + 1
  val oldIncremented = oldIncrementer(42)
  println(oldIncremented)

  val incrementer = (x: Int) => x + 1 // same as oldIncrementer, with sugar
  val incremented = incrementer(40)
  println(incremented)

  // map, flatMap, filters are HoFs, as they takes function as a parameter
  val processedList = List(1, 2, 3, 4).map(incrementer)
  println(processedList)

  // Pattern Matching
  val unknown: Any = "Hi"
  val ordinal = unknown match {
    case _: Int    => "It's an Int"
    case _: String => "It's a String"
    case _         => "It's something else"
  }
  println(ordinal)

  // Type erasure issue with pattern matching
  // List[Any], List[Int], List[String] all become List due to type erasure
  def processList(list: List[Any]): String = list match {
    case _: List[Int] => "List of Int"
    case _: List[String] => "List of Strings"
    case _ => "Unknown List"
  }
  println(processList(List("a", "b", "c"))) // Will print "List of Int"


  // try-catch
  val throwIt = try {
    throw new NullPointerException("I AM NULL POINTER EXCEPTION!!")
  } catch {
    case e: NullPointerException => e.getMessage
    case _: Throwable            => "I'm some exception!!"
  }
  println(throwIt)

  // Using Try
  val aTry = Try(throw new NullPointerException("I AM NULL POINTER EXCEPTION!!"))
  val result = aTry match {
    case Success(_) => "It was successful"
    case Failure(e) => s"Found the following error: ${e.getMessage}"
  }
  println(result)

  // Futures
  val aFuture = Future {
    // some expensive computation which runs on another thread
    99
  }
  aFuture.onComplete {
    case Success(value) => println(s"$value")
    case Failure(ex) => println(s"${ex.getMessage}")
  }

  // Partial Functions
  val aPartial: PartialFunction[Int, Int] = {
    case 1 => 45
    case 2 => 67
    case 3 => 89
    case _ => 0
  }
  val appliedFunction = (x: Int) => aPartial(x)
  println(appliedFunction(3))

  // Implicits
  // auto-injection by compiler
  def methodWithImplicit(implicit x:Int) =  x + 1
  implicit val implicitInt: Int = 67
  // compiler will automatically inject the argument as 67
  val implicitCall = methodWithImplicit
  println(implicitCall)

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
