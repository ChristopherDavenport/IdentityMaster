import Tables._
import slick.jdbc.JdbcBackend

import language.implicitConversions

/**
  * Created by chris on 4/9/16.
  */
trait IdentMethods {
  /**
    * This takes a user and and all Spriden records to generate FirstName, LastName, and UserId Of the User.
    * Takes the first user that has the pidm and where the change ind is empty.
    *
    * @param user Some User
    * @param idents Set of
    * @return A tuple of FirstName, LastName, and UserId if they were in the set and a tuple of none if they are not.
    */
  def UserToIdentColumns(user: User, idents: Seq[Spriden_r]):
  (Option[String], Option[String], Option[String]) = {

    def UserIdent(pidm: Int, idents: Seq[Spriden_r]): Option[Spriden_r] = {
      val ident = idents.find( v =>  v.pidm == pidm &&  v.changeInd.isEmpty )
      ident
    }
    def IdentColumns(ident: Option[Spriden_r]): (Option[String], Option[String], Option[String]) = ident match {
      case None => (None, None, None)
      case Some(s) => ( Some(s.id), Some(s.firstName), Some(s.lastName) )
    }

    val UIdent = UserIdent(user.Pidm, idents)
    val IColumns = IdentColumns(UIdent)

    IColumns
  }

  /**
    * This Method Returns The Best Email
    * @param user Some User
    * @param idents A Set of GOREMAL Records
    * @return Best Email Record For The User
    */
  def UserToEmail(user: User, idents: Seq[GOREMAL_R]): Option[String] = {
    val EmalCodes = List("CA", "CAS", "ECA", "ZCA", "ZCAS", "ZCH")
    def findBestEmail(pidm: Int, idents: Seq[GOREMAL_R]): Option[String] = {
      idents.filter(rec => rec.pidm == pidm && EmalCodes.contains(rec.emal_code))
        .sortWith(_.emal_code < _.emal_code)
        .map(_.email)
        .headOption
    }
    findBestEmail(user.Pidm, idents)
  }

  /**
    * Produces the Username in Gobeacc if it exists
    *
    * @param user Some User
    * @param enterpriseUsers A Set of GOBEACC records
    * @return Option of GOBEACC_USERNAME if the user is in the set of Gobeacc records
    */
  def UserToEnterpriseUsername(user: User, enterpriseUsers: Seq[Gobeacc_r]): Option[String] = {

    def UserEnterprise(pidm: Int, enterpriseUser: Seq[Gobeacc_r]): Option[Gobeacc_r] = {
      val pidmUserName = enterpriseUser.find(_.pidm == pidm)
      pidmUserName
    }

    def EnterpriseColumn(ident: Option[Gobeacc_r]) = ident match {
      case Some(exists) => Some(exists.userName)
      case None => None
    }
    val UEnterprise = UserEnterprise(user.Pidm, enterpriseUsers)
    val UColumn = EnterpriseColumn(UEnterprise)

    UColumn
  }

  /**
    * Generates Employee Class, Employee Staus, and Home Org For Each user if they exist in PEBEMPL
    *
    * @param user Some User
    * @param employees A Set of Employee Records from PEBEMPL
    * @return A tuple of Employee Class, Employee Status, and Home Org if the user exists,
    *         a tuple of Nones if they do not
    */
  def UserToEmployeeColumns(user: User, employees: Seq[Employee]): (Option[String], Option[String], Option[String]) = {
    def UserEmployee(pidm: Int, employees: Seq[Employee]): Option[Employee] = {
      val pidmEmployee = employees.find(_.Pidm == pidm)
      pidmEmployee
    }

    def EmployeeColumns(emp: Option[Employee]): (Option[String], Option[String], Option[String]) = emp match {
      case Some(empl) => (Some(empl.EmployeeClass), Some(empl.Status), Some(empl.HomeOrg))
      case None => (None, None, None)
    }

    val UEmployee = UserEmployee(user.Pidm, employees)
    val EColumns = EmployeeColumns(UEmployee)
    EColumns
  }

  /**
    * Generates Timesheet Org and Primary Job Code for a user if they exist in the set NBRJOBS records.
    * Only takes the Job with the most recent effective date.
    *
    * @param user Some User
    * @param jobs A Set of NBRJOBS records
    * @return A tuple of Timesheet Org and Primary Job if they exist, Otherwise A tuple of None
    */
  def UserToJobColumns(user: User, jobs: Seq[Job]): (Option[String], Option[String]) = {

    def UserJob(pidm: Int, jobs: Seq[Job]): Option[Job] = {
      val pidmJobs = jobs.filter(_.Pidm == pidm)
      val sorter = pidmJobs.sortWith(_.EffDate after _.EffDate).headOption
      sorter
    }

    def JobColumns(job: Option[Job]): (Option[String], Option[String]) = job match {
      case Some(j) => (Some(j.OrgCode), Some(j.Posn))
      case None => (None, None)
    }

    val UJob = UserJob(user.Pidm, jobs)
    val columns = JobColumns(UJob)

    columns
  }

