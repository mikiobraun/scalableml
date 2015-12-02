package scalableml

import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import breeze.linalg.{DenseVector => BDV}
import breeze.linalg.{DenseMatrix => BDM}

/**
 * Created by mibraun on 26/10/15.
 */
class LeastSquaresRegression {
  def fit(dataset: RDD[LabeledPoint]): DenseVector = {
    val features = dataset.map(_.features)

    val covarianceMatrix: BDM[Double] = features.map { v =>
      val x = BDM(v.toArray)
      x.t * x
    }.reduce(_ + _)

    val featuresTimesLabels: BDV[Double] = dataset.map { xy =>
      BDV(xy.features.toArray) * xy.label
    }.reduce(_ + _)

    val weight = covarianceMatrix \ featuresTimesLabels

    new DenseVector(weight.data)
  }
}
