import org.apache.spark.scheduler._

class CustomListener extends SparkListener {

  override def onTaskEnd(taskEnd: SparkListenerTaskEnd) = {
    new JdbcConnect()
      .connect()
      .insert(taskEnd.taskType, taskEnd.stageId, taskEnd.taskMetrics)
      .close()
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted) = {
    new JdbcConnect()
      .connect()
      .insert(stageCompleted.stageInfo)
      .close()
  }
}
