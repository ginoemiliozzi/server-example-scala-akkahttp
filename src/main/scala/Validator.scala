trait Validator[T]{
  def validate(t: T): Option[ApiError]
}

object CreateTodoValidator extends Validator[CreateTodo] {

  override def validate(createTodo: CreateTodo): Option[ApiError] = {
    if(createTodo.title.isEmpty) Some(ApiError.emptyTitle)
    else None
  }
}

object UpdateTodoValidator extends Validator[UpdateTodo]{

  override def validate(t: UpdateTodo): Option[ApiError] = {
    t.title match {
      case Some(updtitle) if updtitle.isEmpty => Some(ApiError.emptyTitle)
      case None => None
    }
  }
}
