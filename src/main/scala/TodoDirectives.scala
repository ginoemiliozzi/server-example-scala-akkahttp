import akka.http.scaladsl.server.{Directive1, Directives}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait TodoDirectives extends Directives {

  def handle[T](f: Future[T])(throwable: Throwable => ApiError): Directive1[T] =
    onComplete(f) flatMap {
      case Success(t) =>
        provide(t)
      case Failure(e) =>
        val apiError = throwable(e)
        complete(apiError.statusCode, apiError.message)
    }

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handle(f)(_ => ApiError.generic)
}
