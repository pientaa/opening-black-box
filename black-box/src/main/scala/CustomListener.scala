import org.apache.log4j.LogManager
import org.apache.spark.scheduler._

class CustomListener extends SparkListener {
  private val logger = LogManager.getLogger("CustomListener")

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = super.onApplicationEnd(applicationEnd)

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    logger.warn(s"Stage completed, runTime: ${stageCompleted.stageInfo.taskMetrics.executorRunTime}, " +
      s"cpuTime: ${stageCompleted.stageInfo.taskMetrics.executorCpuTime}")

    logger.warn(s"Peak Execution memory: ${stageCompleted.stageInfo.taskMetrics.peakExecutionMemory}")

    logger.warn(s"Num tasks: ${stageCompleted.stageInfo.numTasks}")

    logger.warn(s"JVM GC time: ${stageCompleted.stageInfo.taskMetrics.jvmGCTime}")
  }
}
