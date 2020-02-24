package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.kork.plugins.api.ExtensionConfiguration

@ExtensionConfiguration("armory.randomWaitStage")
data class RandomWaitConfig(var defaultMaxWaitTime: Int)
