package playground

import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import scala.language.implicitConversions
import scala.reflect.ClassTag

object TypedEncodersSample extends App {

  //    class MyObj(val i: Int)
  //    implicit val myObjEncoder = org.apache.spark.sql.Encoders.kryo[MyObj]
  //    val d = spark.createDataset(Seq(new MyObj(1),new MyObj(2),new MyObj(3)))

  implicit def single[A](implicit c: ClassTag[A]): Encoder[A] = Encoders.kryo[A](c)

  implicit def tuple2[A1, A2](
                               implicit e1: Encoder[A1],
                               e2: Encoder[A2]
                             ): Encoder[(A1, A2)] = Encoders.tuple[A1, A2](e1, e2)

  implicit def tuple3[A1, A2, A3](
                                   implicit e1: Encoder[A1],
                                   e2: Encoder[A2],
                                   e3: Encoder[A3]
                                 ): Encoder[(A1, A2, A3)] = Encoders.tuple[A1, A2, A3](e1, e2, e3)

  class MyObj(val i: Int, val u: java.util.UUID, val s: Set[String])

  // alias for the type to convert to and from
  type MyObjEncoded = (Int, String, Set[String])

  // implicit conversions
  implicit def toEncoded(o: MyObj): MyObjEncoded = (o.i, o.u.toString, o.s)

  implicit def fromEncoded(e: MyObjEncoded): MyObj = new MyObj(e._1, java.util.UUID.fromString(e._2), e._3)

  val spark = SparkSession.builder()
    .appName("Typed Encoders Sample")
    .config("spark.master", "local")
    .getOrCreate()

  import spark.implicits._

  val d = spark.createDataset(Seq[MyObjEncoded](
    new MyObj(1, java.util.UUID.randomUUID, Set("foo")),
    new MyObj(2, java.util.UUID.randomUUID, Set("bar"))
  )).toDF("i", "u", "s").as[MyObjEncoded]

  d.printSchema
  d.show()

}
