package io.armory.plugin.stage.wait

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/led")
class RandomWaitController(private val randomWaitService: RandomWaitService) {

    @GetMapping("/colors")
    fun test(): String? {
        return randomWaitService.getLedColors()
    }
}
