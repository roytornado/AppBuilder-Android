package com.royalnext.base.util.network;

import com.midland.base.R;
import com.royalnext.base.util.Common;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpConnectionHelper {
    public String code;
    public String response;
    public String urlNew;

    private void free() {
    }

    public HttpConnectionHelper() {
    }

    public void performGet(final String url, final Map<String, String> params) {
        performRequest(url, params, "GET");
    }

    public void performPost(final String url, final Map<String, String> params) {
        performRequest(url, params, "POST");
    }

    public void performRequest(String urlStr, Map<String, String> params, String method) {
        try {
            if (method.equals("GET")) {
                urlNew = processParamsForGet(urlStr, params);
            }
            if (method.equals("POST")) {
                urlNew = urlStr;
            }
            Common.d(method + " : " + urlNew);
            URL url = new URL(urlNew);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(1000 * 60);
            conn.setReadTimeout(1000 * 60);
            if (method.equals("POST")) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
                // ******************************************** HANDLE PARAMS
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    String content = key + "=" + URLEncoder.encode(value, "utf-8");
                    Common.d(content);
                    dataOS.writeBytes(content);
                }
                // ******************************************** END
                dataOS.flush();
                dataOS.close();
            }

            InputStream responseStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            response = stringBuilder.toString();
            code = conn.getResponseCode() + "";
            responseStream.close();
            conn.disconnect();
        } catch (Exception e) {
            code = "900";
            response = "Error";
        } finally {
            free();
        }
    }

    protected String processParamsForGet(String url, final Map<String, String> params) {
        if (!url.endsWith("?"))
            url += "?";

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        if ((params != null) && (params.size() > 0)) {
            nvps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        String paramString = URLEncodedUtils.format(nvps, "utf-8");
        url += paramString;
        return url;
    }
}
