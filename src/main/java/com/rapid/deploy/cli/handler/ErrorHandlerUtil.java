package com.rapid.deploy.cli.handler;

import lombok.experimental.UtilityClass;
import org.json.JSONObject;
import picocli.CommandLine;

@UtilityClass
public class ErrorHandlerUtil {

    public static void handleError(String responseBodyStr) {
        JSONObject jsonObject = new JSONObject(responseBodyStr);
        String errOutput = CommandLine.Help.Ansi.AUTO.string("@|bold,red FAILED|@");
        System.out.println(jsonObject.get("message"));
        System.out.println(errOutput);
    }
}
