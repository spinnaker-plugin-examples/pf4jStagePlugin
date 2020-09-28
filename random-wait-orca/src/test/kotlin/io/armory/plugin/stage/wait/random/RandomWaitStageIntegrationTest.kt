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

import com.fasterxml.jackson.module.kotlin.readValue
import com.netflix.spinnaker.orca.api.test.orcaFixture
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import strikt.api.expect
import strikt.assertions.isEqualTo

/**
 * This test demonstrates that the RandomWaitPlugin can be loaded by Orca
 * and that RandomWaitStage's StageDefinitionBuilder can be retrieved at runtime.
 */
class RandomWaitStageIntegrationTest : JUnit5Minutests {

  fun tests() = rootContext<OrcaPluginsFixture> {
    context("a running Orca instance") {
      orcaFixture {
        OrcaPluginsFixture()
      }

      test("RandomWaitStage extension is resolved to the correct type") {
        val stageDefinitionBuilder = stageResolver.getStageDefinitionBuilder(
            RandomWaitStage::class.java.simpleName, "randomWait")

        expect {
          that(stageDefinitionBuilder.type).isEqualTo("randomWait")
        }
      }

      test("RandomWaitStage can be executed as a stage within a live pipeline execution") {
        val response = mockMvc.post("/orchestrate") {
          contentType = MediaType.APPLICATION_JSON
          content = mapper.writeValueAsString(mapOf(
            "application" to "pf4j-stage-plugin",
            "stages" to listOf(mapOf(
              "refId" to "1",
              "type" to "randomWait",
              "maxWaitTime" to 1
            ))
          ))
        }.andReturn().response

        expect {
          that(response.status).isEqualTo(200)
        }

        val ref = mapper.readValue<ExecutionRef>(response.contentAsString).ref

        var execution: Execution
        do {
          execution = mapper.readValue(mockMvc.get(ref).andReturn().response.contentAsString)
        } while (execution.status != "SUCCEEDED")

        expect {
          that(execution)
            .get { stages.first() }
            .and {
              get { type }.isEqualTo("randomWait")
              get { status }.isEqualTo("SUCCEEDED")
              get { context.maxWaitTime }.isEqualTo(1)
            }
        }
      }
    }
  }

  data class ExecutionRef(val ref: String)
  data class Execution(val status: String, val stages: List<Stage>)
  data class Stage(val status: String, val context: RandomWaitContext, val type: String)
}
