package Tables


import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by davenpcm on 4/12/2016.
  */
case class Sorlcur_r(
                    pidm: Int,
                    seqNo: Int,
                    cactCode: String
                    )

class SORLCUR (tag: Tag) extends Table[Sorlcur_r](tag, "SORLCUR") {
  def PIDM = column[Int]("SORLCUR_PIDM")
  def SEQNO = column[Int]("SORLCUR_SEQNO")
  def CACT_CODE = column[String]("SORLCUR_CACT_CODE")
  def LmodCode = column[String]("SORLCURL_LMOD_CODE")
  def * = (PIDM, SEQNO, CACT_CODE) <> (Sorlcur_r.tupled, Sorlcur_r.unapply)
}