  /**
    * Generates Column of all Role Values for a user if they exist
    *
    * @param user Some User
    * @param roles A set of SOBROLE records
    * @return A single option of All the roles concatenated or None if the User did not exist in the set.
    */
  def UserToRoleColumn(user: User, roles: Seq[Role]): Option[String] = {
    def UserRole(pidm: Int, roles: Seq[Role]): Option[Role] = {
      val pidmRoles = roles.filter(_.Pidm == pidm)
      val sorted = pidmRoles.sortWith(_.TermCode > _.TermCode)
      val effectiveTerm = sorted.headOption
      val singleRoleDef = effectiveTerm match {
        case None => None
        case Some(effTerm) =>
          val effective = sorted.takeWhile(_.TermCode == effTerm.TermCode)
          val userRoles = effective.foldLeft(" "){(s: String, r: Role ) => s + r.RoleCode + " "}
          Some( Role( pidm, effTerm.TermCode, userRoles) )
      }
      singleRoleDef
    }

    def RoleColumn(role: Option[Role]): Option[String] = role match {
      case Some(r) => Some(r.RoleCode)
      case None => None
    }

    val URole = UserRole(user.Pidm, roles)
    val RColumn = RoleColumn(URole)
    RColumn
  }

  /**
    * This function produces primary faculty information on a user object
    *
    * @param user Some User
    * @param faculty A set of records from SIBINST
    * @return A Tuple consisting of Faculty Status, Faculty Tag, Faculty Schd Indicator, and Faculty Advisor Indicator
    */
  def UserToFacultyColumns(user: User, faculty: Seq[Faculty]):
  (Option[String], Option[String], Option[String], Option[String]) = {
    def UserFaculty(pidm: Int, facultyMems: Seq[Faculty]): Option[Faculty] = {
      val pidmFaculty = facultyMems.filter(_.Pidm == pidm)
      val sorted = pidmFaculty.sortWith(_.TermCodeEff > _.TermCodeEff)
      val headRecord = sorted.headOption
      headRecord
    }

    def FacultyColumns(faculty: Option[Faculty]):
    (Option[String], Option[String], Option[String], Option[String]) = faculty match {
      case Some(f) => (Some(f.FacultyStatus), f.FacultyTag, f.ScheduleInd, f.AdvisorInd)
      case None => (None, None, None , None)
    }


    val UFaculty = UserFaculty(user.Pidm, faculty)
    val FColumns = FacultyColumns(UFaculty)
    FColumns
  }

  /**
    * Produces Faculty Type Code from a user
    *
    * @param user Some User
    * @param faculty A set of records from PERBFAC
    * @return An Option containing a faculty type code if it exists for the user
    */
  def UserToFacultyTypeColumn(user: User, faculty: Seq[Perbfac_r]): Option[String] = {
    def UserFaculty(pidm: Int, facultyMems: Seq[Perbfac_r]): Option[Perbfac_r] = {
      val pidmFaculty = facultyMems.find(_.pidm == pidm)
      pidmFaculty
    }

    def FacultyTypeColumn(r: Option[Perbfac_r]): Option[String] = r match {
      case None => None
      case Some(s) => s.ftyp_code
    }
    val UFaculty = UserFaculty(user.Pidm, faculty)
    val Column = FacultyTypeColumn(UFaculty)

    Column
  }

  /**
    * Check against SGBSTDN for the Pidm then filter to take the most recent effective term code.
    *
    * @param user Some User
    * @param students A set of SGBSTDN records
    * @return A tuple of option strings showing student status and student level if they exist for the user
    */
  def UserToStudentColumns(user: User, students: Seq[Student]):
  (Option[String], Option[String], Option[String]) = {
    def UserStudent(pidm: Int, students: Seq[Student]): Option[Student] = {
      val pidmStudent = students.filter(_.Pidm == pidm)
      val sorted = pidmStudent.sortWith(_.TermCodeEff > _.TermCodeEff)
      val headRecord = sorted.headOption
      headRecord
    }

    def StudentColumns(student: Option[Student]):(Option[String], Option[String], Option[String]) = student match {
      case Some(s) =>
        val term = DatabaseMethods.getMaxTerm().getOrElse("")
        val classCode = DatabaseMethods.getClassCode(s.Pidm, s.LevelCode, term)
        (Some(s.StudentStatus), Some(s.LevelCode), classCode)
      case None => (None, None, None)
    }

    val UStudent = UserStudent(user.Pidm, students)
    val SColumns = StudentColumns(UStudent)
    SColumns
  }

