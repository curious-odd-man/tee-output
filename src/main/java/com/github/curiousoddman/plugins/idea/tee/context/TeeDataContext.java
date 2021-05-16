package com.github.curiousoddman.plugins.idea.tee.context;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TeeDataContext implements DataContext {
    private final DataContext         delegate;
    private final Map<String, Object> myData;

    public TeeDataContext(DataContext delegate, Map<DataKey<?>, Object> data) {
        this.delegate = delegate;
        this.myData = new HashMap<>();
        data.forEach((k, v) -> myData.put(k.getName(), v));
    }

    @Override
    public @Nullable Object getData(@NotNull String dataId) {
        Object data = delegate.getData(dataId);
        if (data == null) {
            return myData.get(dataId);
        }
        return data;
    }

    @Override
    public <T> @Nullable T getData(@NotNull DataKey<T> key) {
        return (T) getData(key.getName());
    }
}
