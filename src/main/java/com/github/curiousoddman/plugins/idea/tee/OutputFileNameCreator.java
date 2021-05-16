package com.github.curiousoddman.plugins.idea.tee;

import com.github.curiousoddman.plugins.idea.tee.macro.TimestampMacro;
import com.github.curiousoddman.plugins.idea.tee.settings.TeeSettingsState;
import com.intellij.ide.DataManager;
import com.intellij.ide.macro.Macro;
import com.intellij.ide.macro.MacroManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OutputFileNameCreator {

    private static final Logger log = Logger.getInstance(OutputFileNameCreator.class);

    public static Path createFileName(Project project, DataContext dataContext) throws ExecutionException, TimeoutException, Macro.ExecutionCancelledException {
        String rawPath = TeeSettingsState.getInstance().aLogsOutputDir;
        String replacedMacroPath = MacroManager.getInstance()
                                               .expandMacrosInString(rawPath, true, dataContext);

        if (replacedMacroPath == null) {
            String root = project.getBasePath() == null ? "" : project.getBasePath();
            return Paths.get(root, "logs", project.getName() + '-' + TimestampMacro.getTimestamp() + ".log");
        }

        if (replacedMacroPath.lastIndexOf('.') == -1) {
            return Paths.get(replacedMacroPath + ".log");
        }
        return Paths.get(replacedMacroPath);
    }
}

