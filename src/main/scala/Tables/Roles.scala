package Tables

/**
  * Created by davenpcm on 4/8/2016.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class Role (
                   Pidm: Int,
                   TermCode: String,
                   RoleCode: String
                 )


class SOBROLE(tag: Tag) extends Table[Role](tag, "SOBROLE"){
  def SOBROLE_PIDM = column[Int]("SOBROLE_PIDM")
  def SOBROLE_TERM_CODE_EFF = column[String]("SOBROLE_TERM_CODE_EFF")
  def SOBROLE_ROLE_CODE = column[String]("SOBROLE_ROLE_CODE")

  def * = (SOBROLE_PIDM, SOBROLE_TERM_CODE_EFF, SOBROLE_ROLE_CODE) <> (Role.tupled, Role.unapply)
}
