import dispatch.classic.Http
import org.specs2._
import unfiltered.response.{Ok, ResponseString}
import unfiltered.specs2.jetty.Planned

class Spike extends Specification with Planned {

  def is = s2"""
     Everything should return AOK             $aok
  """


  def intent[A, B]: unfiltered.Cycle.Intent[A, B] = {
    case _ => Ok ~> ResponseString("AOK")
  }


  val h = new Http

  def aok = {
    h(host as_str) must_== "foo is bar"
  }

}
