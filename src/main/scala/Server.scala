import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Server extends App {

  //Initialize server config and Akka supplies
  implicit val system: ActorSystem = ActorSystem("helloworld")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val host = "0.0.0.0"
  val port = 9000

  def route: Route =
    pathSingleSlash {
      get {
        complete("API example")
      }
    } ~ path("hello") {
      get {
        complete("Hello world!")
      }
    } ~ path("scala") {
      get {
        complete("Scala!")
      }
    }

  val bindingFuture = Http().bindAndHandle(route, host, port)
  bindingFuture.onComplete{
    case Success(serverBinding) => println(s"We are online on ${serverBinding.localAddress}!")
    case Failure(e) => println(s"error: ${e.getMessage}")
  }

}
