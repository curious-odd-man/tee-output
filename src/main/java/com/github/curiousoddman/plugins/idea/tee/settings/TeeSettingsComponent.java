package com.github.curiousoddman.plugins.idea.tee.settings;

import com.intellij.ide.DataManager;
import com.intellij.ide.macro.MacrosDialog;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

public class TeeSettingsComponent {
    private static final Logger                        log                    = Logger.getInstance(TeeSettingsComponent.class);
    private static final Map<String, Supplier<String>> MACRO_VALUES_SUPPLIERS = new HashMap<>();
    private static final DateTimeFormatter             TIMESTAMP_FORMAT       = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    static {
        MACRO_VALUES_SUPPLIERS.put("Timestamp", () -> LocalDateTime.now()
                                                                   .format(TIMESTAMP_FORMAT));
    }

    private final JPanel                    aPanel;
    private final TextFieldWithBrowseButton aLogsOutputDir = new TextFieldWithBrowseButton();
    private final Project                   aProject;

    public TeeSettingsComponent() {
        DataContext dataContext = DataManager.getInstance()
                                             .getDataContext(aLogsOutputDir);
        aProject = CommonDataKeys.PROJECT.getData(dataContext);
        Map<String, String> customExtensions = MACRO_VALUES_SUPPLIERS.entrySet()
                                                                     .stream()
                                                                     .collect(toMap(
                                                                             Map.Entry::getKey,
                                                                             entry -> entry.getValue()
                                                                                           .get()
                                                                     ));
        MacrosDialog.addTextFieldExtension((ExtendableTextField) aLogsOutputDir.getTextField(), MacrosDialog.Filters.ALL, customExtensions);
        addWorkingDirectoryBrowseAction(aLogsOutputDir);
        aPanel = FormBuilder.createFormBuilder()
                            .addLabeledComponent(new JBLabel("Log output path: "), aLogsOutputDir, 1, false)
                            .addComponentFillVertically(new JPanel(), 0)
                            .getPanel();
    }

    public JPanel getPanel() {
        return aPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return aLogsOutputDir;
    }

    @NotNull
    public String getLogsOutputDir() {
        return aLogsOutputDir.getText();
    }

    public void setLogsOutputDir(@NotNull String newText) {
        aLogsOutputDir.setText(newText);
    }

    protected final void addWorkingDirectoryBrowseAction(@NotNull final TextFieldWithBrowseButton workingDirField) {
        workingDirField.addBrowseFolderListener(null, null, aProject, FileChooserDescriptorFactory.createSingleFolderDescriptor());
    }
}