  /**
    * This takes a set of SORLCUR records filters to the appropriate pidm for the user and with a cactCode set to Active
    * Then we sort the highest seqno and take the first record if it exists, this is to get the most recent seqno. Then
    * we take Sorlfos with what was done earlier and take all records with that pidm and the seqno from the previous
    * check then we take the most recent activity data first. Finally to get the columns we only take those with
    * activity date that matches the highest activity date(for multiple activity dates on same seqno which causes
    * overlapping entries) Then we take lfst code that is major or minor as the split for the two columns and take the
    * lowest priority number as the lower the priority the more important it is (i.e. First priority)
    *
    * As an explanation of foldLeft - It is a recursive function that takes an argument in the first set of brackets
    * and a function which takes the type of the Iterable Sequence(in this case a sorlfos_r) and the argument type
    * (string) and produces and output of the type type of the argument(again, string) then repeats this for all
    * elements in the sequence. In this case we create the major and a space for each record found starting with an
    * initial space
    *
    *
    * @param user Some User
    * @param sorlcur Set of Records from SORLCUR
    * @param sorlfos Set of Records from SORLFOS
    * @return A tuple of Option Strings Indicating Majors and Minors for the user if they exist
    */
  def UsertoMajorMinorColumns(user: User, sorlcur: Seq[Sorlcur_r], sorlfos: Seq[Sorlfos_r]):
  (Option[String], Option[String]) = {

    def UserSorlcur(pidm: Int, sorlcur: Seq[Sorlcur_r]): Option[Sorlcur_r] = {
      val pidmsorlcur = sorlcur.filter(v =>
        v.pidm == pidm && v.cactCode == "ACTIVE" && (v.LmodCode == "LEARNER" || v.LmodCode == "OUTCOME"))
      val sorted = pidmsorlcur.sortWith(_.seqNo > _.seqNo)
      val headRecord = sorted.headOption
      headRecord
    }

    def UserSorlfos(sorlcur: Option[Sorlcur_r], sorlfos: Seq[Sorlfos_r] ): Option[Seq[Sorlfos_r]] = sorlcur match {
      case None => None
      case Some(s) =>
        val UCur = sorlfos.filter( v =>
          v.pidm == s.pidm && v.lcurSeqNo == s.seqNo
        )
        val sorted = UCur.sortWith( _.activityDate after _.activityDate)
        Some(sorted)
    }

    def SorlfosToMajorMinorColumsn(sorlfosRecords: Option[Seq[Sorlfos_r]]):
    (Option[String], Option[String]) = sorlfosRecords match {
        case None => (None, None)
        case Some(s) =>
          val recentDate = s.head.activityDate
          val records = s.takeWhile(_.activityDate == recentDate)
          val majors = records.filter(_.lfstCode == "MAJOR").sortWith(_.priorityNo < _.priorityNo)
          val minors = records.filter(_.lfstCode == "MINOR").sortWith(_.priorityNo < _.priorityNo)
          val majorString: Option[String] = majors.length match {
            case 0 => None
            case _ =>
              val majorStringBuild = majors.foldLeft(" ")((s: String, b: Sorlfos_r) => s + b.majorCode + " ")
              Some(majorStringBuild)
          }
          val minorString: Option[String] = minors.length match {
            case 0 => None
            case _ =>
              val minorStringBuild = minors.foldLeft(" ")((s: String, b: Sorlfos_r) => s + b.majorCode + " ")
              Some(minorStringBuild)
          }
          (majorString, minorString)
    }

    val UserCur = UserSorlcur(user.Pidm, sorlcur)
    val UserFos = UserSorlfos(UserCur, sorlfos)
    SorlfosToMajorMinorColumsn(UserFos)
  }

  /**
    * This converts a user to a good data user if possible, meaning that the Optional identifier must be present
    * or else it will be converted to a None
    *
    * @param user Some User
    * @return Option of GoodData User
    */
  def UserConverter(user: User): Option[GoodDataUser] = user.Identifier match {
    case Some(ident) => Some(GoodDataUser(ident, user.Pidm))
    case None => None
  }

