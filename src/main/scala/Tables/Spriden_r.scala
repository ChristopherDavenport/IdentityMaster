package Tables

import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by davenpcm on 4/12/2016.
  */
case class Spriden_r(
                    pidm: Int,
                    id: String,
                    firstName: String,
                    lastName: String,
                    changeInd: Option[String]
                    )

class SPRIDEN (tag: Tag) extends Table[Spriden_r](tag, "SPRIDEN") {
  def PIDM = column[Int]("SPRIDEN_PIDM")
  def ID = column[String]("SPRIDEN_ID")
  def FIRST_NAME = column[String]("SPRIDEN_FIRST_NAME")
  def LAST_NAME = column[String]("SPRIDEN_LAST_NAME")
  def CHANGE_IND = column[Option[String]]("SPRIDEN_CHANGE_IND")

  def * = (PIDM, ID, FIRST_NAME, LAST_NAME, CHANGE_IND) <> (Spriden_r.tupled, Spriden_r.unapply)
}