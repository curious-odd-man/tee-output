package com.github.curiousoddman.plugins.idea.tee;

import com.intellij.execution.ExecutionListener;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TeeExecutionListener implements ExecutionListener {
    private static final Logger log = Logger.getInstance(TeeExecutionListener.class);

    private Project aProject;

    public TeeExecutionListener(Project project) {
        aProject = project;
    }

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        handler.addProcessListener(new TeeProcessOutputSaver(aProject));
    }
}
