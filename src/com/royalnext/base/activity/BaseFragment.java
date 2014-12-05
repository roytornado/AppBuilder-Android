package com.royalnext.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.royalnext.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.Common;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Common.i(((Object) this).getClass().getSimpleName() + " onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.i(((Object) this).getClass().getSimpleName() + " onCreate");
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
        Common.i(((Object) this).getClass().getSimpleName() + " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Common.i(((Object) this).getClass().getSimpleName() + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.i(((Object) this).getClass().getSimpleName() + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Common.i(((Object) this).getClass().getSimpleName() + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Common.i(((Object) this).getClass().getSimpleName() + " onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Common.i(((Object) this).getClass().getSimpleName() + " onDetach");
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

    protected void showMsg(String text) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(me().getActivity());
        dialog.setCancelable(true);
        dialog.setMessage(text);
        dialog.show();
    }

    protected void hideKB() {
        if (me().getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) me().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(me().getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
