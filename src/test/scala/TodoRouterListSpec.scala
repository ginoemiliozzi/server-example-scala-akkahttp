import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class TodoRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMocks {

  private val doneTodo = Todo("1", "Do something", "Do it!", true)
  private val pendingTodo = Todo("1", "Do another thing", "Do it!", false)

  private val todos = Seq(doneTodo,pendingTodo)

  "TodoRouter" should {

    "return all todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(repository)

      Get("/todos") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe todos
      }
    }

    "return pending todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(repository)

      Get("/todos/pending") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe Seq(pendingTodo)
      }
    }

    "return done todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(repository)

      Get("/todos/done") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe Seq(doneTodo)
      }
    }

    "handle repository failure" in {
      val repository = new FailingRepository
      val router = new TodoRouter(repository)

      Get("/todos") ~> router.route ~> check {
        status shouldBe ApiError.generic.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.generic.message
      }
    }
  }


}
