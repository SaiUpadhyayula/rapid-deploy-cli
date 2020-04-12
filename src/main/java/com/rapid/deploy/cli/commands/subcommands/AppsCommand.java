package com.rapid.deploy.cli.commands.subcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.deploy.cli.response.ApplicationResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import picocli.CommandLine.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.rapid.deploy.cli.handler.ErrorHandlerUtil.handleError;
import static picocli.CommandLine.Help.Ansi;

@Command(name = "apps")
public class AppsCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/application")
                .build();
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try (Response response = client.newCall(request).execute()) {
            String responseBodyStr = Objects.requireNonNull(response.body()).string();

            if (response.isSuccessful()) {
                List<ApplicationResponse> applicationResponses = Arrays.asList(objectMapper.readValue(responseBodyStr, ApplicationResponse[].class));
                applicationResponses.forEach(el -> System.out.println("-> " + el.getApplicationName()));
                String okStr = Ansi.AUTO.string("@|bold,green OK|@");
                System.out.println(okStr);
            } else {
                handleError(responseBodyStr);
            }
        }
        return null;
    }
}
