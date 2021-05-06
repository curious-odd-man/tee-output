package com.github.curiousoddman.plugins.idea.tee;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.ide.macro.Macro;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class TeeProcessOutputSaver extends ProcessAdapter {
    private static final Logger log = Logger.getInstance(TeeExecutionListener.class);

    private final Project aProject;

    private BufferedWriter aBufferedWriter;

    public TeeProcessOutputSaver(Project project) {
        aProject = project;
        BufferedWriter bufferedWriter = null;
        try {
            Path fileName = OutputFileNameCreator.createFileName(aProject);
            Files.createDirectories(fileName.getParent());
            bufferedWriter = Files.newBufferedWriter(fileName);
        } catch (ExecutionException | TimeoutException | Macro.ExecutionCancelledException | IOException e) {
            log.error("Failed to start writing to file:", e);
        }
        aBufferedWriter = bufferedWriter;
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        if (aBufferedWriter == null) {
            return;
        }

        BufferedWriter bufferedWriter = aBufferedWriter;
        aBufferedWriter = null;

        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("Failed to flush output", e);
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("Failed to close file", e);
        }
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        if (aBufferedWriter == null) {
            log.warn("Text ignored as writer is not available. " + event.getText());
            return;
        }

        try {
            aBufferedWriter.append(event.getText());
        } catch (IOException e) {
            log.error("Failed to write to file", e);
        }
    }
}
