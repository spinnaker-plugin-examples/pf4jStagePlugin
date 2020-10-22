package io.armory.plugin.stage.wait

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blah")
data class RandomWaitProperties(
        var ledColor: String? = null
)
