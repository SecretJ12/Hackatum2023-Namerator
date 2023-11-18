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
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Service(Service.Level.PROJECT)
public final class ChatGPT {
    private final Project myProject;
    private static String key;

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

        return sendRequest(request);
    }

    public static String sendRequest(GPTRequest request) throws Exception {
        if (key == null || key.isEmpty()) {
            System.err.println("No key specified");
            return "";
        }
        System.out.println(key);

        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + key);


        con.setDoOutput(true);
        con.getOutputStream().write(request.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public void setKey(@Nullable String text) {
        if (text == null) {
            return;
        }
        ChatGPT.key = text;
    }
}