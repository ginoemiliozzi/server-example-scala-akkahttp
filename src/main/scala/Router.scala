import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.util.{Failure, Success}

trait Router {

  def route: Route
}

class TodoRouter(todoRepository: TodoRepository)
    extends Router
    with TodoDirectives
    with ValidatorDirectives {

  override def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        handleWithGeneric(todoRepository.all()) { todos =>
          complete(todos)
        }
      } ~ post {
        entity(as[CreateTodo]) { createTodo =>
          validateWith(createTodo)(CreateTodoValidator) {
            handleWithGeneric(todoRepository.save(createTodo)) { todo =>
              complete(todo)
            }
          }
        }
      } ~ path(Segment) { id: String =>
        put {
          entity(as[UpdateTodo]) { updateTodo =>
            validateWith(updateTodo)(UpdateTodoValidator) {
              handle(todoRepository.update(id, updateTodo)) {
                case TodoRepository.TodoNotFound(_) =>
                  ApiError.updateTodoNotFound(id)
                case _ =>
                  ApiError.generic
              } { todo =>
                complete(todo)
              }
            }
          }
        }
      }
    } ~ path("done") {
      get {
        handleWithGeneric(todoRepository.done()) { todos =>
          complete(todos)
        }
      }
    } ~ path("pending") {
      get {
        handleWithGeneric(todoRepository.pending()) { todos =>
          complete(todos)
        }
      }
    }
  }
}
