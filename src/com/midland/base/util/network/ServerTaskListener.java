package com.midland.base.util.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.midland.base.R;
import com.midland.base.app.BaseApp;
import com.midland.base.util.Common;

public abstract class ServerTaskListener extends Handler {
    private ServerTaskManager manager;
    private int taskId;
    public boolean isLog = true;

    public void setManager(ServerTaskManager manager, int taskId) {
        this.manager = manager;
        this.taskId = taskId;
    }

    public abstract void onStart();

    public abstract void onSuccess(String result);

    public abstract void onError(String result, String code, String msg);

    @Override
    public void handleMessage(Message msg) {
        try {
            Bundle bundle = msg.getData();
            String result = bundle.getString("RESPONSE");
            String code = bundle.getString("CODE");
            if (!code.equals("200")) {
                onError("", "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("timeout")) {
                onError(result, "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("connectionError")) {
                onError(result, "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("fail")) {
                onError(result, "900", BaseApp.me.getString(R.string.error_server));
            } else {
                if (manager != null) {
                    manager.onSuccess(taskId, result);
                    manager = null;
                }
                onSuccess(result);
            }
        } catch (Exception e) {
            onError("", "900", BaseApp.me.getString(R.string.error_server));
            Common.e(e);
        } finally {
            manager = null;
        }
    }

}
