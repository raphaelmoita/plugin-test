package com.moita.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.moita.dialog.PluginDialog;
import com.moita.visitor.ClassStructureVisitor;
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
        PsiJavaFile psiJavaFile = (PsiJavaFile) anActionEvent.getData(CommonDataKeys.PSI_FILE);
        ClassStructureVisitor visitor = new ClassStructureVisitor();

        psiJavaFile.accept(visitor);

        showDialog(anActionEvent.getProject(), visitor.getPackageName() + "." + visitor.getClassName());

        int answer = Messages.showIdeaMessageDialog(anActionEvent.getProject(),
                "Which schemas would you like to generate?",
                "Java to Definition file",
                new String[] { "Gold", "Silver", "Both", "Cancel"},
                0,
                Messages.getQuestionIcon(),null);

        if (answer == 3) {
            return;
        }

        if (isSerializable(visitor.getPsiInterfaces())) {
            Messages.showMessageDialog(anActionEvent.getProject(),
                    visitor.getPsiMethods().toString() + visitor.getPsiFields().toString(),
                    "Class members", Messages.getInformationIcon());
        } else {
            Messages.showErrorDialog("Class is not serializable",visitor.getClassName() + " structure");
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