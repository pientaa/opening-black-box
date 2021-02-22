import org.apache.log4j.LogManager
import org.apache.spark.scheduler._

class CustomListener extends SparkListener {
  private val logger = LogManager.getLogger("CustomListener")

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    new JdbcConnect().connect().insert(stageCompleted.stageInfo).close()
  }
}
