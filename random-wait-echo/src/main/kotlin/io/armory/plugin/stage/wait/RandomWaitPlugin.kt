package io.armory.plugin.stage.wait

import com.netflix.spinnaker.kork.plugins.api.spring.SpringLoaderPlugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

class RandomWaitPlugin(wrapper: PluginWrapper) : SpringLoaderPlugin(wrapper) {

    private val logger = LoggerFactory.getLogger(RandomWaitPlugin::class.java)

    override fun getPackagesToScan(): List<String> {
        return listOf(
                "io.armory.plugin.stage.wait"
        )
    }

    override fun start() {
        logger.info("RandomWaitStagePlugin.start()")
    }

    override fun stop() {
        logger.info("RandomWaitStagePlugin.stop()")
    }
}
