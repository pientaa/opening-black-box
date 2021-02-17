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

  override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = {
    logger.warn(s"Job Id: ${jobEnd.jobId} ENDING")
    logger.warn(s"Job result: ${jobEnd.jobResult}")
    logger.warn(s"Job time: ${jobEnd.time}")

  }

  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {
    logger.warn(s"Application: ${applicationStart.appId} STARTING")
    logger.warn(s"Application time: ${applicationStart.time}")
    logger.warn(s"Application driver logs: ${applicationStart.driverLogs}")
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    logger.warn(s"Application: ENDING")
    logger.warn(s"Application time: ${applicationEnd.time}")
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    logger.warn(s"Stage: ${stageCompleted.stageInfo.stageId} COMPLETED")

    new JdbcConnect().connect().insert(stageCompleted.stageInfo).close()

    logger.warn("ParentIds: ")
    stageCompleted.stageInfo.parentIds.foreach(logger.warn(_))

    logger.warn(s"Bytes read: ${stageCompleted.stageInfo.taskMetrics.inputMetrics.bytesRead} ")
    logger.warn(s"Bytes written: ${stageCompleted.stageInfo.taskMetrics.outputMetrics.bytesWritten} ")

    logger.warn(s"RunTime: ${stageCompleted.stageInfo.taskMetrics.executorRunTime}, " +
      s"cpuTime: ${stageCompleted.stageInfo.taskMetrics.executorCpuTime}")

    logger.warn(s"Peak Execution memory: ${stageCompleted.stageInfo.taskMetrics.peakExecutionMemory}")

    logger.warn(s"Num of tasks: ${stageCompleted.stageInfo.numTasks}")

    logger.warn(s"JVM GC time: ${stageCompleted.stageInfo.taskMetrics.jvmGCTime}")
  }
}
