import org.apache.log4j.LogManager
import org.apache.spark.scheduler._

class CustomListener extends SparkListener {
  private val logger = LogManager.getLogger("CustomListener")

  override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
    logger.warn(s"Job Id: ${jobStart.jobId} STARTING")
    logger.warn(s"Job properties: ${jobStart.properties}")
    logger.warn(s"Job stage ids: ")
    jobStart.stageIds.foreach(stageId =>
      logger.warn(s"${stageId}")
    )
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    new JdbcConnect().connect().insert(stageCompleted.stageInfo).close()
  }
}
