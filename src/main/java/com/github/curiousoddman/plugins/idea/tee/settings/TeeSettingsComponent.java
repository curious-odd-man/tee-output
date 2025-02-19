package com.github.curiousoddman.plugins.idea.tee.settings;

import com.intellij.ide.DataManager;
import com.intellij.ide.macro.MacrosDialog;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TeeSettingsComponent {
    private final JPanel                    aPanel;
    private final TextFieldWithBrowseButton aLogsOutputDir = new TextFieldWithBrowseButton();
    private final Project                   aProject;

    public TeeSettingsComponent() {
        DataContext dataContext = DataManager.getInstance()
                                             .getDataContext(aLogsOutputDir);
        aProject = CommonDataKeys.PROJECT.getData(dataContext);
        MacrosDialog.addTextFieldExtension((ExtendableTextField) aLogsOutputDir.getTextField());
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
        workingDirField.addBrowseFolderListener(aProject, FileChooserDescriptorFactory.createSingleFolderDescriptor());
    }
}
