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

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.netflix.spinnaker.kork.plugins.internal.PluginJar
import com.netflix.spinnaker.orca.StageResolver
import com.netflix.spinnaker.orca.api.test.OrcaFixture
import java.io.File
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc

@TestPropertySource(properties = [
  "spinnaker.extensibility.plugins.Armory.RandomWaitPlugin.enabled=true",
  "spinnaker.extensibility.plugins-root-path=build/plugins"
])
@AutoConfigureMockMvc
class OrcaPluginsFixture : OrcaFixture() {

  @Autowired
  lateinit var stageResolver: StageResolver

  @Autowired
  lateinit var mockMvc: MockMvc

  val mapper = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

  init {
    val pluginId = "Armory.RandomWaitPlugin"
    val plugins = File("build/plugins").also {
      it.delete()
      it.mkdir()
    }

    PluginJar.Builder(plugins.toPath().resolve("$pluginId.jar"), pluginId)
      .pluginClass(RandomWaitPlugin::class.java.name)
      .pluginVersion("1.0.0")
      .extensions(mutableListOf(RandomWaitStage::class.java.name, RandomWaitTask::class.java.name))
      .build()
  }
}
