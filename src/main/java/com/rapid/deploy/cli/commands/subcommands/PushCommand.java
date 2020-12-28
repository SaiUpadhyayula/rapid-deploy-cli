package com.rapid.deploy.cli.commands.subcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.deploy.cli.response.ApplicationPushResponse;
import com.rapid.deploy.cli.response.ApplicationResponse;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import okhttp3.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.rapid.deploy.cli.handler.ErrorHandlerUtil.handleError;
import static java.util.Arrays.asList;
import static okhttp3.MediaType.get;

@Command(name = "push")
public class PushCommand implements Callable<Integer> {

    public static final MediaType JSON = get("application/json; charset=utf-8");

    @Parameters(index = "0")
    private String appName;

    @Override
    public Integer call() throws Exception {
        String presentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("Compressing project files..");
        ZipFile zipFile = new ZipFile(String.format("%s.zip", appName));
        zipFile.addFolder(presentWorkingDirectory, new ZipParameters());
        System.out.println("Validating application");
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url("http://localhost:9000/api/application/")
                .build();
        Response response = client.newCall(request).execute();
        String responseBodyStr = Objects.requireNonNull(response.body()).string();

        if (response.isSuccessful()) {
            List<ApplicationResponse> applicationResponses = asList(objectMapper.readValue(responseBodyStr, ApplicationResponse[].class));
            ApplicationResponse applicationResponse = applicationResponses.stream()
                    .filter(appResponse -> appResponse.getApplicationName().equals(appName))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException());
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", zipFile.getFile().getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), zipFile.getFile()))
                    .build();
            System.out.println("Uploading project files..");
            Request multiPartRequest = new Request.Builder()
                    .url("http://localhost:9000/api/application/source-code/app/" + applicationResponse.getGuid())
                    .post(formBody)
                    .build();
            Response multiPartResponse = client.newCall(multiPartRequest).execute();
            if (multiPartResponse.isSuccessful()) {
                System.out.println("Upload successful");
                String multiPartBodyResponse = Objects.requireNonNull(multiPartResponse.body()).string();
                ApplicationPushResponse applicationPushResponse = objectMapper.readValue(multiPartBodyResponse,
                        ApplicationPushResponse.class);
                System.out.println("Container Id - " + applicationPushResponse.getContainerId());
                String okStr = CommandLine.Help.Ansi.AUTO.string("@|bold,green OK|@");
                System.out.println(String.format("%s -> %s", applicationPushResponse.getMessage(), okStr));
            } else {
                handleError(responseBodyStr);
            }
        } else {
            handleError(responseBodyStr);
        }
        return null;
    }
}
