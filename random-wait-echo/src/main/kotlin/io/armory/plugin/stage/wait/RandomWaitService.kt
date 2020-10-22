package io.armory.plugin.stage.wait

import org.springframework.stereotype.Component

@Component
class RandomWaitService(val randomWaitProperties: RandomWaitProperties) {

    fun getLedColors(): String? {
        return randomWaitProperties.ledColor
    }

    fun properties(): RandomWaitProperties {
        return randomWaitProperties
    }
}
