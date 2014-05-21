package com.royalnext.base.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class DownloadFileTask extends AsyncTask<String, Integer, String> {

    protected void download(String urlPath, String filePath) throws Exception {
        Common.d("DownloadFileTask Starts");
        String tmpFilePath = filePath +".tmp";
        File checkFile = new File(tmpFilePath);
        if(checkFile.exists()){
            checkFile.delete();
        }
        URL url = new URL(urlPath);
        URLConnection connection = url.openConnection();
        connection.connect();
        int fileLength = connection.getContentLength();
        InputStream input = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream(tmpFilePath);

        byte data[] = new byte[1024];
        long total = 0;
        int count;
        while ((count = input.read(data)) != -1) {
            total += count;
            publishProgress((int) (total * 100 / fileLength));
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
        if(fileLength == checkFile.length()){
            checkFile.renameTo( new File(filePath));
        }else{
            checkFile.delete();
        }
        Common.d("DownloadFileTask Finish");
    }
}
