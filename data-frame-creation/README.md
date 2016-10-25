#Data Frame Creation Utility

## Motivation

This project has been created with the intention of help people exploring CSV files with Spark using Scala code.

Users can now save time creating data frames based on their CSV files and go straight to execute SQL queries, analyze
data, aggregation, etc. 

If users are planning to use the same data frame for different sessions they can now save CSV file as parquet in a 
given location and don't have to re-create a data frame every time. 

## Using Data Frame Creation Utility

###Prepare your CSV file

Open your CSV file and append the desired data type for each column name. Make sure the values are compatible and can be 
casted to the given data type. 
Each column should contain name, colon and data type as shown below:

    <name>:<type>
    example 1:
    columnName:String
    example 2:
    columnName:Int
    example 3:
    columnName:Double
    example 4:
    columnName:Float
    
This is an example of how your CSV file should look like:

    Name:String,Age:Int,Score:Double
    Ricardo,31,84.5
    Jesica,30,99.9
    Seymour,6,0.0
    
For now the supported data types are String, Int, Double and Float.

###Run DataFrameCreationApplication

Run application mode with spark-submit and get your CSV file saved as parquet either locally or in cluster (HDFS).

        > spark-submit --class "org.sparkshell.utils.DataFrameCreationUtils" \
            target/scala-2.10/data-frame-creation_2.10-1.0.jar \
            <input folder HDFS or local> \
            <output folder HDFS or local>
    
Add a third parameter "hive" to save your CSV content as Hive table.
                
        > spark-submit --class "org.sparkshell.utils.DataFrameCreationUtils" \
            target/scala-2.10/data-frame-creation_2.10-1.0.jar \
            <input folder HDFS or local> \
            <table name> \
            hive

    
###Run DataFrameCreation

Run spark-shell, import DataFrameCreation and invoke `getDataFrame` or `registerAsTempTable`

        > spark-shell --jars target/scala-2.10/data-frame-creation_2.10-1.0.jar
        log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
        log4j:WARN Please initialize the log4j system properly.
        log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
        Using Spark's repl log4j profile: org/apache/spark/log4j-defaults-repl.properties
        To adjust logging level use sc.setLogLevel("INFO")
        Welcome to
              ____              __
             / __/__  ___ _____/ /__
            _\ \/ _ \/ _ `/ __/  '_/
           /___/ .__/\_,_/_/ /_/\_\   version 1.6.2
              /_/
        
        Using Scala version 2.10.5 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_91)
        Type in expressions to have them evaluated.
        Type :help for more information.
        Spark context available as sc.
        SQL context available as sqlContext.
        
        scala> import org.sparkshell.utils.{DataFrameCreation => dfc}
        import org.sparkshell.utils.{DataFrameCreation=>dfc}
        
        scala> val myDF = dfc.getDataFrame(sc, sqlContext, "test.csv")
        myDF: org.apache.spark.sql.DataFrame = [Name: string, Age: int, Score: double]
        
        scala> myDF.show
        +----------+----+-----+
        |      Name|Age |Score|
        +----------+----+-----+
        |   Ricardo|  31| 84.5|
        |    Jesica|  30| 99.9|
        |SomePerson|   6|  0.0|
        +----------+----+-----+
        
        
        scala> val myDF = dfc.registerAsTempTable(sc, sqlContext, "test.csv", "personas")
        myDF: Unit = ()
        
        
        scala> sqlContext.sql("SELECT * FROM personas").show
        +----------+----+-----+
        |      Name|Age |Score|
        +----------+----+-----+
        |   Ricardo|  31| 84.5|
        |    Jesica|  30| 99.9|
        |SomePerson|   6|  0.0|
        +----------+----+-----+
