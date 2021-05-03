package com.github.curiousoddman.plugins.idea.tee;

import com.intellij.application.options.PathMacrosImpl;
import com.intellij.ide.macro.Macro;
import com.intellij.ide.macro.MacroManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputFileNameCreator {

    private static final Logger log = Logger.getInstance(OutputFileNameCreator.class);

    public static Path createFileName(Project project) {
        for (final Macro macro : MacroManager.getInstance()
                                             .getMacros()) {
            log.info("Vlad Macro: " + macro.getName());
        }
        return Paths.get(project.getBasePath(), "output" + project.getName() + ".log");
    }
}

