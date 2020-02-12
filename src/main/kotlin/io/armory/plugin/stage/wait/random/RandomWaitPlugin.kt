package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.kork.plugins.api.ConfigurableExtension
import com.netflix.spinnaker.kork.plugins.api.SpinnakerExtension
import org.slf4j.LoggerFactory
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import com.netflix.spinnaker.orca.api.SimpleStageStatus
import com.netflix.spinnaker.orca.api.SimpleStageOutput
import java.util.concurrent.TimeUnit
import com.netflix.spinnaker.orca.api.SimpleStageInput
import com.netflix.spinnaker.orca.api.SimpleStage
import java.util.*


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
 * Example stage that implements the Orca API Stage interface. By implementing Stage,
 * your stage is available for use in Spinnaker.
 */
@Extension
@SpinnakerExtension(id = "armory.randomWaitStage")
class RandomWaitStage : SimpleStage<RandomWaitInput>, ConfigurableExtension<RandomWaitConfig> {

    private val log = LoggerFactory.getLogger(SimpleStage::class.java)

    private var configuration: RandomWaitConfig? = null

    override fun setConfiguration(configuration: RandomWaitConfig?) {
        this.configuration = configuration
    }

    /**
     * This sets the name of the stage
     * @return the name of the stage
     */
    override fun getName(): String {
        return "randomWait (${configuration!!.defaultMaxWaitTime})"
    }

    /**
     * This is what gets ran when the stage is executed. It takes in an object that you create. That
     * object contains fields that one wishes to pull out of the pipeline context. This gives us a
     * strongly typed object that you have full control over. The function returns a SimpleStageOutput object.
     * The SimpleStageOutput class contains the status of the stage and any stage outputs that should be
     * put back into the pipeline context.
     * @param stageInput<RandomWaitInput>
     * @return the status of the stage and any context that should be passed to the pipeline context
    </RandomWaitInput> */
    override fun execute(stageInput: SimpleStageInput<RandomWaitInput>): SimpleStageOutput<*, *> {
        val rand = Random()
        val maxWaitTime = stageInput.value.maxWaitTime
        val timeToWait = rand.nextInt(maxWaitTime)

        try {
            TimeUnit.SECONDS.sleep(timeToWait.toLong())
        } catch (e: Exception) {
            log.error("{}", e)
        }

        val stageOutput = SimpleStageOutput<Output, Context>()
        val output = Output(timeToWait)
        val context = Context(maxWaitTime)

        stageOutput.setOutput(output)
        stageOutput.setContext(context)
        stageOutput.setStatus(SimpleStageStatus.SUCCEEDED)

        return stageOutput
    }
}
