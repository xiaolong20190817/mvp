package com.kunfei.basemvplib.progress;

public interface ViewCallback {
    void downloadSuccess();

    void downloadProcess(int progress);

    void downloadFailed(String error);

    String filePathName();

}
