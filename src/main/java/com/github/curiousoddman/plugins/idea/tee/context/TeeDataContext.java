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

    public TeeDataContext(@Nullable DataContext delegate, Map<DataKey<?>, Object> data) {
        this.delegate = delegate == null ? new DummyDataContext() : delegate;
        this.myData = new HashMap<>();
        data.forEach((k, v) -> myData.put(k.getName(), v));
    }

    @Override
    public <T> @Nullable T getData(@NotNull DataKey<T> key) {
        Object data = delegate.getData(key);
        if (data == null) {
            return (T) myData.get(key.getName());
        }
        return (T) data;
    }


    @Override
    public @Nullable Object getData(@NotNull String dataId) {
        DataKey<Object> key = DataKey.create(dataId);
        return getData(key);
    }

    static class DummyDataContext implements DataContext {
        @Override
        public @Nullable Object getData(@NotNull String dataId) {
            return null;
        }
    }
}
