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
            int codeInt = Integer.parseInt(code);
            if (codeInt < 200 && codeInt >= 300) {
                onError("", "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("timeout")) {
                onError(result, "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("connectionError")) {
                onError(result, "901", BaseApp.me.getString(R.string.error_network));
            } else if (result.equals("fail")) {
                onError(result, "900", BaseApp.me.getString(R.string.error_server));
            } else {
                if (manager.isTaskAlive(taskId)) {
                    manager.onSuccess(taskId, result);
                    onSuccess(result);
                } else {
                    Common.i("Task killed: " + taskId);
                }
            }
        } catch (Exception e) {
            if (manager.isTaskAlive(taskId)) {
                onError("", "900", BaseApp.me.getString(R.string.error_server));
                Common.e(e);
            } else {
                Common.i("Task killed: " + taskId);
            }
        } finally {
            manager = null;
        }
    }

}
