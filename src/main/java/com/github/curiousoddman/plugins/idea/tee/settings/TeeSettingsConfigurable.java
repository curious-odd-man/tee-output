package com.github.curiousoddman.plugins.idea.tee.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TeeSettingsConfigurable implements Configurable {

    private TeeSettingsComponent aTeeSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Tee-Output: Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return aTeeSettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        aTeeSettingsComponent = new TeeSettingsComponent();
        return aTeeSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        TeeSettingsState settings = TeeSettingsState.getInstance();
        boolean modified = !aTeeSettingsComponent.getLogsOutputDir()
                                                 .equals(settings.aLogsOutputDir);
        return modified;
    }

    @Override
    public void apply() {
        TeeSettingsState settings = TeeSettingsState.getInstance();
        settings.aLogsOutputDir = aTeeSettingsComponent.getLogsOutputDir();
    }

    @Override
    public void reset() {
        TeeSettingsState settings = TeeSettingsState.getInstance();
        aTeeSettingsComponent.setLogsOutputDir(settings.aLogsOutputDir);
    }

    @Override
    public void disposeUIResources() {
        aTeeSettingsComponent = null;
    }
}
