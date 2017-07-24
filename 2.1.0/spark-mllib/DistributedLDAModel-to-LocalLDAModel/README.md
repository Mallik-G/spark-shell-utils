# Distributed LDA Model Extras

## Motivation

This project was created with the intention of extending current MLlib DistributedLDAModel and offer users a new method to
convert into LocalLDAModel that allows updating document concentration and topic concentration.

Current default method .toLocal doesn't take any parameters. When running a very simple test with two documents, one document 
just one word "dog" and another document with just one word "cat", training the model using EM and then converting to LocalLDAModel
will limit the performance of LocalLDAModel as EM hyperparameters are bigger than 1. 

With this projects, users will be able to convert from DistributedLDAModel to LocalLDAModel and reset alpha and beta, that way 
those values can be as small as needed, even less than 1. 

## Building DistributedLDAModel to LocalLDAModel
    > cd 2.1.0/spark-mllib/DistributedLDAModel-to-LocalLDAModel
    > sbt package
   
## Using DistributedLDAModel to LocalLDAModel
### Include in your spark-shell
1. After building, take the .jar file to some more useful location:
        
        > cd 2.1.0/spark-mllib/DistributedLDAModel-to-LocalLDAModel      
        > cp target/scala-2.11/distributedldamodel-to-localldamodel_2.11-1.0.jar ~/myjars/.
        > cd ~/myjars
      
2. Load the .jar in your spark-shell session and import org.apache.spark.mllib.clustering.implicits
        
        > spark-shell --jars distributedldamodel-to-localldamodel_2.11-1.0.jar
        Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
        Setting default log level to "WARN".
        To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
        17/07/24 10:41:57 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
        17/07/24 10:42:13 WARN ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
        Spark context Web UI available at http://10.219.16.149:4040
        Spark context available as 'sc' (master = local[*], app id = local-1500910921334).
        Spark session available as 'spark'.
        Welcome to
              ____              __
             / __/__  ___ _____/ /__
            _\ \/ _ \/ _ `/ __/  '_/
           /___/ .__/\_,_/_/ /_/\_\   version 2.1.1
              /_/

        Using Scala version 2.11.8 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_91)
        Type in expressions to have them evaluated.
        Type :help for more information.

        scala> import org.apache.spark.mllib.clustering.implicits._
        import org.apache.spark.mllib.clustering.implicits._
        
 3. Supossing there is an instance of DistributedLDAModel:
      
        scala> val localModel = distributedModel.toLocal(0.0009, 0.00001)
        localModel: org.apache.spark.mllib.clustering.LocalLDAModel = org.apache.spark.mllib.clustering.LocalLDAModel@54ba4f78

