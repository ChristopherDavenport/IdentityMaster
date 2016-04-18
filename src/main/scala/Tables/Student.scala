package Tables

/**
  * Created by chris on 4/8/16.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class Student (
                   Pidm: Int,
                   TermCodeEff: String,
                   StudentStatus: String,
                   LevelCode: String
                   )

class SGBSTDN(tag: Tag) extends Table[Student](tag, "SGBSTDN"){
  def SGBSTDN_PIDM = column[Int]("SGBSTDN_PIDM")
  def SGBSTDN_TERM_CODE_EFF = column[String]("SGBSTDN_TERM_CODE_EFF")
  def SGBSTDN_STST_CODE = column[String]("SGBSTDN_STST_CODE")
  def SGBSTDN_LEVL_CODE = column[String]("SGBSTDN_LEVL_CODE")

  def * = (SGBSTDN_PIDM, SGBSTDN_TERM_CODE_EFF, SGBSTDN_STST_CODE, SGBSTDN_LEVL_CODE) <> (Student.tupled, Student.unapply)
}