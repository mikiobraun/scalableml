package scalableml

import org.apache.spark.SparkContext
import org.scalatest._

/**
 * Created by mibraun on 26/10/15.
 */
class LeastSquaresRegressionTest extends FunSuite {
  test("Simple run of least-squares regression") {
    val sc = new SparkContext("local", "LeastSquaresRegressionTest")

    val dataset = new LinearExampleDataset(100, 3, 0.1)
    val lds = sc.parallelize(dataset.labeledPoints)

    val lsr = new LeastSquaresRegression()

    val weights = lsr.fit(lds)

    println("Real weights = " + dataset.weights.toSeq)
    println("Fitted weights = " + weights)

    sc.stop()
  }
}
