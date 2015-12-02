# url for libsvm
www.csie.ntu.edu.tw/~cjlin/libsvmtools/datasets/binary.html

# was run with 1.6.0-snapshot, check again 1.5.1!!!
import org.apache.spark.mllib.util.MLUtils
val data = MLUtils.loadLibSVMFile(sc, "rcv1_train.binary.bz2")
data.count()
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
val model = new LogisticRegressionWithLBFS().setNumClasses(2).run(data)
data.map(_.label).collect().toSet
import org.apache.spark.mllib.regression.LabeledPoint
val data2 = data.map(p => new LabeledPoint(if (p.label == 1.0) 1.0 else 0.0, p.features))
data2.take(10)
data2.map(_.label).take(10)
val model = new LogisticRegressionWithLBFS().setNumClasses(2).run(data2)
data2.cache()
model.numFeatures
model.predict(data2.first().features)
val labels = data2.map(p => (model.predict(p.features), p.label))
val error = labels.map(yy => if (yy._1 == yy._2) 0.0 else 1.0).reduce(_ + _) / labels.count()
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
val metrics = new BinaryClassificationMetrics(labels)
metrics.areaUnderROC

val trainingTestSplit = data2.randomSplit(Array(0.8, 0.2))
val train = trainingTestSplit(0)
val test = trainingTestSplit(1)
train.count()
test.count()
def logreg(reg: Double) = { val l = new LogisticRegressionWithLBFGS(); l.optimizer.setRegParam(reg); l }
logreg(1.0)

import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.rdd.RDD
def evaluate(model: LogisticRegressionModel, data: RDD[LabeledPoint]): BinaryClassificationMetrics = { val lables = data.map(p => (model.predict(p.features), p.label)); new BinaryClassificationMetrics(labels) }
logreg(1.0).run(train)
train.cache()
logreg(10.0).run(train)
evaluate(logreg(10.0).run(train), test)
evaluate(logreg(10.0).run(train), test).areaUnderROC
for (r <- Seq(0.01, 0.1, 1.0, 10.0, 100.0)) yield evaluate(logreg(r).run(train), test).areaUnderROC

--- spark.ml

import org.apache.spark.ml.classification.LogisticRegression
val lr = new LogisticRegression
lr.explainParams
lr.maxIter

import sqlContext.implicits._
data
val data2 = data.toDF
data2.registerTempTable("data")
val df = sqlContext.sql("SELECT (label+1)/2 AS label, features FROM data")
lr.fit(df)

import org.apache.spark.ml.feature.Normalizer
val norm = new Noramlizer
norm.explainParams
norm.setInputCol("features")
lr.setFeaturesCol(norm.getOutputColumn)
norm.getOutputCol
import org.apache.spark.ml.Pipeline
val p = new Pipeline
p.setStages(Array(norm, lr))
val model = p.fit(df)
val result = model.transform(df)
val eval = new org.apache.spark.ml.evaluation.BinaryClassificationEvalluator
eval.evaluate(result)
eval.explainParams

# fix slide 67 sqlContext.select => sqlContext.sql
