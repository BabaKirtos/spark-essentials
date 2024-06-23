package part2SparkBasics

import org.apache.spark.sql._
import org.apache.spark.sql.types._

object L1DataFramesBasics extends App {

  // creating a Spark Session
  val spark = SparkSession.builder()
    .appName("DataFrameBasics")
    .config("spark.master", "local")
    .getOrCreate()

  // Reading a spark dataframe
  val firstDf: DataFrame = spark.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/cars.json")
    .repartition(10)

  // showing a DF
  firstDf.show()
  firstDf.printSchema()
  println(firstDf.rdd.getNumPartitions)

  // get rows
  val takeFirstDF: Array[Row] = firstDf.take(10)
  takeFirstDF.foreach(println)

  // spark types
  val longType = LongType

  // define car json schema
  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", DateType),
    StructField("Origin", StringType)
  ))

  // Notice the difference between Year
  val carsDFSchema = firstDf.schema
  println(carsDFSchema)

  // let's use our own schema
  val carsDF = spark.read
    .format("json")
    .schema(carsSchema)
    .load("src/main/resources/data/cars.json")
    .repartition(10)

  carsDF.show()
  carsDF.printSchema()

  // create a row by hand
  val myRows = Row("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA")

  // create a DF from Seq of Tuples
  val cars = Seq(
    ("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA"),
    ("buick skylark 320", 15.0, 8L, 350.0, 165L, 3693L, 11.5, "1970-01-01", "USA"),
    ("amc rebel sst", 16.0, 8L, 304.0, 150L, 3433L, 12.0, "1970-01-01", "USA"),
    ("plymouth satellite", 18.0, 8L, 318.0, 150L, 3436L, 11.0, "1970-01-01", "USA"),
    ("ford torino", 17.0, 8L, 302.0, 140L, 3449L, 10.5, "1970-01-01", "USA"),
    ("ford galaxie 500", 15.0, 8L, 429.0, 198L, 4341L, 10.0, "1970-01-01", "USA"),
    ("chevrolet impala", 14.0, 8L, 454.0, 220L, 4354L, 9.0, "1970-01-01", "USA"),
    ("plymouth fury iii", 14.0, 8L, 440.0, 215L, 4312L, 8.5, "1970-01-01", "USA"),
    ("pontiac catalina", 14.0, 8L, 455.0, 225L, 4425L, 10.0, "1970-01-01", "USA"),
    ("amc ambassador dpl", 15.0, 8L, 390.0, 190L, 3850L, 8.5, "1970-01-01", "USA"))

  val manualCarsDF = spark.createDataFrame(cars) // schema is auto inferred

  // NOTE: DFs have schemas, Rows do not, Row is unstructured data

  manualCarsDF.show()

  // create DFs with implicits

  import spark.implicits._

  val manualCarsDFWithImplicis = cars.toDF(
    "Name",
    "Miles_per_Gallon",
    "Cylinders",
    "Displacement",
    "Horsepower",
    "Weight_in_lbs",
    "Acceleration",
    "Year",
    "Origin")

  manualCarsDFWithImplicis.show()

  // Lets check schemas for both
  manualCarsDF.printSchema()
  manualCarsDFWithImplicis.printSchema()

  /*
  Exercise:
    1. Create a manual DF for Smart Phones
      - make
      - model
      - screen dimension
      - camera megapixels
    2. Read another file from Data folder - movies.json
      - print schema
      - count number of rows
   */

  val moviesDF = spark.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/movies.json")
    .repartition(10)

  moviesDF.printSchema()
  println(moviesDF.count())

}
