import java.util.UUID

import TodoRepository.TodoNotFound

import scala.concurrent.{ExecutionContext, Future}

trait TodoRepository {

  def all(): Future[Seq[Todo]]
  def done(): Future[Seq[Todo]]
  def pending(): Future[Seq[Todo]]
  def save(createTodo: CreateTodo): Future[Todo]
  def update(id: String, updateTodo: UpdateTodo): Future[Todo]

}

object TodoRepository {

  final case class TodoNotFound(id: String) extends Exception(s"Todo with id ${id} not found.")
}

//Simulation of the repository for the sake of simplicity
class InMemoryTodoRepository(initialTodos: Seq[Todo] = Seq.empty)(implicit ec: ExecutionContext)
extends TodoRepository {

  private var todos: Vector[Todo] = initialTodos.toVector

  //Again just returning Future.successful to avoid doing real work and keep it simple
  override def all(): Future[Seq[Todo]] = Future.successful(todos)

  override def done(): Future[Seq[Todo]] = Future.successful(todos.filter(_.done))

  override def pending(): Future[Seq[Todo]] = Future.successful(todos.filterNot(_.done))

  override def save(createTodo: CreateTodo): Future[Todo] = Future.successful{
    val todo = Todo(
      UUID.randomUUID().toString,
      createTodo.title,
      createTodo.description,
      false
    )
    todos = todos :+ todo
    todo
  }

  override def update(id: String, updateTodo: UpdateTodo): Future[Todo] = {
    todos.find(_.id == id) match {
      case Some(todoFound) =>
        val newTodo: Todo = updateHelper(todoFound, updateTodo)
        todos = todos.map(t => if(t.id == id) newTodo else t)
        Future.successful(newTodo)
      case None =>
        Future.failed(TodoNotFound(id))
    }
  }

  private def updateHelper(oldTodo: Todo, newTodo: UpdateTodo): Todo ={
    val updatedTodo = oldTodo.copy(
      title = newTodo.title.getOrElse(oldTodo.title),
      description = newTodo.description.getOrElse(oldTodo.description),
      done = newTodo.done.getOrElse(oldTodo.done)
    )
    updatedTodo
  }
}