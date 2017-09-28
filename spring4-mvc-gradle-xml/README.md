# Development

## Start Tomcat server in debug mode

Execute this command to build and run `sample` module: \
`./gradlew :sample:tomcatStartDebug --info`

Switch `--info` prints some info log.
If everything is OK, you see this output at the end:\
`Listening for transport dt_socket at address: 5005`

## URL

Here is the index URL for `sample` module:\
`http://localhost:8080/sample`

## Attach Intellij IDEA debugger

### Create the Run configuration

Execute the following steps at the first time.

1. `Edit configurations...`
0. `+` button
0. `Remote`
0. Host: `localhost`, port: `5005`
0. Name: `Debug server`

### Use the Run configuration

You can attach the debugger any time, just execute the following steps.

1. Start the server in debug mode.
0. Select `Debug server` from the list.
0. Press the debug button.

## Server log

Log files can be found in the `~/logs` directory.\
Example for `sample` module: `~/logs/sample.log`

NOTE: Character `~` means the user home.

# Release

Execute:\
`./gradlew buildAllProducts`

That's all. You can find the outputs in each module.\
Example for `sample` module: `modules/sample/build/output/sample`

The WAR files are in the `webapps` directory.

# More info

[Gretty](http://akhikhl.github.io/gretty-doc/index.html) is the gradle plugin we use.
