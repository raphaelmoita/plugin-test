package com.moita.visitor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;

public class PluginActivateVisitor extends JavaRecursiveElementVisitor {
    private final AnActionEvent event;
    private boolean enable;

    public PluginActivateVisitor(AnActionEvent event) {
        this.event = event;
    }

    @Override
    public void visitClass(PsiClass aClass) {

        boolean interfaceType = aClass.isInterface();
        boolean enumType = aClass.isEnum();
        boolean annotationType = aClass.isAnnotationType();

        enable = !interfaceType && !enumType && !annotationType;
    }

    public boolean isEnable() {
        return enable;
    }
}