  /**
    * This is the implicit conversion from a gooddatauser to a user so that we might right our code for users but then
    * choose to only use good data individuals if we would like and pass them as users wherever we might like
    *
    * @param goodData A GoodData Type that converts the identifier to an option where we might need a user
    * @return A User
    */
  implicit def GoodDataToUser(goodData: GoodDataUser): User = {
    User(Some(goodData.Identifier), goodData.Pidm)
  }

  /**
    * This generates a single ident record, it uses the functions above to create all the necessary fields to create an
    * IdentRecord
    *
    * @param user Some User
    * @param idents Set of Spriden Records
    * @param ents Set of GOBEACC Records
    * @param emps Set of PEBEMPL Records
    * @param jobs Set of NBRJOBS Records
    * @param roles Set OF SOBROLE Records
    * @param faculty Set of SIBINST Records
    * @param perbfacs Set of PERBFAC Records
    * @param students Set of SGBSTDN Records
    * @param sorlcur Set of SORLCUR Records
    * @param sorlfos Set of SORLFOS Records
    * @return An Option of an IdentRecord if there is some user to be run, and a None if we started with a None
    */
  def GenerateIdent(
                    user: Option[GoodDataUser],
                    idents: Seq[Spriden_r],
                    ents: Seq[Gobeacc_r],
                    emps: Seq[Employee],
                    jobs: Seq[Job],
                    roles: Seq[Role],
                    faculty: Seq[Faculty],
                    perbfacs: Seq[Perbfac_r],
                    students: Seq[Student],
                    sorlcur: Seq[Sorlcur_r],
                    sorlfos: Seq[Sorlfos_r],
                   goremal: Seq[GOREMAL_R]): Option[IdentRecord] = user match {
    case None => None
    case Some(u) =>
      val identColumns = UserToIdentColumns(u, idents)
      val entsColumn = UserToEnterpriseUsername(u, ents)
      val emailColumn = UserToEmail(u, goremal)
      val empColumns = UserToEmployeeColumns(u, emps)
      val jobColumn = UserToJobColumns(u, jobs)
      val roleColumn = UserToRoleColumn(u, roles)
      val facultyColumns = UserToFacultyColumns(u, faculty)
      val facultyTypeColumn = UserToFacultyTypeColumn(u, perbfacs)
      val studentColumns = UserToStudentColumns( u, students)
      val majorMinorColumns = UsertoMajorMinorColumns(u, sorlcur, sorlfos )

      Some(
        IdentRecord(
          u.Identifier,
          u.Pidm,
          PersonalInfo(identColumns._1,
          identColumns._2,
          identColumns._3,
          emailColumn),
          BussinessInfo(entsColumn,
          empColumns._1,
          empColumns._2,
          empColumns._3,
          jobColumn._1,
          jobColumn._2,
          roleColumn),
          FacultyInfo(facultyColumns._1,
          facultyColumns._2,
          facultyColumns._3,
          facultyColumns._4,
          facultyTypeColumn),
          StudentInfo(studentColumns._1,
          studentColumns._2,
          studentColumns._3,
          majorMinorColumns._1,
          majorMinorColumns._2)
        )
      )
  }

  /**
    * The generates a set of identity records for all the users that are passed to it.
    *
    * @param users Sequence of GoodDataUser
    * @param identities A Set of Spriden Records
    * @param ents A set of GOBEACC records
    * @param employees A set of PEBEMPL records
    * @param jobs A set of NBRJOBS records
    * @param roles A set of SOBROLE records
    * @param faculty A set of SIBINST records
    * @param perbfacs A set of PERBFAC records
    * @param students A set of SGBSTDN records
    * @param sorlcur A set of SORLCUR records
    * @param sorlfos A set of SORLFOS records
    * @return A set of IdentRecords that have been flattened to remove any that returned completely None
    */
  def GenerateIdents(
                     users: Seq[GoodDataUser],
                     identities: Seq[Spriden_r],
                     ents: Seq[Gobeacc_r],
                     employees: Seq[Employee],
                     jobs: Seq[Job],
                     roles: Seq[Role],
                     faculty: Seq[Faculty],
                     perbfacs: Seq[Perbfac_r],
                     students: Seq[Student],
                     sorlcur: Seq[Sorlcur_r],
                     sorlfos: Seq[Sorlfos_r],
                    goremal: Seq[GOREMAL_R]
                    ): Seq[IdentRecord] = {

    val optionIdentSeq: Seq[Option[IdentRecord]] = users.par.map(user =>
      GenerateIdent(UserConverter(user), identities, ents, employees, jobs, roles, faculty, perbfacs, students, sorlcur, sorlfos, goremal)).seq

    val idents = optionIdentSeq.flatten

    idents
  }

}
