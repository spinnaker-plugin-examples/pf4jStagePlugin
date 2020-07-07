![CI](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/CI/badge.svg)
![Latest Kork](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/Latest%20Kork/badge.svg?branch=master)
![Latest Orca](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/Latest%20Orca/badge.svg?branch=master)

Spinnaker Plugin (PF4J-based) that is a custom pipeline stage. 
The [pf4jStagePlugin](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin) creates a custom pipeline stage that waits a random number of seconds before signaling success. 
This plugin implements the [SimpleStage](https://github.com/spinnaker/orca/blob/ab89a0d7f847205ccd62e70f8a714040a8621ee7/orca-api/src/main/java/com/netflix/spinnaker/orca/api/SimpleStage.java) PF4J extension point in Orca. 
The plugin consists of a `random-wait-orca` [Kotlin](https://kotlinlang.org/docs/reference/) server component and a `random-wait-deck` [React](https://reactjs.org/) UI component that uses the [rollup.js](https://rollupjs.org/guide/en/#plugins-overview) plugin library.

This is for demo only and not meant to be used in a production environment.

# Version Compatibility
 
| Plugin  | Spinnaker Platform |
|:----------- | :--------- |
| 1.0.x  |  1.19.x |
| 1.1.14  | 1.20.6, 1.21.0 |

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

# Video walkthroughs

* `Plugging into the UI` [video walkthrough](https://youtu.be/u9NVlG58NYo)
* `Plugging into Spinnaker Services` [video walkthrough](https://drive.google.com/open?id=1JPkXG5NnXowb1OElAFj2VjnpvUDA-Wyi)
* `Plugin Build and Release` [video walkthrough](https://drive.google.com/file/d/16DIo812nRyan2CDCTuZvsgN4D9yl0Dur/view?usp=sharing)
* `Plugin Delivery` [video walkthrough](https://drive.google.com/file/d/1k-MUgmwWFdh6YiozmFw5Y2hGSm84UeTw/view?usp=sharing)
