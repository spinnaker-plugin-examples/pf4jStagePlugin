![CI](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/CI/badge.svg)
![Latest Kork](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/Latest%20Kork/badge.svg?branch=master)
![Latest Orca](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/Latest%20Orca/badge.svg?branch=master)

Spinnaker Plugin (PF4J-based) that is a custom pipeline stage.
The [pf4jStagePlugin](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin) creates a custom pipeline stage that waits a random number of seconds before signaling success.
<<<<<<< HEAD
This plugin implements the [SimpleStage](https://github.com/spinnaker/orca/blob/ab89a0d7f847205ccd62e70f8a714040a8621ee7/orca-api/src/main/java/com/netflix/spinnaker/orca/api/SimpleStage.java) PF4J extension point in Orca.
=======
This plugin implements the [SimpleStage](https://github.com/spinnaker/orca/blob/master/orca-api/src/main/java/com/netflix/spinnaker/orca/api/simplestage/SimpleStage.java) PF4J extension point in Orca.
>>>>>>> 36744e8... Incorporate review feedback
The plugin consists of a `random-wait-orca` [Kotlin](https://kotlinlang.org/docs/reference/) server component and a `random-wait-deck` [React](https://reactjs.org/) UI component that uses the [rollup.js](https://rollupjs.org/guide/en/#plugins-overview) plugin library.

This is for demo only and not meant to be used in a production environment.

# Version Compatibility

| Plugin  | Spinnaker Platform |
|:----------- | :--------- |
| 1.0.x  |  1.19.x |
| 1.1.x  | 1.20.x |

# Usage

1) Run `./gradlew releaseBundle`
2) Put the `/build/distributions/<project>-<version>.zip` into the [configured plugins location for your service](https://pf4j.org/doc/packaging.html).
3) Configure the Spinnaker service. Put the following in the service yml to enable the plugin and configure the extension:

```
spinnaker:
  extensibility:
    plugins:
      Armory.RandomWaitPlugin:
        enabled: true
        extensions:
          armory.randomWaitStage:
            enabled: true
            config:
              defaultMaxWaitTime: 60
```

Or use the [examplePluginRepository](https://github.com/spinnaker-plugin-examples/examplePluginRepository) to avoid copying the plugin `.zip` artifact.

# Debugging

To debug the `random-wait-orca`  server component inside a Spinnaker service (like Orca) using IntelliJ Idea follow these steps:

1) Run `./gradlew releaseBundle` in the plugin project.
2) Copy the generated `.plugin-ref` file under `build` in the plugin project submodule for the service to the `plugins` directory under root in the Spinnaker service that will use the plugin .
3) Link the plugin project to the service project in IntelliJ (from the service project use the `+` button in the Gradle tab and select the plugin build.gradle).
4) Configure the Spinnaker service the same way specified above.
5) Create a new IntelliJ run configuration for the service that has the VM option `-Dpf4j.mode=development` and does a `Build Project` before launch.
6) Debug away...

# Videos

* [Intro to Spinnaker Plugins](https://youtu.be/HtkXeC8a38Y), 2020 Spinnaker Gardening Days
* [How to build a PLUGIN: Building the frontend for a Spinnaker-native custom stage](https://youtu.be/u9NVlG58NYo)
* [How to build a PLUGIN: Creating a Spinnaker-native custom stage](https://youtu.be/b7BmMY1kR10)
* [Backend Plugin Development](https://drive.google.com/open?id=1JPkXG5NnXowb1OElAFj2VjnpvUDA-Wyi)
* [How to build a PLUGIN: The build process for a Spinnaker plugin](https://youtu.be/-AIOXdgvNqs)
* [How to build a PLUGIN: Delivering a plugin to your Spinnaker environment](https://youtu.be/G2eyc9gzNS0)

# Architecture

## `random-wait-orca`

This component implements the [SimpleStage](https://github.com/spinnaker/orca/blob/master/orca-api/src/main/java/com/netflix/spinnaker/orca/api/simplestage/SimpleStage.java) PF4J extension point in Orca and  consists of five classes in the `io.armory.plugin.state.wait.random` package:

* `Context.kt`: a data class that stores the `maxWaitTime` value; `SimpleStage` uses `Context`
* `Output.kt`: a data class that stores the `timeToWait` getValue; this data is returned to the extension point implementation and can be used in downstream stages
* `RandomWaitConfig.kt`: a data class with the `@ExtensionConfiguration` tag; key-value pairs in this class map to the plugin's configuration
* `RandomWaitInput.kt`: a data class that contains the key-value pairs that we care about from the Context map
* `RandomWaitPlugin.kt`: this is the plugin's main class; implements `SimpleStage`

When adding a stage to a pipeline in the Spinnaker UI, the user can select this `Armory.RandomWaitPlugin` stage from the **Type** dropdown list. You enter a `maxWaitTime`, which is deserialized in `RandomWaitInput`.

Watch [How to build a PLUGIN: Creating a Spinnaker-native custom stage](https://youtu.be/b7BmMY1kR10) and read [code comments](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/tree/master/random-wait-orca/src/main/kotlin/io/armory/plugin/stage/wait/random) for more information.

## `random-wait-deck`

Prior to v1.1.4, this component used the [`rollup.js`](https://rollupjs.org/guide/en/#plugins-overview) plugin library to create a UI widget for Deck.

* `rollup.config.js`: configuration for building the JavaScript application
* `package.json`: defines dependencies
* `RandomWaitStage.tsx`: defines the custom pipeline stage; renders UI output
* `RandomWaitStageIndex.ts`: exports the name and custom stages

The code was refactored in v1.1.5 to use the new Deck UI SDK. `rollup.config.js`
now points to the config defined by the UI SDK. It's mostly not necessary to
define your own build config. This is also true of `tsconfig.json`. If you use
the UI SDK, you no longer define how your TypeScript should be compiled.
