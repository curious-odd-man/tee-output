package com.github.curiousoddman.plugins.idea.tee.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "com.github.curiousoddman.plugins.idea.tee.settings.TeeSettingsState",
        storages = {@Storage("TeeOutputSettingsPlugin.xml")}
)
public class TeeSettingsState implements PersistentStateComponent<TeeSettingsState> {
    public String aLogsOutputDir = "";

    public static TeeSettingsState getInstance() {
        return ServiceManager.getService(TeeSettingsState.class);
    }

    @Nullable
    @Override
    public TeeSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TeeSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
