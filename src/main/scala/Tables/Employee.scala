package Tables

/**
  * Created by davenpcm on 4/8/2016.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class Employee(Pidm: Int,
                    Status: String,
                    HomeOrg: String,
                    EmployeeClass: String)

class PEBEMPL (tag: Tag) extends Table[Employee](tag, "PEBEMPL") {
  def PEBEMPL_PIDM = column[Int]("PEBEMPL_PIDM")
  def PEBEMPL_EMPL_STATUS = column[String]("PEBEMPL_EMPL_STATUS")
  def PEBEMPL_ORGN_CODE_HOME = column[String]("PEBEMPL_ORGN_CODE_HOME")
  def PEBEMPL_ECLS_CODE = column[String]("PEBEMPL_ECLS_CODE")
  def * = (PEBEMPL_PIDM, PEBEMPL_EMPL_STATUS, PEBEMPL_ORGN_CODE_HOME, PEBEMPL_ECLS_CODE) <> (Employee.tupled, Employee.unapply)
}