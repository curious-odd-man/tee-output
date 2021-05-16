package com.github.curiousoddman.plugins.idea.tee.macro;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.ide.macro.Macro;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunConfigNameMacro extends Macro {
    private static final Logger log = Logger.getInstance(RunConfigNameMacro.class);

    public static final DataKey<RunProfile> RUN_CONFIGURATION_BASE = DataKey.create("tee-output.run-configuration-base");

    @Override
    @NonNls
    public @NotNull String getName() {
        return "RunConfigurationName";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDescription() {
        return "Name of current run configuration.";
    }

    @Override
    public @Nullable String expand(@NotNull DataContext dataContext) {
        RunProfile data = dataContext.getData(RUN_CONFIGURATION_BASE);
        if (data != null) {
            return data.getName();
        }
        return "UnknownRunConfigurationName";
    }
}