package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.util.{ Try, Success, Failure }
import models.User
import scala.concurrent.{ Future, ExecutionContext }
import org.postgresql.util.PSQLException

/**
 * A repository for users.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._
  import UserRepository.Failures._

  /**
   * Here we define the table. It will have a name of users
   */
  private class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")
    def username = column[String]("username")
    def email = column[String]("email")
    def password = column[String]("password")


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the User object.
     *
     * In this case, we are simply passing the User parameters to the User case classes
     * apply and unapply methods.
     */
    def * = (name, username, email, password, id) <> ((User.apply _).tupled, User.unapply)
  }

  /**
   * The starting point for all queries on the users table.
   */
  private val users = TableQuery[UsersTable]

  /**
   * adds a User to the collection of users
   * 
   * http://slick.lightbend.com/doc/3.1.0/queries.html#inserting @see returning into
   * 
   * 
   * org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "users_username_key"
  Detail: Key (username)=(mamoreno) already exists.
   */
  def add(user:User): Future[Either[UserRepositoryFailure, User]] = db.run {
    val insertReturningUserWithIdQuery = 
      users returning users.map(_.id) into ((user,id) => user.copy(id=id))
      (insertReturningUserWithIdQuery += user).asTry 
  } map { res => 
    res match {
      case Success(user) => Right(user)
      case Failure(e: PSQLException) => {
        val msg = e.getMessage()
        msg match {
          case _ if msg.contains("users_username_key") => Left(UsernameTaken)
          case _ if msg.contains("users_email_key") => Left(EmailTaken)
          case _ => Left(DBFailure)
        }
      }
      case Failure(_) => Left(DBFailure)
    }
  }      

  /**
   * List all the users in the database.
   */
  def list(): Future[Seq[User]] = db.run {
    users.result
  }

}

object UserRepository {
  object Failures {
    sealed trait UserRepositoryFailure
    object DBFailure extends UserRepositoryFailure
    object UsernameTaken extends UserRepositoryFailure  
    object EmailTaken extends UserRepositoryFailure
  }
}
