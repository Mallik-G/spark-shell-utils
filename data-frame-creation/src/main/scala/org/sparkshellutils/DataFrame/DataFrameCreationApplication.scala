package org.sparkshellutils.DataFrame

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object DataFrameCreationApplication {

  def main(args: Array[String]) {
    val sparkConf: SparkConf = new SparkConf().setAppName("Data Frame Creation Util")
    val sparkContext: SparkContext = new SparkContext(sparkConf)
    val sQLContext: SQLContext = new SQLContext(sparkContext)

    if (args.length < 2) println("I need at least a HDSF path to read the data from. If you are running locally it can" +
      "be a local path.") else
    if (args.length == 2) {
      val input = args(0)
      val output = args(1)
      DataFrameCreation.saveDataFrame(sparkContext, sQLContext, input, output)
    } else if(args.length == 3 && args(2) == "hive") {
      val input = args(0)
      val tableName = args(1)
      DataFrameCreation.saveAsHiveTable(sparkContext, sQLContext, input, tableName)
    }
    else println("I don't understand what you are doing.")
  }

}