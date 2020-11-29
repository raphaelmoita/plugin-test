package com.moita.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

public class PsiUtil {

    public static final String OBJECT = "Object";
    public static final String SERIALIZABLE = "Serializable";

    public static PsiFile[] findFile(AnActionEvent event, String fileName)
    {
        return FilenameIndex.getFilesByName(event.getProject(), fileName,
                GlobalSearchScope.allScope(event.getProject()));
    }

    public static boolean isClassTypeObject(PsiClass aClass)
    {
        return OBJECT.equals(aClass.getName());
    }

    public static boolean nonClassTypeObject(PsiClass aClass)
    {
        return !isClassTypeObject(aClass);
    }

    public static boolean isSerializable(List<PsiClass> interfaces)
    {
        return interfaces.stream()
                .anyMatch(i -> SERIALIZABLE.equals(i.getName()));
    }
}