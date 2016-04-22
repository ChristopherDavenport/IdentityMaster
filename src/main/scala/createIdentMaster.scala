import com.typesafe.slick.driver.oracle.OracleDriver.api._
import slick.backend.DatabaseConfig
import Tables.AllTables._
import com.typesafe.slick.driver.oracle.OracleDriver
import scala.concurrent.Await
import scala.concurrent.duration.Duration
/**
  * Created by davenpcm on 4/8/2016.
  * This is a linear time script where all the database interactions take place. Processing Occurs in IdentMethods
  */
object createIdentMaster extends App with IdentMethods{
  // This is the Config
  val dbConfig: DatabaseConfig[OracleDriver] = DatabaseConfig.forConfig("slick.dbs.oracle")
  val db = dbConfig.db
  try {

    // Get All Variables From Database
    val allJobs = Await.result(db.run(jobs.result), Duration.Inf)
    println("Jobs Returned")
    val allEmployees = Await.result( db.run(employees.result), Duration.Inf)
    println("Employees Returned")
    val allRoles = Await.result(db.run(roles.result), Duration.Inf)
    println("Roles Returned")
    val allFaculty = Await.result(db.run(faculty.result), Duration.Inf)
    println("Faculty Returned")
    val allStudents = Await.result(db.run(students.result), Duration.Inf)
    println("Students Returned")
    val allUsers = Await.result(db.run(users.result), Duration.Inf)
    println("Users Returned")
    val allSorlcur = Await.result(db.run(sorlcur.result), Duration.Inf )
    println("Sorlcur Returned")
    val allSorlfos = Await.result(db.run(sorlfos.result), Duration.Inf )
    println("Sorlfos Returned")
    val allSpriden = Await.result(db.run(spriden.result), Duration.Inf )
    println("Spriden Returned")
    val allGobeacc = Await.result(db.run(gobeacc.result), Duration.Inf)
    println("Gobeacc Returned")
    val allPerbfac = Await.result(db.run(perbfac.result), Duration.Inf)
    println("Perbfac Returned")

    // Generating  All records
    val IdentityRecords = GenerateIdents(allUsers,
      allSpriden, allGobeacc, allEmployees, allJobs,
      allRoles, allFaculty, allPerbfac, allStudents,
      allSorlcur, allSorlfos )

    println("Records Created")

    //Recreate Schema and Drop All Old Records
    Await.result(db.run(identities.schema.drop), Duration.Inf)
    Await.result(db.run(identities.schema.create), Duration.Inf)
    println("Schema Created")

    // Insert Them Into the Database
    Await.result(db.run(identities ++= IdentityRecords), Duration.Inf)
    println("Records Inserted")

  } finally db.close()
}
