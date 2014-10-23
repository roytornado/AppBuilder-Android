package com.royalnext.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.royalnext.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.Common;
import com.royalnext.base.util.network.ServerTaskManager;
import com.royalnext.base.util.network.multitask.MultiTaskManager;
import com.royalnext.base.widget.AppToast;

public abstract class BaseFragmentActivity extends ActionBarActivity {
    private AppToast toast;
    private ProgressDialog loading;
    protected ServerTaskManager stm;
    protected MultiTaskManager mtm;

    protected Activity me() {
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        toast = new AppToast(BaseApp.me);
        stm = new ServerTaskManager();
        Common.i(getClass().getSimpleName() + ": onCreate");
    }

    public void onCreate(Bundle savedInstanceState, int layoutRes) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (layoutRes > -1) {
            setContentView(layoutRes);
        }
        toast = new AppToast(BaseApp.me);
        stm = new ServerTaskManager();
        Common.i(getClass().getSimpleName() + ": onCreate");
    }

    @Override
    protected void onDestroy() {
        Common.i(getClass().getSimpleName() + ": onDestroy");
        stm.free();
        stm = null;
        mtm = null;
        toast = null;
        hideLoading();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Common.i(getClass().getSimpleName() + ": onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Common.i(getClass().getSimpleName() + ": onPause");
        hideKB();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Common.i(getClass().getSimpleName() + ": onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Common.i(getClass().getSimpleName() + ": onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Common.i(getClass().getSimpleName() + ": onStop");
        super.onStop();
    }

    protected void showToast(int resId) {
        toast.setText(resId);
        toast.show();
    }

    protected void showToast(String text) {
        toast.setText(text);
        toast.show();
    }

    protected void showLoading() {
        hideLoading();
        loading = ProgressDialog.show(this, null, getString(R.string.loading), true, false);
    }

    protected void hideLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        loading = null;
    }

    protected void showMsg(int resId) {
        Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setMessage(resId);
        dialog.show();
    }

    protected void hideKB() {
        if (this.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
