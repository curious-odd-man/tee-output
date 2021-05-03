package com.github.curiousoddman.plugins.idea.tee;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;

public class TeeProcessOutputSaver extends ProcessAdapter {
    private static final Logger log = Logger.getInstance(TeeExecutionListener.class);

    private final Project        aProject;
    private final BufferedWriter aBufferedWriter;

    public TeeProcessOutputSaver(Project project) throws IOException {
        aProject = project;
        aBufferedWriter = Files.newBufferedWriter(OutputFileNameCreator.createFileName(project));
    }

    @Override
    public void startNotified(@NotNull ProcessEvent event) {
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        try {
            aBufferedWriter.flush();
        } catch (IOException e) {
            log.error("Failed to flush output", e);
        }

        try {
            aBufferedWriter.close();
        } catch (IOException e) {
            log.error("Failed to close file", e);
        }
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        try {
            aBufferedWriter.append(event.getText());
        } catch (IOException e) {
            log.error("Failed to write to file", e);
        }
    }
}
