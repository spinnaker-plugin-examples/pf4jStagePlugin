/*
 * Copyright 2020 Armory, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.kork.plugins.tck.PluginsTck
import com.netflix.spinnaker.kork.plugins.tck.serviceFixture
import dev.minutest.rootContext
import strikt.api.expect
import strikt.assertions.isEqualTo

/**
 * This test demonstrates that the RandomWaitPlugin can be loaded by Orca
 * and that RandomWaitStage's StageDefinitionBuilder can be retrieved at runtime.
 */
class RandomWaitStageTckTest : PluginsTck<OrcaPluginsFixture>() {

  fun tests() = rootContext<OrcaPluginsFixture> {
    context("a running Orca instance") {
      serviceFixture {
        OrcaPluginsFixture()
      }

      defaultPluginTests()

      test("RandomWaitStage extension is resolved to the correct type") {
        val stageDefinitionBuilder = stageResolver.getStageDefinitionBuilder(
            RandomWaitStage::class.java.simpleName, "randomWait")

        expect {
          that(stageDefinitionBuilder.type).isEqualTo("simple")
        }
      }
    }
  }
}
