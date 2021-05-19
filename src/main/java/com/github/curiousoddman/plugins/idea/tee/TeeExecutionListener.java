package com.github.curiousoddman.plugins.idea.tee;

import com.github.curiousoddman.plugins.idea.tee.context.TeeDataContext;
import com.github.curiousoddman.plugins.idea.tee.macro.RunConfigNameMacro;
import com.github.curiousoddman.plugins.idea.tee.output.GradleSaver;
import com.github.curiousoddman.plugins.idea.tee.output.TeeProcessOutputSaver;
import com.intellij.execution.ExecutionListener;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TeeExecutionListener implements ExecutionListener {
    private static final Logger log = Logger.getInstance(TeeExecutionListener.class);

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        DataContext dataContext = getDataContext(env);
        Map<DataKey<?>, Object> additionalProperties = new HashMap<>();
        additionalProperties.put(RunConfigNameMacro.RUN_CONFIGURATION_BASE, env.getRunProfile());

        TeeProcessOutputSaver teeProcessOutputSaver = new TeeProcessOutputSaver(env.getProject(), new TeeDataContext(dataContext, additionalProperties));
        handler.addProcessListener(teeProcessOutputSaver);
        env.getProject()
           .getMessageBus()
           .connect()
           .subscribe(SMTRunnerEventsListener.TEST_STATUS, new GradleSaver(teeProcessOutputSaver));
    }

    @Nullable
    private static DataContext getDataContext(@NotNull ExecutionEnvironment env) {
        DataContext dataContext = env.getDataContext();
        if (dataContext == null) {
            log.info("No data context present.. trying to figure it out.");
            try {
                dataContext = DataManager.getInstance()
                                         .getDataContextFromFocusAsync()
                                         .blockingGet(30, TimeUnit.SECONDS);
            } catch (TimeoutException | ExecutionException e) {
                log.warn(e);
                dataContext = null;
            }
        }
        return dataContext;
    }
}
