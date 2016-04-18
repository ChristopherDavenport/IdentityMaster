package Tables

import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by davenpcm on 4/12/2016.
  */
case class Gobeacc_r(
                    pidm: Int,
                    userName: String
                    )

class GOBEACC (tag: Tag) extends Table[Gobeacc_r](tag, "GOBEACC") {
  def PIDM = column[Int]("GOBEACC_PIDM")
  def USERNAME = column[String]("GOBEACC_USERNAME")

  def * = (PIDM, USERNAME) <> (Gobeacc_r.tupled, Gobeacc_r.unapply)
}