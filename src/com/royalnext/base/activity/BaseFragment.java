package com.royalnext.base.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.royalnext.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.network.ServerTaskManager;
import com.royalnext.base.util.network.multitask.MultiTaskManager;
import com.royalnext.base.widget.AppToast;

public class BaseFragment extends Fragment {

    protected BaseFragment me() {
        return this;
    }

    private AppToast toast;
    private ProgressDialog loading;
    protected ServerTaskManager stm;
    protected MultiTaskManager mtm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = new AppToast(BaseApp.me);
        stm = new ServerTaskManager();
    }

    @Override
    public void onDestroy() {
        stm.free();
        stm = null;
        mtm = null;
        toast = null;
        hideLoading();
        super.onDestroy();
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
        loading = ProgressDialog.show(me().getActivity(), null, getString(R.string.loading), true, false);
    }

    protected void hideLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        loading = null;
    }

    protected void showMsg(int resId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(me().getActivity());
        dialog.setCancelable(true);
        dialog.setMessage(resId);
        dialog.show();
    }

    protected void hideKB() {
        if (me().getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) me().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(me().getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
