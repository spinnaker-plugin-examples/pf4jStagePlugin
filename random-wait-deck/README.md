# UI Plugin Development

## Generating a new project

This directory was generated using the scaffolding tool provided by
`@spinnaker/pluginsdk`. The scaffolding generates the project structure
expected by the plugin framework and Spinnaker's UI.

To generate your own UI plugin, run `npx -p @spinnaker/pluginsdk scaffold`.

## Plugin development

You can build your UI plugin by running `yarn build` or `yarn watch` from this
directory.

You can run `DEV_PROXY_HOST=<spinnaker-address> yarn proxy` 
from this directory to load your plugin into an existing Spinnaker installation. 
The proxy will auto-reload the browser when you re-build your plugin.

The proxy will not work with auth-protected Spinnaker installations:
the proxy's address (`localhost:9000`) is not a valid redirect URL. You can fix
this by re-configuring Gate to accept the address, but this probably isn't a safe thing to do.

