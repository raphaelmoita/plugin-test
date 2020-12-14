package com.moita.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import static java.util.Arrays.asList;

public class NotificationUtil {

    public static void showBalloon(Project project, MessageType type, String text) {

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setSmallVariant(true)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(RelativePoint.getSouthEastOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

    public static void showStackBalloon(Project project, MessageType type, String text) {

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        JBPopupFactory.getInstance()
                .createListPopup(new BaseListPopupStep("Created", asList("aaaaa", "bbbbb")))
                .show(RelativePoint.getSouthEastOf(statusBar.getComponent()));


    }

    public static void notify(Project project, NotificationType type, String title, String content)
    {
        String groupId = NotificationGroup.balloonGroup("MoitaGroup").getDisplayId();
        Notification notification = new Notification(groupId, title, content, type);
        notification.notify(project);
        notification.hideBalloon();
    }

    private static void showBalloon(String message, MessageType type, long fadeoutTime) {
        try {
            String html = "<html><body>" + message + " created!</body></html>";
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(html, type, null)
                    .setFadeoutTime(fadeoutTime)
                    .setShadow(true)
                    .createBalloon()
                    .show(RelativePoint.getNorthEastOf(WindowManager.getInstance().getAllProjectFrames()[0].getComponent()), Balloon.Position.below);
        } catch (Exception e) {
            // Ignore error. It's just a balloon
        }
    }
}
