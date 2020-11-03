package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import java.util.*
import java.util.concurrent.TimeUnit
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

class RandomWaitPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
  private val logger = LoggerFactory.getLogger(RandomWaitPlugin::class.java)

  override fun start() {
    logger.info("RandomWaitPlugin.start()")
  }

  override fun stop() {
    logger.info("RandomWaitPlugin.stop()")
  }
}

/**
 * By implementing StageDefinitionBuilder, your stage is available for use in Spinnaker.
 * @see com.netflix.spinnaker.orca.api.StageDefinitionBuilder
 */
@Extension
class RandomWaitStage : StageDefinitionBuilder {

  /**
   * This function describes the sequence of substeps, or "tasks" that comprise this
   * stage. The task graph is generally linear though there are some looping mechanisms.
   *
   * This method is called just before a stage is executed. The task graph can be generated
   * programmatically based on the stage's context.
   */
  override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
    builder.withTask("randomWait", RandomWaitTask::class.java)
  }
}

@Extension
class RandomWaitTask(private val config: RandomWaitConfig) : Task {

  private val log = LoggerFactory.getLogger(RandomWaitTask::class.java)

  /**
   * This method is called when the task is executed.
   */
  override fun execute(stage: StageExecution): TaskResult {
    val context = stage.mapTo(RandomWaitContext::class.java)
    val rand = Random()
    val maxWaitTime = context.maxWaitTime ?: config.defaultMaxWaitTime
    val timeToWait = rand.nextInt(maxWaitTime)

    try {
      TimeUnit.SECONDS.sleep(timeToWait.toLong())
    } catch (e: Exception) {
      log.error("{}", e)
    }

    return TaskResult.builder(ExecutionStatus.SUCCEEDED)
        .context(mutableMapOf("maxWaitTime" to maxWaitTime))
        .outputs(mutableMapOf("timeToWait" to timeToWait))
        .build()
  }
}
