package com.github.secretj12.hackatum2023namerator.actions;

import com.github.secretj12.hackatum2023namerator.GPTModels;
import com.github.secretj12.hackatum2023namerator.GPTRequest;
import com.github.secretj12.hackatum2023namerator.GPTRequester;
import com.github.secretj12.hackatum2023namerator.toolWindow.MaxNameLengthDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.refactoring.rename.RenameProcessor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NamerateAll extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Enable the action only if text is selected
    }

    private static int maxNameLength = -1;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        Project project = event.getData(CommonDataKeys.PROJECT);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        if (editor != null && project != null && psiFile != null) {
//            if (maxNameLength == -1){
                MaxNameLengthDialog maxNameLengthDialog = new MaxNameLengthDialog();
                maxNameLengthDialog.show();
//            }

            String[] lines = editor.getDocument().getText().split("\n");
            String numberedText = IntStream.range(0, lines.length)
                    .mapToObj(i -> (i + 1) + ": " + lines[i])
                    .collect(Collectors.joining("\n"));
            JSONObject suggestions;
            try {
                suggestions = generateNames(numberedText);
            } catch (Exception e) {
                System.err.println("ChatGPT request failed");
                GPTRequester.setKey(null);
                return;
            }
            System.out.println(suggestions);

            psiFile.accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitElement(@NotNull PsiElement element) {
                    super.visitElement(element);

                    if (!(element instanceof PsiNamedElement)) {
                        element = element.getParent();
                    }
                    if (!(element instanceof PsiNamedElement namedElement))
                        return;

                    String sug;
                    try {
                        sug = suggestions.getString(namedElement.getName());

                    } catch (JSONException e) {
                        return;
                    }

                    if (!sug.matches("^[a-zA-Z0-9]+$") | ( namedElement.getName().length() > maxNameLength  && maxNameLength >= 0))
                        return;

                    RenameProcessor rP = new RenameProcessor(project, namedElement, sug, true, false);
                    rP.run();
                }
            });
        }
    }

    private JSONObject generateNames(String question) throws Exception {
        GPTRequest request = new GPTRequest(
                0, 100,
                0.5,
                0,
                0,
                GPTModels.GPT4,
                system_message,
                question);
        String response = GPTRequester.sendRequest(request);
        System.out.println(response);
        return new JSONObject(response);
    }


    public static void setMaxNameLength(int maxNameLength) {
        NamerateAll.maxNameLength = maxNameLength;
    }
    private static String system_message = """
            Your name is "Namerator".
            The user will provider you a code snippet and you should think of better suited namings for alle the contained variables and function names. Every name should have a maximum of 30 characters.
            The answer should be a JSON object of the form: "oldName: suggestion" for every variable and function name.""";
}