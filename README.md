# Overview

This is a tiny Intellij plugin that will put all output to a text file of your choosing.

NOTE: The plugin is at the start of the development. Please feel free to add issues/feature requests.

# Link to plugin page

https://plugins.jetbrains.com/plugin/16728-tee-output


# Hints

## A way to create popup notification

```
new Notification("Error Report", processHandler.toString(), text, NotificationType.ERROR).notify(aProject);
```
