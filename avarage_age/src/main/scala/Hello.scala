import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object Hello{
  /**
  **/
  def parseLine(line: String): (String,Int,Int) = {
    
    val fields = line.split(',')
    val firstName = fields(1)
    val age = fields(2).toInt
    val numFriends = fields(3).toInt
    val result = (firstName,age,numFriends)
    return result
  }

  def main(args: Array[String]){
    System.setProperty("hadoop.home.dir", "C:\\winutils\\hadoop-3.2.1")
    // Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]","average age")
    val sourcefile = sc.textFile("fakefriends.csv")
    val formated = sourcefile.map(parseLine).map(x => (1,(x._2, 1))).reduceByKey( (x,y) => (x._1 + y._1, x._2 + y._2)).map(x=>x._2._1/x._2._2).collect.foreach(println)
  // .map(x => (1,x._, 1)).reduceByKey( (x,y) => (x._1 + y._1, x._2 + y._2))
    // val average = formated._1 / formated._2
    // average.collect.foreach(println)
  }
}
