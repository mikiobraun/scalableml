3 * 4
val x = 1
var y = 1
y = 2
x = 2
val z: Double = 3
val l = Seq(1,2,3,4,5,6,7,8)
Array(1,2,3,4)
l(3)
l(0)
l(1)
l(2)
l
l.map(x => 2*x)
l.map(2 * _)

l
sc
val ls = sc.parallelize(l)
ls.map(_ * 2)
ls.map(_ * 2).collect()
ls.map(_ * 2).take(3)
ls.count()
ls.reduce(_ + _)
val ms = sc.parallelize(Seq(2, 4, 6))
ms.map(m => ls.map(_ + m)).collect()
ls
val cls = ls.collect()
ms.map(m => cls.map(_ + m)).collect()

val s = "hello world!"
val cs = sc.parallelize(s.split(""))
cs.collect()
cs.map(c => (c, 1))
cs.map(c => (c, 1)).collect()
cs.map(c => (c, 1)).reduceByKey(_ + _)
cs.map(c => (c, 1)).reduceByKey(_ + _).collect()



