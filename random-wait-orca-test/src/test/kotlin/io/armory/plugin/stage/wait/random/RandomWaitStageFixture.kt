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

import com.netflix.spinnaker.kork.plugins.SpinnakerPluginManager
import com.netflix.spinnaker.kork.plugins.internal.PluginJar
import com.netflix.spinnaker.kork.plugins.tck.PluginsTckFixture
import com.netflix.spinnaker.orca.Main
import com.netflix.spinnaker.orca.StageResolver
import com.netflix.spinnaker.orca.notifications.NotificationClusterLock
import com.netflix.spinnaker.orca.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.q.Queue
import com.netflix.spinnaker.q.memory.InMemoryQueue
import com.netflix.spinnaker.q.metrics.EventPublisher
import java.io.File
import java.time.Clock
import java.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

class OrcaPluginsFixture : PluginsTckFixture, OrcaTestService() {

  final override val plugins = File("build/plugins")
  final override val enabledPlugin: PluginJar
  final override val disabledPlugin: PluginJar
  final override val versionNotSupportedPlugin: PluginJar

  override val extensionClassNames: MutableList<String> = mutableListOf(
      RandomWaitStage::class.java.name
  )

  final override fun buildPlugin(pluginId: String, systemVersionRequirement: String): PluginJar {
    return PluginJar.Builder(plugins.toPath().resolve("$pluginId.jar"), pluginId)
        .pluginClass(RandomWaitPlugin::class.java.name)
        .pluginVersion("1.0.0")
        .manifestAttribute("Plugin-Requires", "orca$systemVersionRequirement")
        .extensions(extensionClassNames)
        .build()
  }

  @Autowired
  override lateinit var spinnakerPluginManager: SpinnakerPluginManager

  @Autowired
  lateinit var stageResolver: StageResolver

  @MockBean
  var executionRepository: ExecutionRepository? = null

  @MockBean
  var notificationClusterLock: NotificationClusterLock? = null

  init {
    plugins.delete()
    plugins.mkdir()
    enabledPlugin = buildPlugin("Armory.RandomWaitPlugin.Enabled", ">=1.0.0")
    disabledPlugin = buildPlugin("Armory.RandomWaitPlugin.Disabled", ">=1.0.0")
    versionNotSupportedPlugin = buildPlugin("Armory.RandomWaitPlugin.NotSupported", ">=2.0.0")
  }
}

@SpringBootTest(classes = [Main::class])
@ContextConfiguration(classes = [PluginTestConfiguration::class])
@TestPropertySource(properties = ["spring.config.location=classpath:random-wait-test.yml"])
abstract class OrcaTestService

@TestConfiguration
internal class PluginTestConfiguration {

  @Bean
  @Primary
  fun queue(clock: Clock?, publisher: EventPublisher?): Queue {
    return InMemoryQueue(
        clock!!, Duration.ofMinutes(1), emptyList(), false, publisher!!)
  }
}
