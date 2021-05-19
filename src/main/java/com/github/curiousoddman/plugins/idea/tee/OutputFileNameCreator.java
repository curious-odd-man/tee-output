package com.github.curiousoddman.plugins.idea.tee;

import com.github.curiousoddman.plugins.idea.tee.macro.TimestampMacro;
import com.github.curiousoddman.plugins.idea.tee.settings.TeeSettingsState;
import com.intellij.ide.macro.Macro;
import com.intellij.ide.macro.MacroManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public final class OutputFileNameCreator {
    private static final Pattern UNSUPPORTED_FILENAME_CHARS = Pattern.compile("[:\"*?<>|]");

    private static final Logger log = Logger.getInstance(OutputFileNameCreator.class);

    private OutputFileNameCreator() {
    }

    public static Path createFileName(Project project, DataContext dataContext) throws ExecutionException, TimeoutException, Macro.ExecutionCancelledException {
        log.debug("Creating file name..");
        String rawPath = TeeSettingsState.getInstance().aLogsOutputDir;
        String replacedMacroPath = MacroManager.getInstance()
                                               .expandMacrosInString(rawPath, true, dataContext);

        if (replacedMacroPath == null) {
            log.info("Could not replace all macros. Falling back to project path logs directory.");
            String root = project.getBasePath() == null ? "" : project.getBasePath();
            return Paths.get(root, "logs", replaceUnsupportedCharacters(project.getName()) + '-' + TimestampMacro.getTimestamp() + ".log");
        } else {
            String allowedCharsPath = replaceUnsupportedCharacters(replacedMacroPath);
            log.info("Using path '" + allowedCharsPath + '\'');
            if (replacedMacroPath.lastIndexOf('.') == -1) {
                return Paths.get(allowedCharsPath + ".log");
            } else {
                return Paths.get(allowedCharsPath);
            }
        }
    }

    static String replaceUnsupportedCharacters(String path) {
        return UNSUPPORTED_FILENAME_CHARS.matcher(path)
                                         .replaceAll("_");
    }
}

