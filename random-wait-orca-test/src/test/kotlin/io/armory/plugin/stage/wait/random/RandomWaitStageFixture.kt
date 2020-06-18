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
import com.netflix.spinnaker.orca.StageResolver
import com.netflix.spinnaker.orca.api.test.OrcaFixture
import java.io.File
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = [
  "spinnaker.extensibility.plugins.Armory.RandomWaitEnabledPlugin.enabled=true",
  "spinnaker.extensibility.plugins.Armory.RandomWaitDisabledPlugin.disabled=true",
  "spinnaker.extensibility.plugins.Armory.RandomWaitNotSupportedPlugin.enabled=true",
  "spinnaker.extensibility.plugins-root-path=build/plugins"
])
class OrcaPluginsFixture : PluginsTckFixture, OrcaFixture() {

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

  init {
    plugins.delete()
    plugins.mkdir()
    enabledPlugin = buildPlugin("Armory.RandomWaitEnabledPlugin", ">=1.0.0")
    disabledPlugin = buildPlugin("Armory.RandomWaitDisabledPlugin", ">=1.0.0")
    versionNotSupportedPlugin = buildPlugin("Armory.RandomWaitNotSupportedPlugin", ">=1000.0.0")
  }
}
