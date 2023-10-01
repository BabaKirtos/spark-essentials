package part2SparkBasics

import org.apache.spark.sql.SparkSession

object L1DataFramesBasics extends App {

  // creating a Spark Session
  val spark = SparkSession.builder()
    .appName("DataFrameBasics")
    .config("spark.master", "local")
    .getOrCreate()

  // Reading a spark dataframe
  val firstDf = spark.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/cars.json")

  // showing a DF
  firstDf.show()
  firstDf.printSchema()

  // get rows
  firstDf.take(10).foreach(println)



}
