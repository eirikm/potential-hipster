import dispatch.classic.Http
import org.specs2._
import unfiltered.filter.Plan
import unfiltered.filter.request.ContextPath
import unfiltered.request.Seg
import unfiltered.response.{Ok, ResponseString}
import unfiltered.specs2.jetty.Planned

class Spike extends Specification with Planned with MyPlan { def is = s2"""
     GET /kunde/
        12345/abonnementer
          skal returnere json             $t01
          skal returnere 2 abonnement     $t02
  """


  def h = new Http

  def t01 = h(host / "kunde" / "1" / "abonnementer" as_str) should startWith("[")
  def t02 = h(host / "kunde" / "1" / "abonnementer" as_str) must_== "AOK"
}

trait MyPlan extends TestData {
  def intent[A, B]: unfiltered.Cycle.Intent[A, B] = {
    case ContextPath(_, Seg("kunde" :: kundenr :: "abonnementer" :: Nil)) =>
      val json = abonnement.groupBy {
        case (k, _) => k.id.asInt.toString
      }.get(kundenr).map {
        (l: Vector[(Kunde, Blad)]) =>
          l.map {
            case (_, b) => b.toJson
          }.mkString("[", ", ", "]")
      }.getOrElse("[]")

      Ok ~> ResponseString(json)
  }
}

trait TestData {
  val kunde1 = Kunde(KundeId(1), "Ståle")
  val kunde2 = Kunde(KundeId(2), "Mari")
  val blad1 = Blad(BladId(1), "Jakt og fiske")
  val blad2 = Blad(BladId(2), "HiFi-fil")

  val kunder = Vector(kunde1, kunde2)
  val blader = Vector(blad1, blad2)

  val abonnement = Vector(
    (kunde1, blad1),
    (kunde1, blad2),
    (kunde2, blad1))
}

case class KundeId(asInt: Int)
case class BladId(asInt: Int)
case class Kunde(id: KundeId, navn: String)
case class Blad(id: BladId, navn: String) extends JsonHelper {
  def toJson = Seq(attrToJson("id", id.asInt.toString),
    attrToJson("navn", navn)
  ).mkString("{", ", ", "}")
}
case class Abonnement(kundeId: KundeId, bladId: BladId)

trait JsonHelper {
  def attrToJson(navn: String, verdi: String) = "\"" + navn + "\": \"" + verdi + "\""
}
