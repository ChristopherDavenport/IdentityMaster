package Tables

/**
  * Created by chris on 4/8/16.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class Faculty(
                  Pidm: Int,
                  TermCodeEff: String,
                  FacultyStatus: String,
                  FacultyTag: Option[String],
                  ScheduleInd: Option[String],
                  AdvisorInd: Option[String]
                  )

class SIBINST(tag: Tag) extends Table[Faculty](tag, "SIBINST"){
  def SIBINST_PIDM = column[Int]("SIBINST_PIDM")
  def SIBINST_TERM_CODE_EFF = column[String]("SIBINST_TERM_CODE_EFF")
  def SIBINST_FCST_CODE = column[String]("SIBINST_FCST_CODE")
  def SIBINST_FCTG_CODE = column[Option[String]]("SIBINST_FCTG_CODE")
  def SIBINST_SCHD_IND = column[Option[String]]("SIBINST_SCHD_IND")
  def SIBINST_ADVR_IND = column[Option[String]]("SIBINST_ADVR_IND")

  def * = (SIBINST_PIDM, SIBINST_TERM_CODE_EFF, SIBINST_FCST_CODE, SIBINST_FCTG_CODE, SIBINST_SCHD_IND, SIBINST_ADVR_IND) <>
    (Faculty.tupled, Faculty.unapply)

}