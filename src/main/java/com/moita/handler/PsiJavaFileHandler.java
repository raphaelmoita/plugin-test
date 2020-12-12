package com.moita.handler;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.moita.visitor.ClassStructureVisitor;

import java.util.List;

public class PsiJavaFileHandler {

    private ClassStructureVisitor visitor;
    private Project project;

    public PsiJavaFileHandler(Project project, PsiFile psiFile)
    {
        this.project = project;
        this.visitor = new ClassStructureVisitor();

        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        psiJavaFile.accept(visitor);
    }

    public List<PsiField> getPsiFields() {
        return visitor.getPsiFields();
    }

    public List<PsiClass> getPsiInterfaces() {
        return visitor.getPsiInterfaces();
    }

    public List<PsiMethod> getPsiMethods() {
        return visitor.getPsiMethods();
    }

    public String getClassName() {
        return getPackageName() + "." + getClassSimpleName();
    }

    public String getClassSimpleName() {
        return visitor.getClassName();
    }

    public String getPackageName() {
        return visitor.getPackageName();
    }
}
