package com.midland.base.util.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.midland.base.util.Common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class HTTPRequestHelper {
    public static final int POST_TYPE = 1;
    public static final int GET_TYPE = 2;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
    private static final DefaultHttpClient client;
    private Handler handler;

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("https", socketFactory, 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 60000);

        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        client = new DefaultHttpClient(cm, params);

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

    private void free() {
        handler = null;
    }

    public HTTPRequestHelper(final Handler handler) {
        this.handler = handler;
    }

    public HttpResponse performGet(final String url, final Map<String, String> params) {
        return performRequest(null, url, params, HTTPRequestHelper.GET_TYPE);
    }

    public HttpResponse performPost(final String url, final Map<String, String> params) {
        return performRequest(HTTPRequestHelper.MIME_FORM_ENCODED, url, params, HTTPRequestHelper.POST_TYPE);
    }

    private HttpResponse performRequest(final String contentType, final String url, final Map<String, String> params, final int requestType) {

        final Map<String, String> sendHeaders = new HashMap<String, String>();
        if (requestType == HTTPRequestHelper.POST_TYPE) {
            sendHeaders.put(HTTPRequestHelper.CONTENT_TYPE, contentType);
        }
        if (sendHeaders.size() > 0) {
            client.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                    for (String key : sendHeaders.keySet()) {
                        if (!request.containsHeader(key)) {
                            request.addHeader(key, sendHeaders.get(key));
                        }
                    }
                }
            });
        }

        if (requestType == HTTPRequestHelper.POST_TYPE) {
            HttpPost method = new HttpPost(url);
            List<NameValuePair> nvps = null;
            if ((params != null) && (params.size() > 0)) {
                nvps = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
            }
            if (nvps != null) {
                try {
                    method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                }
            }
            if (handler != null) {
                execute(client, method);
            } else {
                try {
                    return client.execute(method);
                } catch (Exception e) {
                    Common.e(e);
                }
            }
        } else if (requestType == HTTPRequestHelper.GET_TYPE) {
            String newUrl = processParamsForGet(url, params);
            Common.d("New URL:" + newUrl);
            HttpGet method = new HttpGet(newUrl);
            if (handler != null) {
                execute(client, method);
            } else {
                try {
                    return client.execute(method);
                } catch (Exception e) {
                    Common.e(e);
                }
            }
        }
        return null;
    }

    private void execute(HttpClient client, HttpRequestBase method) {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        try {
            HttpResponse response = client.execute(method);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                try {
                    result = HTTPRequestHelper.inputStreamToString(entity.getContent());
                    bundle.putString("CODE", Integer.toString(status.getStatusCode()));
                    bundle.putString("RESPONSE", result);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    bundle.putString("CODE", Integer.toString(status.getStatusCode()));
                    bundle.putString("RESPONSE", "Network Error");
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } else {
                bundle.putString("CODE", Integer.toString(status.getStatusCode()));
                bundle.putString("RESPONSE", "Network Error");
                message.setData(bundle);
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            bundle.putString("CODE", "500");
            bundle.putString("RESPONSE", "Network Error");
            message.setData(bundle);
            handler.sendMessage(message);
        }
        free();
    }

    public static String inputStreamToString(final InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static HttpResponse get(String urlString) throws MalformedURLException, IOException {
        HttpGet request = new HttpGet(urlString);
        return client.execute(request);
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
