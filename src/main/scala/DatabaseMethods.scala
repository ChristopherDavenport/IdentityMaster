import com.typesafe.slick.driver.oracle.OracleDriver
import slick.backend.DatabaseConfig

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}
/**
  * Created by davenpcm on 4/22/2016.
  */
object DatabaseMethods {

  object StateContainer{
    val dbConfig: DatabaseConfig[OracleDriver] = DatabaseConfig.forConfig("slick.dbs.oracle")
    var MaxTerm: Option[String] = None
  }

  def getDatabaseConfig = {
    val dbconfig = StateContainer.dbConfig
    dbconfig
  }

  def getClassCode(pidm: Int, levl: String, term: String): Option[String] = {
    val dbConfig = getDatabaseConfig
    import dbConfig.driver.api._
    val dboperation = sql"""SELECT sgkclas.F_CLASS_CODE(${pidm}, ${levl} ,${term}) FROM dual""".as[String]
    val myTry = Try(Await.result(dbConfig.db.run(dboperation), Duration(2, "seconds")).headOption)
    myTry match {
      case Success(opt) => opt
      case _ => None
    }
  }



  def getMaxTerm(): Option[String] = {
    StateContainer.MaxTerm match{
      case Some(term) => StateContainer.MaxTerm
      case None =>
        println("In Database Loop")
        val dbConfig = getDatabaseConfig
        import dbConfig.driver.api._
        val db = dbConfig.db

        val dboperation = sql"""
                        SELECT STVTERM_CODE as CURRENT_PEL_TERM
                         FROM STVTERM b
                         INNER JOIN (
                             SELECT Max(STVTERM_START_DATE) maxTerm
                             FROM STVTERM
                             WHERE STVTERM_START_DATE <= sysdate
                                   AND stvterm_code LIKE '%5'
                             ) a on b.STVTERM_START_DATE = a.maxTerm
      """.as[String]

        val myTry = Try(Await.result(db.run(dboperation), Duration(2, "seconds")).headOption)
        myTry match {
          case Success(opt) =>
            StateContainer.MaxTerm = opt
            opt
          case _ => None
        }
    }

  }

}
