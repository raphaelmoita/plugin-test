package com.moita.action;

import com.intellij.json.json5.Json5FileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.moita.util.PsiUtil;

import java.io.IOException;
import java.util.stream.Stream;

import static com.moita.util.NotificationUtil.showBalloon;

public class JsonManager {

    public static final String COMMONS_MODULE = "commons-module";
    public static final String JSON_FOLDER = "/src/main/resources/jsons";
    private Project project;
    private AnActionEvent anActionEvent;

    public JsonManager(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getProject();
    }

    public boolean save(String jsonFileName, String jsonContent) {

        try {

            PsiFileFactory factory = PsiFileFactory.getInstance(project);
            PsiFile jsonFile = factory.createFileFromText(jsonFileName, Json5FileType.INSTANCE, jsonContent);

            PsiDirectory rootDir = PsiUtil.findRootDirectory(anActionEvent);

            PsiDirectory commonsModuleDir = Stream.of(rootDir.getChildren())
                    .filter(d -> d instanceof PsiDirectory)
                    .map(d -> (PsiDirectory) d)
                    .filter(d -> COMMONS_MODULE.equals(d.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(COMMONS_MODULE + "not found"));

            VirtualFile commonsModuleVf = commonsModuleDir.getVirtualFile();
            VirtualFile targetDirectory = getOrCreateDestinationPath(commonsModuleVf, JSON_FOLDER);

            PsiDirectoryFactory.getInstance(project).createDirectory(targetDirectory).add(jsonFile);

            jsonFile = PsiUtil.findFile(project, jsonFileName);

            if (jsonFile.canNavigate()) {
                jsonFile.navigate(true);
            }

            showBalloon(project, MessageType.INFO, jsonFileName + " created!");
        } catch (Exception e) {
            showBalloon(project, MessageType.ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    private static VirtualFile getOrCreateDestinationPath(VirtualFile rootDirectory, String destinationPath) {
        try {
            return VfsUtil.createDirectoryIfMissing(rootDirectory, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}