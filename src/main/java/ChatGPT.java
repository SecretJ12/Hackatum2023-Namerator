import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.secretj12.hackatum2023namerator.GPTModels;
import com.github.secretj12.hackatum2023namerator.GPTRequest;
import com.github.secretj12.hackatum2023namerator.GPTRequester;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Service(Service.Level.PROJECT)
public final class ChatGPT {
    private final Project myProject;

    public ChatGPT(Project project) {
        myProject = project;
    }

    public String getChatResponse(String text) throws Exception {
        GPTRequest request = new GPTRequest(
                1.0,
                100,
                0.5,
                0,
                0,
                GPTModels.GPT35turbo,
                "",
                text);

        return GPTRequester.sendRequest(request);
    }

    public void setKey(@Nullable String text) {
        if (text == null) {
            return;
        }
        GPTRequester.setKey(text);
    }
}