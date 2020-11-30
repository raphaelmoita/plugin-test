package com.moita.visitor;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.moita.util.PsiUtil.findFile;
import static com.moita.util.PsiUtil.nonClassTypeObject;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class ClassStructureVisitor extends JavaRecursiveElementVisitor {
    private final List<PsiClass> psiInterfaces = new ArrayList<>();
    private final List<PsiMethod> psiMethods = new ArrayList<>();
    private final List<PsiField> psiFields = new ArrayList<>();

    private String className;
    private String packageName;
    private List<String> annotationValue;

    @Override
    public void visitJavaFile(PsiJavaFile file) {
        packageName = file.getPackageStatement().getPackageName();
        super.visitJavaFile(file);
    }

    private List<String> getValuesFromAnnotation(PsiAnnotation annotation) {
        String annotationQualifiedName = annotation.getQualifiedName();
        if (nonNull(annotationQualifiedName)) {
            PsiAnnotationMemberValue annotationMemberValue = annotation.findAttributeValue("values");
            String text = annotationMemberValue.getText();
            String values = text.substring(1, text.length() - 1);
            return commaSeparatedToList(values);
        }
        return new ArrayList<>();
    }

    private List<String> commaSeparatedToList(String commaSeparated) {
        return Stream.of(commaSeparated.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

    }

    @Override
    public void visitClass(PsiClass aClass) {

        PsiModifierList psiModifierList = aClass.getModifierList();
        PsiAnnotation[] annotations = psiModifierList.getAnnotations();

        for (PsiAnnotation psiAnnotation : annotations) {
            List<String> values = getValuesFromAnnotation(annotations[0]);

            List<PsiFile> psiFiles = values.stream()
                    .map(file -> file.substring(0, file.indexOf(".")) + ".java")
                    .map(file -> findFile(aClass.getProject(), file))
                    .collect(Collectors.toList());
        }

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

    public String getPackageName() {
        return packageName;
    }
}

