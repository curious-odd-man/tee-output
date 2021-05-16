package com.github.curiousoddman.plugins.idea.tee;

import com.github.curiousoddman.plugins.idea.tee.output.GradleSaver;
import com.github.curiousoddman.plugins.idea.tee.output.TeeProcessOutputSaver;
import com.intellij.execution.ExecutionListener;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class TeeExecutionListener implements ExecutionListener {
    private static final Logger log = Logger.getInstance(TeeExecutionListener.class);

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        TeeProcessOutputSaver teeProcessOutputSaver = new TeeProcessOutputSaver(env.getProject());
        handler.addProcessListener(teeProcessOutputSaver);
        env.getProject()
           .getMessageBus()
           .connect()
           .subscribe(SMTRunnerEventsListener.TEST_STATUS, new GradleSaver(teeProcessOutputSaver));
    }
}
