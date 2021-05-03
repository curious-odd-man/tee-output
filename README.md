https://github.com/JetBrains/intellij-sdk-code-samples

https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/max_opened_projects

https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/settings

https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/run_configuration

# Hints

## A way to create popup notification

```
new Notification("Error Report", processHandler.toString(), text, NotificationType.ERROR).notify(aProject);
```