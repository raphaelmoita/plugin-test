package com.moita.action;

import com.intellij.json.json5.Json5FileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.moita.util.PsiUtil;

import java.io.IOException;
import java.util.stream.Stream;

public class CreateAndOpenJsonAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        try {

            final PsiFileFactory factory = PsiFileFactory.getInstance(project);
            PsiFile file = factory.createFileFromText("test.json",
                    Json5FileType.INSTANCE, "{ \"id\" : \"123456789\" }");

            PsiDirectory rootDirectory1 = PsiUtil.findRootDirectory(anActionEvent);

            PsiDirectory commonsModuleDir = Stream.of(rootDirectory1.getChildren())
                    .filter(d -> d instanceof PsiDirectory)
                    .map(d -> (PsiDirectory) d)
                    .filter(d -> "commons-module".equals(d.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("commons-module not found"));

            VirtualFile commonsModuleDirVirtualFile = commonsModuleDir.getVirtualFile();
            String destinationPath = "/src/main/resources/jsons";
            VirtualFile targetDirectory = getOrCreateDestinationPath(commonsModuleDirVirtualFile, destinationPath);

            PsiDirectoryFactory.getInstance(project).createDirectory(targetDirectory).add(file);

            PsiFile file1 = PsiUtil.findFile(project, "test.json");
            if (file1.canNavigate()) {
                file1.navigate(true);
            }

            Messages.showInfoMessage(project, "test.json created", "File Created");
        }
        catch (Exception e)
        {
            Messages.showErrorDialog(project, e.getMessage(), "Error");
        }
    }

    private static VirtualFile getOrCreateDestinationPath(VirtualFile rootDirectory, String destinationPath) {
        try {
            return VfsUtil.createDirectoryIfMissing(rootDirectory, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}