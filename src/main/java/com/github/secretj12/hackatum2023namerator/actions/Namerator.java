package com.github.secretj12.hackatum2023namerator.actions;

import com.github.secretj12.hackatum2023namerator.GPTModels;
import com.github.secretj12.hackatum2023namerator.GPTRequest;
import com.github.secretj12.hackatum2023namerator.GPTRequester;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.rename.RenameProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Namerator extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Enable the action only if text is selected
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        Project project = event.getData(CommonDataKeys.PROJECT);
        boolean isTextSelected = editor != null && editor.getSelectionModel().hasSelection();
        assert editor != null;
        PsiElement psiElement = findElement(editor, project);

        // Set the visibility of the action
        event.getPresentation().setEnabledAndVisible(!isTextSelected && psiElement != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        Project project = event.getData(CommonDataKeys.PROJECT);

        if (editor != null && project != null) {
            final int line = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line;

            PsiElement psiElement = findElement(editor, project);
            if (psiElement == null)
                return;

            String[] lines = editor.getDocument().getText().split("\n");
            String numberedText = IntStream.range(0, lines.length)
                    .mapToObj(i -> (i + 1) + ": " + lines[i])
                    .collect(Collectors.joining("\n"));
            String question = line + " \"" + psiElement.getText() + "\"\n\n" + numberedText;
            try {
                String generatedName = generateNames(question)[0];

                RenameProcessor rP = new RenameProcessor(project, psiElement, generatedName, true, false);
                rP.run();
            } catch (Exception e) {
                System.err.println("Request to ChatGPT failed");
            }
        }
    }

    private PsiElement findElement(Editor editor, Project project) {
        if (editor == null || project == null)
            return null;

        final int offset = editor.getCaretModel().getOffset();
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);

        assert psiFile != null;
        PsiElement psiElement = psiFile.findElementAt(offset);
        assert (psiElement != null);
        psiElement = psiElement.getParent();
        assert (psiElement != null);
        if ((psiElement instanceof PsiNamedElement))
            return psiElement;

        psiElement = psiFile.findElementAt(offset - 1);
        assert (psiElement != null);
        psiElement = psiElement.getParent();
        assert (psiElement != null);
        if ((psiElement instanceof PsiNamedElement))
            return psiElement;

        return null;
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