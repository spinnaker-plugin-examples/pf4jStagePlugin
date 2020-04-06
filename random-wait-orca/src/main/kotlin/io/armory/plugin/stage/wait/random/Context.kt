package io.armory.plugin.stage.wait.random

/**
 * Context is used within the stage itself. It is returned to the Orca execution point, much like Output.
 */
data class Context(var maxWaitTime: Int) {}
