import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object Main extends App {

  //Initialize server config and Akka supplies
  implicit val system: ActorSystem = ActorSystem("todoapi")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val host = "0.0.0.0"
  val port = 9000

  val mockData = Seq(
    Todo("1", "Learn Akka", "Learn about Akka http", false),
    Todo("2", "Learn Scala", "Learn about Scala language", true),
    Todo("3", "Go to Newells stadium", "Encourage the red and black", true),
    Todo("4", "Buy burgers", "Ran out of burgers", false),
    Todo("5", "Make a pizza", "With a lot of cheese", false),
    Todo("6", "Go for a pint", "Has to be IPA", true),
    Todo("7", "Watch a movie", "Choose a thriller from Netflix", true)
  )
  val todoRepository = new InMemoryTodoRepository(mockData)
  val router = new TodoRouter(todoRepository)
  val server = new Server(router, host, port)

  val bindingFuture = server.bind()
  bindingFuture.onComplete {
    case Success(serverBinding) =>
      println(s"We are online on ${serverBinding.localAddress}!")
    case Failure(e) => println(s"error: ${e.getMessage}")
  }

  Await.result(bindingFuture, 3.seconds)

}
