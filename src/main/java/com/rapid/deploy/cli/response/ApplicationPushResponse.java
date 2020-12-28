package com.rapid.deploy.cli.response;

import lombok.Data;

@Data
public class ApplicationPushResponse {
    private String message;
    private String containerId;
}
