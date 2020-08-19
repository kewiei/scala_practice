
import org.apache.log4j._
import org.apache.spark._

object PopMovie {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]","popmovie")
    val lines = sc.textFile("C:\\_Ongoing\\scalaproject\\ml-100k\\u.data")
    val movies = lines.map(x=>(x.split("\t")(1),1))
    val moviecount = movies.reduceByKey((x,y)=>x+y)
    val flipped = moviecount.map(x=>(x._2,x._1))
    val sorted = flipped.sortByKey()
    val result = sorted.collect()
    result.foreach(println)
  }
}
