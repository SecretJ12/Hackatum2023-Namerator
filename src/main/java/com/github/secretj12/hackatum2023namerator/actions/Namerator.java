package com.github.secretj12.hackatum2023namerator.actions;

import com.github.secretj12.hackatum2023namerator.GPTModels;
import com.github.secretj12.hackatum2023namerator.GPTRequest;
import com.github.secretj12.hackatum2023namerator.GPTRequester;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.rename.RenameProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Namerator extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Enable the action only if text is selected
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        boolean isTextSelected = editor != null && editor.getSelectionModel().hasSelection();

        // Set the visibility of the action
        event.getPresentation().setEnabledAndVisible(isTextSelected);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        Project project = event.getData(CommonDataKeys.PROJECT);

        if (editor != null && project != null) {
            String sel = editor.getSelectionModel().getSelectedText();
            int vLine = editor.getSelectionModel().getSelectionEnd();


            String[] lines = editor.getDocument().getText().split("\n");
            String numberedText = IntStream.range(0, lines.length)
                    .mapToObj(i -> (i + 1) + ": " + lines[i])
                    .collect(Collectors.joining("\n"));
            String question = vLine + " \"" + sel + "\"\n\n" + numberedText;
            try {
                String generatedName = generateNames(question)[0];

                final int offset = editor.getCaretModel().getOffset();

                PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                assert psiFile != null;
                PsiElement psiElement = psiFile.findElementAt(offset);
                assert(psiElement != null);
                System.out.println(psiElement.getClass());
                psiElement = psiElement.getParent();
                System.out.println(psiElement.getClass());
                assert(psiElement != null);

                RenameProcessor rP = new RenameProcessor(project, psiElement, generatedName, true, false);
                rP.run();
            } catch (Exception e) {
                System.err.println("Request to ChatGPT failed");
            }
        }
    }

    private String[] generateNames(String question) throws Exception {
        GPTRequest request = new GPTRequest(
                0, 100,
                0.5,
                0,
                0,
                GPTModels.GPT4,
                system_message,
                question);
        return GPTRequester.sendRequest(request).split("\n");
    }

    private static String system_message = """
            Your name is "Namerator".
            You first get a line number and the name of a variable. Afterward you get a code snipped the variable is used in. You should then generate some suggestions for proper variables names, which are kind of explanatory for the variable used.
            The name should have a maximum of 30 characters, you have to provide 3 suggestions. Give every suggestions in a new line without any other descripion.
            The main function should remain the main function""";
}