package Tables

/**
  * Created by davenpcm on 4/8/2016.
  */
import com.typesafe.slick.driver.oracle.OracleDriver.api._

case class IdentRecord(
                        Username: String,
                        Pidm: Int,

                        EckerdId: Option[String],
                        FirstName: Option[String],
                        LastName: Option[String],

                        EnterpriseUsername: Option[String],

                        EmployeeClass: Option[String],
                        EmployeeStatus: Option[String],
                        HomeOrg: Option[String],

                        JobOrg: Option[String],
                        JobPosn: Option[String],

                        Roles: Option[String],

                        FacultyStatus: Option[String],
                        FacultyTag: Option[String],
                        FacultySchdInd: Option[String],
                        FacultyAdvisorInd: Option[String],

                        FacultyType: Option[String],

                        StudentStatus: Option[String],
                        StudentLevel: Option[String],

                        Majors: Option[String],
                        Minors: Option[String]

                        )

class IDENT_MASTER (tag: Tag) extends Table[IdentRecord](tag, "IDENT_MASTER") {

  def Username = column[String]("USERNAME", O.PrimaryKey)
  def Pidm = column[Int]("PIDM")

  def EckerdId = column[Option[String]]("ECKERD_ID")
  def FirstName = column[Option[String]]("FIRST_NAME")
  def LastName = column[Option[String]]("LAST_NAME")

  def EnterpriseUsername = column[Option[String]]("ENTERPRISE_USERNAME")

  def EmployeeClass = column[Option[String]]("EMPLOYEE_CLASS")
  def EmployeeStatus = column[Option[String]]("EMPLOYEE_STATUS")
  def HomeOrg = column[Option[String]]("HOME_ORG")

  def JobOrg = column[Option[String]]("TIMESHEET_ORG")
  def JobPosn = column[Option[String]]("PRIMARY_JOB")

  def Roles = column[Option[String]]("ROLES")

  def FacultyStatus = column[Option[String]]("FACULTY_STATUS")
  def FacultyTag = column[Option[String]]("FACULTY_TAG")
  def FacultySchdInd = column[Option[String]]("FACULTY_SCHD_IND")
  def FacultyAdvisorInd = column[Option[String]]("FACULTY_ADVR_IND")
  def FacultyType = column[Option[String]]("FACULTY_TYPE")

  def StudentStatus = column[Option[String]]("STUDENT_STATUS")
  def StudentLevel = column[Option[String]]("STUDENT_LEVEL")

  def StudentMajors = column[Option[String]]("STUDENT_MAJORS")
  def StudentMinors = column[Option[String]]("STUDENT_MINORS")

  def * = (
    Username,
    Pidm ,
    EckerdId,
    FirstName,
    LastName,
    EnterpriseUsername,
    EmployeeClass,
    EmployeeStatus,
    HomeOrg,
    JobOrg,
    JobPosn,
    Roles,
    FacultyStatus,
    FacultyTag,
    FacultySchdInd,
    FacultyAdvisorInd,
    FacultyType,
    StudentStatus,
    StudentLevel,
    StudentMajors,
    StudentMinors
    ) <> (IdentRecord.tupled, IdentRecord.unapply)

  def pidmIndex = index("pidm_index", Pidm, unique=true)
}