package com.kunfei.basemvplib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class FileUtils {

    public static boolean dealBody(final ResponseBody responseBody, final String filePathName) {
        try {
            File futureStudioIconFile = new File(filePathName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = responseBody.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = responseBody.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Timber.d("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
               return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
