package com.royalnext.base.util.network.multitask;

import com.royalnext.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.network.HttpConnectionHelper;

import java.util.HashMap;

public class HttpTask extends TasksRequest {
    public static enum HttpType {
        POST, GET
    }

    public static enum CacheType {
        NO_CACHE, RELOAD, CACHE
    }

    protected String host;
    protected HashMap<String, String> params;
    protected CacheType cacheType;
    protected HttpType httpType;

    public HttpTask(String tag, String host, HashMap<String, String> params, CacheType cacheType, HttpType httpType) {
        super(tag);
        this.host = host;
        this.params = params;
        this.cacheType = cacheType;
        this.httpType = httpType;
    }

    public void execute() throws Exception {
        HttpConnectionHelper http = new HttpConnectionHelper();
        if (httpType == HttpType.POST) {
            http.performPost(host, params);
        }
        if (httpType == HttpType.GET) {
            http.performGet(host, params);
        }

        if (http.code.equals("200")) {
            code = http.code;
            resMsg = http.response;
            isOk = true;
            return;
        }
        code = http.code;
        resMsg = BaseApp.me.getString(R.string.error_network);
        isOk = false;
    }
}
