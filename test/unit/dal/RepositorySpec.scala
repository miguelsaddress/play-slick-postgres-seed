import play.api.test.Helpers._
import play.api.test.{WithApplication, PlaySpecification}
import play.api.db.evolutions.Evolutions
import play.api.db.DBApi
import play.api.inject.guice.GuiceApplicationBuilder


import org.specs2.specification.BeforeAfterAll
import org.specs2.concurrent.ExecutionEnv


import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

/**
 * @see example https://github.com/knoldus/activator-play-slick-app/blob/master/src/main/g8/test/repo/EmployeeRepositorySpec.scala
 */
class RepositorySpec() extends PlaySpecification with BeforeAfterAll {

  def await[T](v: Future[T]): T = Await.result(v, Duration.Inf)

  lazy val appBuilder = new GuiceApplicationBuilder()
  lazy val injector = appBuilder.injector()
  lazy val databaseApi = injector.instanceOf[DBApi]

  override def beforeAll() = {
    println("Running all the evolutions of default DB")
    Evolutions.applyEvolutions(databaseApi.database("default"))
  }

  override def afterAll() = {
    println("Cleaning all the evolutions of default DB")
    Evolutions.cleanupEvolutions(databaseApi.database("default"))
  }
}