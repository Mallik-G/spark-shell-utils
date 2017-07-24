package org.apache.spark.mllib.clustering

import org.apache.spark.mllib.linalg.Vectors

/**
  * mllib.clustering implicits
  */
package object implicits {

  /**
    * Extra methods for DistributedLDAModel
    *
    * @param distributedLDAModel Distributed LDA Model
    */
  implicit class DistributedLDAModelExtras(val distributedLDAModel: DistributedLDAModel) {

    /**
      * Convert DistributedLDAModel to LocalLDAModel with new document concentration and topic concentration for
      * better results.
      *
      * @param alpha Document concentration, can be >= 0.
      * @param beta  Document concentration, can be >= 0.
      * @return LocalLDAModel with given alpha and beta
      */
    def toLocal(alpha: Double, beta: Double): LocalLDAModel = {
      new LocalLDAModel(distributedLDAModel.topicsMatrix,
        Vectors.dense(Array.fill(distributedLDAModel.k)(alpha)),
        beta)
    }
  }

}
