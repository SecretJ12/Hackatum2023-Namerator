package com.github.secretj12.hackatum2023namerator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class Namerator extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Using the event, evaluate the context,
        // and enable or disable the action.
        System.out.println("Test update");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, implement an action.
        // For example, create and show a dialog.
        System.out.println("Test action");
    }

    // Override getActionUpdateThread() when you target 2022.3 or later!

}