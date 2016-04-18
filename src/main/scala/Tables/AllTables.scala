package Tables

import com.typesafe.slick.driver.oracle.OracleDriver.api._
/**
  * Created by chris on 4/9/16.
  */
object AllTables {
  // Tables Declaration
  val users = TableQuery[GOBUMAP]
  val jobs = TableQuery[NBRJOBS]
  val employees = TableQuery[PEBEMPL]
  val roles = TableQuery[SOBROLE]
  val faculty = TableQuery[SIBINST]
  val students = TableQuery[SGBSTDN]
  val identities = TableQuery[IDENT_MASTER]
  val sorlcur = TableQuery[SORLCUR]
  val sorlfos = TableQuery[SORLFOS]
  val spriden = TableQuery[SPRIDEN].filter(_.CHANGE_IND.isEmpty)
  val gobeacc = TableQuery[GOBEACC]
  val perbfac = TableQuery[PERBFAC]
}
