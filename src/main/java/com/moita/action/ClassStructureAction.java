package com.moita.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.moita.dialog.PluginDialog;
import com.moita.handler.PsiJavaFileHandler;
import com.moita.visitor.PluginActivateVisitor;

import static com.moita.util.PsiUtil.isSerializable;

public class ClassStructureAction extends AnAction {

    private void showDialog(Project project, String className ) {
        PluginDialog sdw = new PluginDialog(project, className);
        sdw.show();
        int exitCode = sdw.getExitCode();
        System.out.println("exitCode: " + exitCode);
        System.out.println("controlset: " + sdw.isRawCheckBoxSelected());
        System.out.println("curated: " + sdw.isSilverSchemaCheckBoxSelected());
        System.out.println("governed: " + sdw.isGoldenSchemaCheckBoxSelected());
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        PsiJavaFileHandler psiJavaFileHandler = new PsiJavaFileHandler(anActionEvent.getProject(),
                anActionEvent.getData(CommonDataKeys.PSI_FILE));

        showDialog(anActionEvent.getProject(), psiJavaFileHandler.getClassName());

        int answer = Messages.showIdeaMessageDialog(anActionEvent.getProject(),
                "Which schemas would you like to generate?",
                "Java to Definition file",
                new String[] { "Gold", "Silver", "Both", "Cancel"},
                0,
                Messages.getQuestionIcon(),null);

        if (answer == 3) {
            return;
        }

        if (isSerializable(psiJavaFileHandler.getPsiInterfaces())) {
            Messages.showMessageDialog(anActionEvent.getProject(),
                    psiJavaFileHandler.getPsiMethods().toString() + psiJavaFileHandler.getPsiFields().toString(),
                    "Class members", Messages.getInformationIcon());
        } else {
            Messages.showErrorDialog("Class is not serializable",psiJavaFileHandler.getClassName() + " structure");
        }
    }

    @Override
    public void update(AnActionEvent e) {
        PluginActivateVisitor pluginActivateVisitor = new PluginActivateVisitor(e);

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        psiFile.accept(pluginActivateVisitor);

        e.getPresentation().setEnabled(pluginActivateVisitor.isEnable());
        e.getPresentation().setVisible(pluginActivateVisitor.isEnable());
    }
}