package Tables
import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by davenpcm on 5/11/16.
  */
case class GOREMAL_R(
                    pidm: Int,
                    emal_code: String,
                    email: String
                    )



/**
  * Created by davenpcm on 4/15/2016.
  */
class GOREMAL(tag: Tag) extends Table[GOREMAL_R](tag, "GOREMAL") {
  def pidm = column[Int]("GOREMAL_PIDM")
  def emal_code = column[String]("GOREMAL_EMAL_CODE")
  def email = column[String]("GOREMAL_EMAIL_ADDRESS")
  def * = (pidm, emal_code, email) <> (GOREMAL_R.tupled, GOREMAL_R.unapply)
}
