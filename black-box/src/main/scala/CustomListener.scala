import org.apache.log4j.LogManager
import org.apache.spark.scheduler._

class CustomListener extends SparkListener {
  private val logger = LogManager.getLogger("CustomListener")

  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    new JdbcConnect().connect().insert(taskEnd.taskType, taskEnd.stageId, taskEnd.taskMetrics).close()
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    new JdbcConnect().connect().insert(stageCompleted.stageInfo).close()
  }
}
