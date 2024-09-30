package part2SparkBasics

import org.apache.spark.sql._
import org.apache.spark.sql.types._

object L2DataSources extends App {

  val spark = SparkSession.builder()
    .appName("DataSources")
    .config("spark.master", "local")
    .getOrCreate()

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

  /*
   Reading a DataFrame:
    - format
    - schema (can use inferSchema)
    - zero or more options
    - load on path (can be given in option)
   */
  val carsDF = spark.read
    .format("json")
    .schema(carsSchema)
    .option("mode", "failFast") // dropMalformed, permissive (default)
    .load("src/main/resources/data/cars.json")
    .repartition(10)

  carsDF.show()

  val carsDFWithOptionsMap = spark.read
    .format("json")
    .options(Map(
      "mode" -> "failFast",
      "path" -> "src/main/resources/data/cars.json",
      "inferSchema" -> "true"
    ))
    .load()

  carsDFWithOptionsMap.show()

  /*
  Writing DFs:
    - format
    - save mode: overwrite, append, ignore, errorIfExists
    - path
    - zero or more options
   */
  // Without coalesce, we will have 10 json files
  carsDF.coalesce(1).write
    .format("json")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/data/cars_dupe_2.json")

  // JSON flags
  val carsDFWithFlags = spark.read
    .format("json")
    .option("dateFormat", "YYYY-MM-DD")
    .option("allowSingleQuotes", "true")
    .option("compression", "uncompressed") // bzip2, gzip, lz4, snappy, deflate
    .json("src/main/resources/data/cars.json")

  carsDFWithFlags.show()

  // CSV Flags


}
