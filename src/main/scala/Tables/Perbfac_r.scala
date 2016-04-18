package Tables
import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by davenpcm on 4/12/2016.
  */
case class Perbfac_r(
                    pidm: Int,
                    ftyp_code: Option[String]
                    )


class PERBFAC(tag: Tag) extends Table[Perbfac_r](tag, "PERBFAC"){
  def PIDM = column[Int]("PERBFAC_PIDM")
  def FTYP_CODE = column[Option[String]]("PERBFAC_FTYP_CODE")

  def * = (PIDM, FTYP_CODE) <> (Perbfac_r.tupled, Perbfac_r.unapply)
}