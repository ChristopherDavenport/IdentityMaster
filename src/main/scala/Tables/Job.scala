package Tables

import java.sql.Date
import com.typesafe.slick.driver.oracle.OracleDriver.api._

/**
  * Created by davenpcm on 4/7/2016.
  */
case class Job(Pidm : Int,
               Posn: String,
               EffDate: Date,
               OrgCode: String)


class NBRJOBS (tag: Tag) extends Table[Job](tag, "NBRJOBS") {
  def NBRJOBS_PIDM = column[Int]("NBRJOBS_PIDM")
  def NBRJOBS_POSN = column[String]("NBRJOBS_POSN")
  def NBRJOBS_EFFECTIVE_DATE = column[Date]("NBRJOBS_EFFECTIVE_DATE")
  def NBRJOBS_ORGN_CODE_TS = column[String]("NBRJOBS_ORGN_CODE_TS")
  def * = (NBRJOBS_PIDM, NBRJOBS_POSN, NBRJOBS_EFFECTIVE_DATE, NBRJOBS_ORGN_CODE_TS) <> (Job.tupled, Job.unapply)
}