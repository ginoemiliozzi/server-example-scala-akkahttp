import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class ApiError private (statusCode: StatusCode, message: String)

object ApiError {

  private def apply(statusCode: StatusCode, message: String): ApiError =
    new ApiError(statusCode, message)

  val generic: ApiError =
    new ApiError(StatusCodes.InternalServerError, "Unknown error.")

  val emptyTitle: ApiError =
    new ApiError(StatusCodes.BadRequest, "Title must not be empty.")

  def updateTodoNotFound(id: String): ApiError =
    new ApiError(StatusCodes.NotFound, s"Todo ID ${id} does not exist.")
}
