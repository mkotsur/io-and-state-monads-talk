import cats.data.State
import cats.kernel.{Monoid, Semigroup}

object StateTests extends App {

  var operations: List[String] = List.empty
  def add(a: Int, b: Int): Int = {
    operations :+= s"$a+$b"
    a + b
  }

  import cats.implicits._
  1 |+| 2
  "a" |+| "b"

//  implicit val optionStringSemigroup =
//    Semigroup.instance[Option[String]](
//      (a, b) => Some(a.getOrElse("") |+| b.getOrElse(""))
//    )
//
//  Option("a") |+| Option("b")

  val x = add(2, add(3, 3))
  // "operations" contains 1 operation
  // instead of 2

  println(operations)

  type MyStateChange[S, A] = S => (S, A)

  val inc1: MyStateChange[String, Int] = (word) => (s"$word" + "a", word.length)
  val inc2: MyStateChange[String, Int] = (word) => (s"$word" + "a", word.length)

  val (w1, r1) = inc1("")
  println(s"w1: $w1, r1: $r1")

  val (w2, r2) = inc2(w1)
  println(s"w2: $w2, r2: $r2")

  import cats.implicits._

  def inc[S, A: Semigroup](
      i1: MyStateChange[S, A],
      i2: MyStateChange[S, A]
  ): MyStateChange[S, A] = { (w: S) =>
    {
      val (state1, result1) = i1(w)
      val (state2, result2) = i2(state1)
      (state2, result1 |+| result2)
    }
  }

  val inc3     = inc[String, Int](inc1, inc2)
  val (w3, r3) = inc3("b")
  println(s"w3: $w3, r3: $r3")

  val state1 = State(inc1)
  val state2 = State(inc2)

  def glueStates[S, A: Semigroup](
      s1: State[S, A],
      s2: State[S, A]
  ): State[S, A] =
    for {
      v1 <- s1; v2 <- s2
    } yield v1 |+| v2

  val state4 = glueStates(state1, state2)

  val (w4, r4) = state4.run("b").value
  println(s"w4: $w4, r4: $r4")

  val es: State[String, Int] = State.empty[String, Int]
  type MyStringState[R] = State[String, R]

  val state5 = List(state1, state2)
    .foldM[MyStringState, Int](0)((a, s) => s.map(r => r + a))

  val (w5, r5) = state5.run("b").value
  println(s"w5: $w5, r5: $r5")
}
