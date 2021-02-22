import org.apache.log4j.LogManager
import org.apache.spark.scheduler.StageInfo

import java.sql.{Connection, DriverManager, Statement}

class JdbcConnect {
  val driver: String = "org.postgresql.Driver";
  val url: String = BlackBox.url
  val username: String = "postgres"
  val password: String = "postgres"
  var connection: Connection = null
  var statement: Statement = null

  private val logger = LogManager.getLogger("JdbcConnect")


  def connect(): JdbcConnect = {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
    statement = connection.createStatement()
    this
  }

  def insert(stageInfo: StageInfo): JdbcConnect = {
    val sql = s"insert into metrics(function_name, stage_id, details, num_tasks, name, " +
      s"submission_time, completion_time, executor_run_time, result_size, jvm_gc_time, peak_execution_memory, disk_bytes_spilled, memory_bytes_spilled) " +
      s"values('${BlackBox.functionName}', ${stageInfo.stageId}, '${stageInfo.details}', ${stageInfo.numTasks}, " +
      s"'${stageInfo.name}', ${stageInfo.submissionTime.get}, ${stageInfo.completionTime.get}, ${stageInfo.taskMetrics.executorRunTime}, ${stageInfo.taskMetrics.resultSize}, " +
      s"${stageInfo.taskMetrics.jvmGCTime}, ${stageInfo.taskMetrics.peakExecutionMemory}, ${stageInfo.taskMetrics.diskBytesSpilled}, ${stageInfo.taskMetrics.memoryBytesSpilled})"

    logger.warn(sql)
    statement.execute(sql)

    this
  }

  def close(): Unit = {
    connection.close()
  }
}