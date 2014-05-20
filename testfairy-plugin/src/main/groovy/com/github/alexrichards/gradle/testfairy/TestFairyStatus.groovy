package com.github.alexrichards.gradle.testfairy

class TestFairyStatus {

    int code
    String status
    String message
    String app_name
    String app_version
    long file_size
    String build_url
    String invite_testers_url
    String instrumented_url
    String icon_url

    int getCode() {
        return code
    }

    void setCode(int code) {
        this.code = code
    }

    String getStatus() {
        return status
    }

    void setStatus(String status) {
        this.status = status
    }

    String getMessage() {
        return message
    }

    void setMessage(String message) {
        this.message = message
    }

    String getApp_name() {
        return app_name
    }

    void setApp_name(String app_name) {
        this.app_name = app_name
    }

    String getApp_version() {
        return app_version
    }

    void setApp_version(String app_version) {
        this.app_version = app_version
    }

    long getFile_size() {
        return file_size
    }

    void setFile_size(long file_size) {
        this.file_size = file_size
    }

    String getBuild_url() {
        return build_url
    }

    void setBuild_url(String build_url) {
        this.build_url = build_url
    }

    String getInvite_testers_url() {
        return invite_testers_url
    }

    void setInvite_testers_url(String invite_testers_url) {
        this.invite_testers_url = invite_testers_url
    }

    String getInstrumented_url() {
        return instrumented_url
    }

    void setInstrumented_url(String instrumented_url) {
        this.instrumented_url = instrumented_url
    }

    String getIcon_url() {
        return icon_url
    }

    void setIcon_url(String icon_url) {
        this.icon_url = icon_url
    }
}
