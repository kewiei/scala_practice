import org.apache.spark._
import org.apache.spark.SparkContext._
// import org.apache.spark.sql.SparkSession
// import org.apache.hadoop.fs.{FileSystem, Path}
// import org.apache.hadoop.conf.Configuration
import org.apache.log4j._
import scala.io.Source
import java.nio.charset.CodingErrorAction
import scala.io.Codec
import java.{util => ju}

object popularMovie {
  def loadNameMap(itemSource: String): java.util.HashMap[Int, String] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    // var movieNameMap: Map[Int,String] = Map()
    // val lines = Source.fromFile("u.item").getLines()
    // // val lines = Source.fromFile("s3n://tianmu-personal/ml-100k/u.item").getLines()
    // for (line<-lines){
    //     val fields = line.split('|')
    //     if (fields.length>1){
    //         movieNameMap += (fields(0).toInt -> fields(1))
    //     }
    // }
    // return movieNameMap
    var movieNameMap: java.util.HashMap[Int, String] =
      new java.util.HashMap[Int, String]
    val lines = Source.fromFile(itemSource).getLines()
    // val lines = Source.fromFile("s3n://tianmu-personal/ml-100k/u.item").getLines()
    for (line <- lines) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNameMap.put(fields(0).toInt, fields(1))
      }
    }
    return movieNameMap
  }
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    // val spark:SparkSession = SparkSession.builder
    //     .master("local[*]")
    //     .appName("popularMovie")
    //     .getOrCreate()
    
    // val sc: SparkContext = new SparkContext("local[*]", "popularMovie")
    val sc:SparkContext = new SparkContext("yarn","popularMovie")

    // val hadoopConf = sc.hadoopConfiguration
    // hadoopConf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
    // hadoopConf.set("fs.s3.awsAccessKeyId", "AKIAVZP6FPUPOZGTMD6D")
    // hadoopConf.set("fs.s3.awsSecretAccessKey", "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")
    // val dataSource:String = "s3://tianmu-personal/ml-100k/u.data"
    // val itemSource:String = "s3://tianmu-personal/ml-100k/u.item"

    // val dataSource:String = "C:/_Ongoing/ml-100k/u.data"
    // val itemSource:String = "C:/_Ongoing/ml-100k/u.item"
    val dataSource:String = "u.data"
    val itemSource:String = "u.item"

    // val s3Login = "s3://AKIAVZP6FPUPOZGTMD6D:F0CpFRB4IAferK23pv0Kxb9k2OkuTC2ESpOk9ktC@tianmu-personal"
    // val dataSource: String = s3Login + "/ml-100k/u.data"
    // val itemSource: String = s3Login + "/ml-100k/u.item"


    // val record = spark.read.textFile(dataSource).collect.foreach(println)


    val record = sc
      .textFile(dataSource)
      .map(x => (x.split("\t")(1).toInt, 1))

    val nameMap1 = sc.broadcast(loadNameMap(itemSource))
    // println(nameMap1.value.getClass)
    // val nameMap = sc.textFile(itemSource).map(x=>(x.split('|')(0),x.split('|')(1))).collect.toMap

    var flipped = record.reduceByKey(_ + _).map(x => (x._2, x._1))

    val sorted = flipped.sortByKey()

    // val withName = sorted.map(x=>(nameMap.getOrElse(x._2.toString,"Movie not found"),x._1))
    // val withName = sorted.map(x=>(nameMap.getOrElse(x._2.toString,"Movie not found"),x._1))
    val withName = sorted.map(x => (nameMap1.value.get(x._2), x._1))

    val sort_again = withName.map(x => (x._2, x._1)).sortByKey()

    println("<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>")
    val remaining = sort_again.collect.foreach(println)
  }
}

// spark-submit --class popularMovie /home/hadoop/popularmovie_2.11-0.1.0-SNAPSHOT.jar
