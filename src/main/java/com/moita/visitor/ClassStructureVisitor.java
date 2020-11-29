package com.moita.visitor;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

import static com.moita.util.PsiUtil.nonClassTypeObject;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class ClassStructureVisitor extends JavaRecursiveElementVisitor {
    private final List<PsiClass> psiInterfaces = new ArrayList<>();
    private final List<PsiMethod> psiMethods = new ArrayList<>();
    private final List<PsiField> psiFields = new ArrayList<>();

    private String className;

    @Override
    public void visitClass(PsiClass aClass) {

        if (className == null) {
            className = aClass.getName();
        }

        PsiClass superClass = aClass.getSuperClass();

        if (nonNull(superClass) && nonClassTypeObject(aClass)) {
            visitClass(superClass);
            psiInterfaces.addAll(asList(aClass.getInterfaces()));
            psiMethods.addAll(asList(aClass.getMethods()));
            psiFields.addAll(asList(aClass.getFields()));
        }

        super.visitClass(aClass);
    }

    public List<PsiMethod> getPsiMethods() {
        return psiMethods;
    }

    public List<PsiField> getPsiFields() {
        return psiFields;
    }

    public List<PsiClass> getPsiInterfaces() {
        return psiInterfaces;
    }

    public String getClassName() {
        return className;
    }
}

