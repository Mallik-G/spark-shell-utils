package org.sparkshellutils.DataFrame

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SaveMode}
import org.apache.spark.sql.hive.HiveContext

/**
  * Contains routines to either create a new data frame upload data in parquet format to HDFS or register as temporal table.
  */
object DataFrameCreation {

  private val STRING_TYPE = Some("String")
  private val INT_TYPE = Some("Int")
  private val DOUBLE_TYPE = Some("Double")
  private val FLOAT_TYPE = Some("Float")

  /**
    * Saves a data frame to HDFS or local folder. If running in a cluster will save to HDFS if running in local will save
    * in local file system.
    *
    * @param sparkContext application spark context
    * @param sQLContext application sql context
    * @param input input path. If running in cluster should be a HDFS folder else a local path
    * @param output output path. If running in cluster should be a HDFS folder else a local path
    */
  def saveDataFrame(sparkContext: SparkContext,
                    sQLContext: SQLContext,
                    input: String,
                    output: String): Unit ={
    val dataFrame = createDataFrame(sparkContext, sQLContext, input)
    dataFrame.write.mode(SaveMode.Overwrite).parquet(output)
  }

  /**
    * Returns a data frame. This methods is used when running spark shell. Import DataFrameCreation and call getDataFrame
    * to generate a data frame and start analyzing data.
    *
    * @param sparkContext application spark context
    * @param sQLContext application sql context
    * @param input input path. If running in cluster should be a HDFS folder else a local path
    * @return new data frame based on input data
    */
  def getDataFrame(sparkContext: SparkContext,
                      sQLContext: SQLContext,
                      input: String): DataFrame ={
    createDataFrame(sparkContext, sQLContext, input)
  }

  /**
    * Registers a temp table with a given name containing the data on input file. This methods is used when running
    * spark shell. Import DataFrameCreation and call registerAsTempTable to get a temp table.
    * @param sparkContext application spark context
    * @param sQLContext application sql context
    * @param input input path. If running in cluster should be a HDFS folder else a local path
    * @param tableName temporal table name
    */
  def registerAsTempTable(sparkContext: SparkContext,
                          sQLContext: SQLContext,
                          input: String,
                          tableName: String): Unit ={
    val dataFrame = createDataFrame(sparkContext, sQLContext, input)

    dataFrame.registerTempTable(tableName)
  }

  /**
    * Creates a new table in default database in Hive.
    * @param sparkContext application spark context
    * @param sQLContext application sql context
    * @param input input path. If running in cluster should be a HDFS folder else a local path
    * @param tableName hive table name
    */
  def saveAsHiveTable(sparkContext: SparkContext,
                      sQLContext: SQLContext,
                      input: String,
                      tableName: String): Unit ={

    val hiveContext = new HiveContext(sparkContext)

    val dataFrame = createDataFrame(sparkContext, sQLContext, input)
    val hiveDataFrame = hiveContext.createDataFrame(dataFrame.rdd, dataFrame.schema)
    hiveDataFrame.write.mode(SaveMode.Overwrite).saveAsTable(tableName)

  }

  private def createDataFrame(sparkContext: SparkContext, sQLContext: SQLContext, input: String): DataFrame ={

    val data: RDD[Array[String]] = sparkContext.textFile(input).map(_.split(","))
    val header: Array[String] = data.first()

    val dataWithoutHeader: RDD[Array[String]] = data.filter(row => !(row sameElements header))

    val columnIndexType: Map[Int, String] = header
      .map(_.split(":"))
      .map(tuple => tuple(1))
      .zipWithIndex
      .map(pair => (pair._2,pair._1)).toMap

    val dataColumnCasted: RDD[Array[Any]] = dataWithoutHeader.map(castField(_, columnIndexType))

    val rddRow: RDD[Row] = dataColumnCasted.map(row => Row.fromSeq(row))
    val schema: StructType = StructType(header.map(getStructField))

    return sQLContext.createDataFrame(rddRow, schema)
  }

  private def castField(row: Array[String], columnIndexType: Map[Int, String]): Array[Any] ={

    return for ((column, i) <- row.zipWithIndex) yield {
      val columnType: Option[String] = columnIndexType.get(i)
      columnType match {
        case STRING_TYPE=> column
        case INT_TYPE => column.toInt
        case DOUBLE_TYPE => column.toDouble
        case FLOAT_TYPE => column.toFloat
        case _ => column
      }
    }
  }

  private def getStructField(column: String): StructField = {
    val columnDescription = column.split(":")

    if (columnDescription.length > 1) {
      val columnName = columnDescription(0)
      val columnType = columnDescription(1)
      columnType match {
        case "String" => return StructField(columnName, StringType)
        case "Int" => return StructField(columnName, IntegerType)
        case "Double" => return StructField(columnName, DoubleType)
        case "Float" => return StructField(columnName, FloatType)
      }
    }
    else return StructField(columnDescription(0), StringType)
  }

}
