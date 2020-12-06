package com.moita.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PluginDialog extends DialogWrapper
{
    private static final String TITLE = "Definition Files Generator";

    private JPanel contentPane;
    private JCheckBox rawCheckBox;
    private JCheckBox silverSchemaCheckBox;
    private JCheckBox goldenSchemaCheckBox;
    private JLabel icpmDomainClassNameLabel;

    public PluginDialog(Project project, String className)
    {
        super(project);
        this.icpmDomainClassNameLabel.setText(className);
        init();
        setTitle(TITLE);
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{new GenerateAction(), getCancelAction()};
    }

    @Override
    protected JComponent createCenterPanel()
    {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Definition files generator");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);

        return contentPane;
    }

    public boolean isRawCheckBoxSelected()
    {
        return rawCheckBox.isSelected();
    }

    public boolean isSilverSchemaCheckBoxSelected()
    {
        return silverSchemaCheckBox.isSelected();
    }

    public boolean isGoldenSchemaCheckBoxSelected()
    {
        return goldenSchemaCheckBox.isSelected();
    }

    private class GenerateAction extends DialogWrapperAction
    {
        protected GenerateAction()
        {
            super("Generate");
            putValue(Action.NAME, "Generate");
        }

        @Override
        protected void doAction(ActionEvent e) {
            doOKAction();
        }
    }
}
