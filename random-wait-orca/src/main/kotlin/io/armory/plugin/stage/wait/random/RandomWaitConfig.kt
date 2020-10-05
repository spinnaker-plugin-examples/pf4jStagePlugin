package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.kork.plugins.api.PluginConfiguration

/**
 * Data in this class maps to the plugin configuration in a service's config YAML.
 * The data can be key/value pairs or an entire configuration tree.
 */
@PluginConfiguration
data class RandomWaitConfig(var defaultMaxWaitTime: Int)
