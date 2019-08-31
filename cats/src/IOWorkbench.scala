import cats.data.State

object IOWorkbench extends App {


  sealed trait A
  case class A1() extends A
  case class A2() extends A

  def a(a: A): A = {
    A1()
  }


  State[A, Int]{
    case x: A1 => (a(x), 12)
    case x: A2 => (a(x), 22)
  }

}
