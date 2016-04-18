package Tables

/**
  * Created by davenpcm on 4/6/2016.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class User(
                 Identifier: Option[String] = None,
                 Pidm : Int
               )

case class GoodDataUser(
                       Identifier: String,
                       Pidm: Int
                       )

class GOBUMAP(tag: Tag) extends Table[GoodDataUser](tag, "GOBUMAP") {
  def UDC_ID = column[String]("GOBUMAP_UDC_ID")
  def pidm = column[Int]("GOBUMAP_PIDM")

  def * = (UDC_ID, pidm) <> (GoodDataUser.tupled, GoodDataUser.unapply)
}

class GOBTPAC (tag: Tag) extends Table[User](tag, "GOBTPAC") {

    def GOBTPAC_LDAP_USER = column[Option[String]]("GOBTPAC_LDAP_USER")
//    def GOBTPAC_EXTERNAL_USER = column[Option[String]]("GOBTPAC_EXTERNAL_USER")
    def GOBTPAC_PIDM = column[Int]("GOBTPAC_PIDM")

     def * = (GOBTPAC_LDAP_USER, GOBTPAC_PIDM) <> (User.tupled, User.unapply)
}
