import org.apache.spark.mllib.linalg.Vectors
val x = Vectors.dense(1.0, 2.0, 3.0)
import breeze.linalg.DenseVector
val x = DenseVector(1.0, 2.0, 3.0, 4.0, 5.0)
x(0)
x(1 to 3)
val y = DenseVector(6.0, 7.0, 8.0, 9.0, 10.0)
x + y
x - y
x :* y
x * y
x + 1.0
x :+ 1.0
x * 1.0
x * 2.0
val x = sc.parallelize((1 to 1000).map(i => DenseVector.rand(10)))
val sum = x.reduce(_ + _)
val n = x.count()
val mean = sum :/ n.toDouble
x.map(v => v - mean).first()

import org.apache.spark.rdd.RDD
def mean(xs: RDD[DenseVector[Double]]): DenseVector[Double] = { val sum = xs.reduce(_ + _); sum / xs.count().toDouble }
mean(x)
def center(xs: RDD[DenseVector[Double]]): RDD[DenseVector[Double]] = { val m = mean(xs); xs.map(x => x - m) }
center(x)
center(x).first()
mean(center(x))

# how to set up intellij!