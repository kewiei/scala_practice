import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object TwoSum{
    def main(args: Array[String]){
        Logger.getLogger("org").setLevel(Level.ERROR)

        val sc = new SparkContext("local[*]","TwoSum")
        val datasource = sc.textFile("data.csv")
        val targe_value = 500
        val formated = datasource.map(x=>x.toInt).map(x=>(x,0))
        val subtracted = formated.map(x=>(targe_value-x._1,1))
        val combined = formated.union(subtracted)
        // val reduced = combined.reduceByKey((x,y)=>(true if))
        val reduced = combined.groupByKey().mapValues(x=>Set()++x).mapValues(x=>if (x.size==2) true else false)
        val possible = reduced.filter(x=>x._2).map(x=>x._1)
        val pairs = possible.map(x=>if(x>(targe_value/2)) (targe_value-x,x) else (x,targe_value-x) )
        val reduce_pairs = pairs.reduceByKey((x,y)=>x)
        println("<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>")
        println("result starts:")
        reduce_pairs.collect.sorted.foreach(println)
    }
}