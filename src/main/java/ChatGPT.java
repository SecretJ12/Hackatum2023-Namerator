import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization","Bearer " + key);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        String response = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
        System.out.println(response);
        return response;
    }

    public void setKey(@Nullable String text) {
        if(text == null) {
            return;
        }
        ChatGPT.key = text;
    }
}