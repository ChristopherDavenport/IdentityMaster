package Tables

import java.sql.Date
import com.typesafe.slick.driver.oracle.OracleDriver.api._

/**
  * Created by davenpcm on 4/12/2016.
  */
case class Sorlfos_r(
                 pidm: Int,
                 lcurSeqNo: Int,
                 majorCode: String,
                 lfstCode: String,
                 priorityNo: Int,
                 activityDate: Date
               )


class SORLFOS (tag: Tag) extends Table[Sorlfos_r](tag, "SORLFOS") {
  def PIDM = column[Int]("SORLFOS_PIDM")
  def LCUR_SEQNO = column[Int]("SORLFOS_LCUR_SEQNO")
  def MAJOR_CODE = column[String]("SORLFOS_MAJR_CODE")
  def LFST_CODE = column[String]("SORLFOS_LFST_CODE")
  def PRIORITY_NO = column[Int]("SORLFOS_PRIORITY_NO")
  def ACTIVITY_DATE = column[Date]("SORLFOS_ACTIVITY_DATE")
  def * = (PIDM, LCUR_SEQNO, MAJOR_CODE, LFST_CODE, PRIORITY_NO, ACTIVITY_DATE) <> (Sorlfos_r.tupled, Sorlfos_r.unapply)
}