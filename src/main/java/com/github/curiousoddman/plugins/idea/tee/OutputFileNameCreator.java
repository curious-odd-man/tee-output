package com.github.curiousoddman.plugins.idea.tee;

import com.github.curiousoddman.plugins.idea.tee.macro.TimestampMacro;
import com.github.curiousoddman.plugins.idea.tee.settings.TeeSettingsState;
import com.intellij.ide.macro.Macro;
import com.intellij.ide.macro.MacroManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.nio.file.InvalidPathException;
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
            String root = project.getBasePath() == null ? "." : project.getBasePath();
            String potentiallyInvalidPath = root
                    + File.pathSeparator + "logs"
                    + File.pathSeparator + project.getName() + '-' + TimestampMacro.getTimestamp() + ".log";
            String goodPath = ensurePathValid(potentiallyInvalidPath);
            return Paths.get(goodPath);
        } else {
            String path = ensurePathValid(replacedMacroPath);
            Path absolutePath = Paths.get(path)
                                     .toAbsolutePath()
                                     .normalize();
            log.info("Using path '" + absolutePath + '\'');
            return absolutePath;
        }
    }

    static String ensurePathValid(String path) {
        StringBuilder sb = new StringBuilder(path);
        do {
            int invalidCharPosition = getInvalidCharPosition(sb.toString());
            if (invalidCharPosition == -2) {
                return sb.toString();
            } else if (invalidCharPosition == -1) {
                return ".";
            } else {
                sb.setCharAt(invalidCharPosition, '_');
            }
        } while (true);
    }

    static int getInvalidCharPosition(String path) {
        try {
            Paths.get(path);
            return -2;
        } catch (InvalidPathException e) {
            return e.getIndex();
        }
    }
}

