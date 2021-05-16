package com.github.curiousoddman.plugins.idea.tee.output;

import com.github.curiousoddman.plugins.idea.tee.OutputFileNameCreator;
import com.github.curiousoddman.plugins.idea.tee.TeeExecutionListener;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.ide.macro.Macro;
import com.intellij.openapi.actionSystem.DataContext;
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

    public TeeProcessOutputSaver(Project project, DataContext dataContext) {
        aProject = project;
        BufferedWriter bufferedWriter = null;
        try {
            Path fileName = OutputFileNameCreator.createFileName(aProject, dataContext);
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

    public void onTextAvailable(@NotNull String text) {
        if (aBufferedWriter == null) {
            log.warn("Text ignored as writer is not available. " + text);
            return;
        }

        try {
            aBufferedWriter.append(text);
        } catch (IOException e) {
            log.error("Failed to write to file", e);
        }
    }

    public void onTextAvailable(@NotNull String text, @NotNull Key outputType) {
        onTextAvailable(text);
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        onTextAvailable(event.getText(), outputType);
    }
}
