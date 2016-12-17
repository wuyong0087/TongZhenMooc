package com.tzhen.mooc.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tzhen.mooc.R;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/17.
 */
public class DialogProgress {
    private Dialog progressDialog;

    @Inject
    public DialogProgress() {
    }

    public void showProgressView(Context activity) {
        if (progressDialog == null) {
            View view = View.inflate(activity, R.layout.layout_dialog_progress, null);

            progressDialog = new Dialog(activity, R.style.dialog);
            progressDialog.setContentView(view);
            progressDialog.setCancelable(true);

            Window window = progressDialog.getWindow();

            WindowManager.LayoutParams attrs = window.getAttributes();
            attrs.gravity = Gravity.CENTER;
            attrs.height = (int) activity.getResources().getDimension(R.dimen.loading_dialog_height);
            attrs.width = (int) activity.getResources().getDimension(R.dimen.loading_dialog_width);
            window.setAttributes(attrs);

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public void hideProgressView() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
