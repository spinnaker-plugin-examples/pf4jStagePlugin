package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.test.stage
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class RandomWaitStageTest : JUnit5Minutests {

  fun tests() = rootContext {
    test("execute random wait stage") {

      val stage = stage {
        type = "randomWait"
        context = mapOf(
          "maxWaitTime" to 1
        )
      }

      val task = RandomWaitTask(RandomWaitConfig(0))
      expectThat(task.execute(stage)) {
        get { status }.isEqualTo(ExecutionStatus.SUCCEEDED)
        get { context["maxWaitTime"] }.isNotNull().isEqualTo(1)
        get { outputs["timeToWait"] }.isNotNull()
      }
    }
  }
}
