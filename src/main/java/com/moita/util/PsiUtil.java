package com.moita.util;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.List;

public class PsiUtil {

    public static final String OBJECT = "Object";
    public static final String SERIALIZABLE = "Serializable";

    public static PsiFile findFile(Project project, String fileName)
    {
        return Arrays.stream(FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.projectScope(project)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(fileName + " not found!"));
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

    public static PsiDirectory findDirectoryFromActionEvent(AnActionEvent actionEvent) {

        DataContext dataContext = actionEvent.getDataContext();
        IdeView data = LangDataKeys.IDE_VIEW.getData(dataContext);

        if (data == null) {
            return null;
        }

        return data.getOrChooseDirectory();
    }

    public static PsiDirectory findRootDirectory(AnActionEvent actionEvent) {

        PsiDirectory currentDirectory = findDirectoryFromActionEvent(actionEvent);
        while (!"plugin-test-playground".equals(currentDirectory.getName())) {

            PsiDirectory parentDirectory = currentDirectory.getParentDirectory();
            if (parentDirectory == null)
            {
                return null;
            }
            currentDirectory = parentDirectory;
        }
        return currentDirectory;
    }
}
