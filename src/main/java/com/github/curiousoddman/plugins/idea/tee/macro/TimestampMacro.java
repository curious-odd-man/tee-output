package com.github.curiousoddman.plugins.idea.tee.macro;

import com.intellij.ide.macro.Macro;
import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampMacro extends Macro {
    @Override
    public @NonNls @NotNull
    String getName() {
        return "Timestamp";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDescription() {
        return "Current timestamp in default format";
    }

    @Override
    public @Nullable String expand(@NotNull DataContext dataContext) {
        return getTimestamp();
    }

    public static String getTimestamp() {
        return LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss-SSSS"));
    }
}