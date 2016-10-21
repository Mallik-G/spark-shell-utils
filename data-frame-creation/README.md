#Data Frame Creation Util

###Prepare your data

For this utility to work your csv file should contain headers, first row, with the following format:

    <name>:<type>
    example 1:
    columnName:String
    example 2:
    columnName:Int
    example 3:
    columnName:Double
    example 4:
    columnName:Float
    
Given that, headers should look like:

    Name:String,Age:Int,Score:Double
    Ricardo,31,84.5
    Jesica,30,99.9
    Seymour,6,0.0

###Run DataFrameCreationUtils

Run application mode with spark-submit and get your csv file saved as parquet either locally or in cluster (HDFS).

        > spark-submit --class "org.sparkshell.utils.DataFrameCreationUtils" \
            target/scala-2.10/data-frame-creation_2.10-1.0.jar \
            <input folder HDFS or local> \
            <output folder HDFS or local>
    
    
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
        myDF: org.apache.spark.sql.DataFrame = [col1: int, col2: int, col3: string]
        
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
