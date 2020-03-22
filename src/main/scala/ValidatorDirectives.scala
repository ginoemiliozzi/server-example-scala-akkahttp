import akka.http.scaladsl.server.{Directive0, Directives}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

trait ValidatorDirectives extends Directives {

  def validateWith[T](t: T)(implicit validator: Validator[T]): Directive0 =
    validator.validate(t) match {
      case Some(apiError) =>
        complete(apiError.statusCode, apiError.message)
      case None =>
        pass
    }
}
