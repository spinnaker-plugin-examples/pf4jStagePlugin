package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import java.util.*
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

  private val log = LoggerFactory.getLogger(RandomWaitStage::class.java)

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
