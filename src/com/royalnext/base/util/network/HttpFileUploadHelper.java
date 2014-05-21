package com.royalnext.base.util.network;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.royalnext.base.util.Common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpFileUploadHelper {
    final String BOUNDARY = "==================================";
    final String HYPHENS = "--";
    final String CRLF = "\r\n";

    public String file_para = "photo";
    public String file_name = "photo.jpg";
    public String file_type = "image/jpeg";
    private Handler handler;

    private void free() {
        handler = null;
    }

    public HttpFileUploadHelper(final Handler handler) {
        this.handler = handler;
    }

    public void performUpload(final String urlStr, final Map<String, String> params, final Uri fileUri) {
        long startDataOS = 0;
        long endDataOS = 0;
        long startInputStream = 0;
        long endInputStream = 0;
        long startUploadTask = 0;
        long endUploadTask = 0;
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        try {
            startUploadTask = System.currentTimeMillis();
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(300000);
            conn.setReadTimeout(300000);
            conn.setChunkedStreamingMode(262144);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            startDataOS = System.currentTimeMillis();
            DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
            dataOS.writeBytes(HYPHENS + BOUNDARY + CRLF);
            // ******************************************** HANDLE PARAMS
            for (String key : params.keySet()) {
                String value = params.get(key);
                dataOS.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + CRLF);
                dataOS.writeBytes("Content-Type: text/plain;charset=UTF-8" + CRLF);
                dataOS.writeBytes("Content-Length: " + value.length() + CRLF);
                dataOS.writeBytes(CRLF);
                dataOS.writeBytes(value + CRLF);
                dataOS.writeBytes(HYPHENS + BOUNDARY + CRLF);
            }
            // ******************************************** END

            // ******************************************** HANDLE FILE
            if (fileUri != null) {
                FileInputStream fileInputStream = new FileInputStream(new File(fileUri.getPath()));
                dataOS.writeBytes(HYPHENS + BOUNDARY + HYPHENS);
                dataOS.writeBytes("Content-Disposition: form-data; name=\"" + file_para + "\"; filename=\"" + file_name + "\"" + CRLF);
                dataOS.writeBytes("Content-Type: " + file_type + CRLF);
                dataOS.writeBytes(CRLF);

                //   int iBytesAvailable = fileInputStream.available();
                //  byte[] byteData = new byte[iBytesAvailable];
                int currentByte = 0;
                int count;
                byte buffer[] = new byte[1024];
                while ((count = fileInputStream.read(buffer)) > 0) {
                    dataOS.write(buffer, 0, count);
                    currentByte += count;
                    Common.d("HttpFileUploadHelper writing KByte: " + currentByte / 1024);
                }
               /* int iBytesRead = fileInputStream.read(byteData, 0, iBytesAvailable);
                while (iBytesRead > 0) {
                    dataOS.write(byteData, 0, iBytesAvailable);
                    iBytesAvailable = fileInputStream.available();
                    iBytesRead = fileInputStream.read(byteData, 0, iBytesAvailable);
                }*/
                dataOS.writeBytes(CRLF);
                dataOS.writeBytes(HYPHENS + BOUNDARY + HYPHENS + CRLF);
                fileInputStream.close();

            }
            // ******************************************** END
            Common.d("HttpFileUploadHelper dataOS Finish");
            endDataOS = System.currentTimeMillis();
            dataOS.flush();
            dataOS.close();
            Common.d("HttpFileUploadHelper InputStream Start");
            startInputStream = System.currentTimeMillis();
            InputStream responseStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            endInputStream = System.currentTimeMillis();
            endUploadTask = System.currentTimeMillis();
            Common.d("HttpFileUploadHelper InputStream Finish");
            Common.d("HttpFileUploadHelper DataOS Time Taken" + (endDataOS - startDataOS));
            Common.d("HttpFileUploadHelper InputStream Time Taken" + (endInputStream - startInputStream));
            Common.d("HttpFileUploadHelper Total Time Taken" + (endUploadTask - startUploadTask));
            String response = stringBuilder.toString();
            String code = conn.getResponseCode() + "";
            Common.d("CODE: " + code);
            Common.d("RESPONSE: " + response);
            responseStream.close();
            conn.disconnect();
            bundle.putString("CODE", code);
            bundle.putString("RESPONSE", response);
            message.setData(bundle);
            handler.sendMessage(message);
        } catch (Exception e) {
            Common.e(e);
            bundle.putString("CODE", "500");
            bundle.putString("RESPONSE", "Network Error");
            message.setData(bundle);
            handler.sendMessage(message);
        } finally {
            free();
        }
    }

    public void performUpload2(final String urlStr, final Map<String, String> params, final Uri fileUri) {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
        HttpConnectionParams.setSoTimeout(httpParameters, 300000);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(urlStr);
        try {
            MultipartEntity entity = new MultipartEntity();
            for (String key : params.keySet()) {
                String value = params.get(key);
                entity.addPart(key, new StringBody(value));
            }


            File file = new File(fileUri.getPath());
            entity.addPart(file_para, new FileBody(file));

            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }

            String result = s.toString();
            String code = response.getStatusLine().getStatusCode() + "";
            Common.d("CODE: " + code);
            Common.d("RESPONSE: " + result);
            bundle.putString("CODE", code);
            bundle.putString("RESPONSE", result);
            message.setData(bundle);
            handler.sendMessage(message);
        } catch (Exception e) {
            Common.e(e);
            bundle.putString("CODE", "500");
            bundle.putString("RESPONSE", "Network Error");
            message.setData(bundle);
            handler.sendMessage(message);
        } finally {
            free();
        }
    }
}
