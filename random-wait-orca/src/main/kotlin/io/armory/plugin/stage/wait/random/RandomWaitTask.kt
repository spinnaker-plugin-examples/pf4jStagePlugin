

package io.armory.plugin.stage.wait.random

import com.netflix.spinnaker.kork.plugins.api.PluginSdks
import com.netflix.spinnaker.kork.plugins.api.httpclient.Request
import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import java.util.*
import java.util.concurrent.TimeUnit
import org.pf4j.Extension
import org.slf4j.LoggerFactory

@Extension
@Task.Aliases("io.armory.plugin.stage.wait.random.RandomWaitStage\$RandomWaitTask")
class RandomWaitTask(pluginSdks: PluginSdks, private val config: RandomWaitConfig) : Task {

    private val log = LoggerFactory.getLogger(RandomWaitTask::class.java)
    val pluginSdks = pluginSdks

    /**
     * This method is called when the task is executed.
     */
    override fun execute(stage: StageExecution): TaskResult {
        val context = stage.mapTo(RandomWaitContext::class.java)
        val rand = Random()
        val maxWaitTime = context.maxWaitTime ?: config.defaultMaxWaitTime
        val timeToWait = rand.nextInt(maxWaitTime)

        val echo = pluginSdks.http().getInternalService("echo")
        val request = Request("getEchoRequest", "/pubsub/subscriptions")
        val response = echo.get(request)
        val subscriptionList = response.body.bufferedReader().readLines()

        try {
            TimeUnit.SECONDS.sleep(timeToWait.toLong())
        } catch (e: Exception) {
            log.error("{}", e)
        }

        return TaskResult.builder(ExecutionStatus.SUCCEEDED)
                .context(mutableMapOf(
                        "maxWaitTime" to maxWaitTime,
                        "echo" to echo
                ))
                .outputs(mutableMapOf(
                        "timeToWait" to timeToWait,
                        "subscriptionList" to subscriptionList
                ))
                .build()
    }
}
