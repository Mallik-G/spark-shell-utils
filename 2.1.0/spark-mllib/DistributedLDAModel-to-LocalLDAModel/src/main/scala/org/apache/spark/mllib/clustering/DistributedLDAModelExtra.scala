package org.apache.spark.mllib.clustering

import org.apache.spark.mllib.linalg.Vectors

package object implicits {

  implicit class DistributedLDAModelExtras(val distributedLDAModel: DistributedLDAModel) {
    def toLocal(alpha: Double, beta: Double): LocalLDAModel = {
      new LocalLDAModel(distributedLDAModel.topicsMatrix,
        Vectors.dense(Array.fill(distributedLDAModel.k)(alpha)),
        beta)
    }
  }

}
