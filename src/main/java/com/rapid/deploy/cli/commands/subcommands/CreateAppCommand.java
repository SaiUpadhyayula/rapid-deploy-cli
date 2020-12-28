package com.rapid.deploy.cli.commands.subcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.deploy.cli.handler.ErrorHandlerUtil;
import com.rapid.deploy.cli.payload.ApplicationPayload;
import com.rapid.deploy.cli.response.ApplicationResponse;
import okhttp3.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Parameters;

import java.util.Objects;
import java.util.concurrent.Callable;

import static okhttp3.MediaType.get;

@Command(name = "create-app")
public class CreateAppCommand implements Callable<Integer> {

    public static final MediaType JSON = get("application/json; charset=utf-8");

    @Parameters(index = "0")
    private String appName;

    public Integer call() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        if (appName == null || appName.equals("")) {
            System.out.println("App Name cannot be null or empty");
            return -1;
        }
        ApplicationPayload applicationPayload = new ApplicationPayload(appName);
        String json = objectMapper.writeValueAsString(applicationPayload);
        RequestBody body = RequestBody.create(json, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:9000/api/application")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBodyStr = Objects.requireNonNull(response.body()).string();
            if (response.isSuccessful()) {
                ApplicationResponse applicationResponse = objectMapper.readValue(responseBodyStr,
                        ApplicationResponse.class);
                System.out.println(applicationResponse.getApplicationName() + " created successfully.");
                String okOutput = Ansi.AUTO.string("@|bold,green OK|@");
                System.out.println(okOutput);
            } else {
                ErrorHandlerUtil.handleError(responseBodyStr);
            }
        }
        return 0;
    }
}
