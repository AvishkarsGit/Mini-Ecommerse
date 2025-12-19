package com.avishkar.mini_e_commerse.constants;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.avishkar.mini_e_commerse.R;
import com.avishkar.mini_e_commerse.interfaces.OnPaymentComplete;

public class Global {

    private static AlertDialog loaderDialog;
    public static void showSuccessToast(String message, Context context) {


        View layout  = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout,null);


        TextView text = layout.findViewById(R.id.toast_text);
        ImageView icon = layout.findViewById(R.id.toast_icon);

        text.setText(message);
        icon.setImageResource(R.drawable.check); // change icon if needed

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();

    }

    public static void showErrorToast(String message, Context context) {


        View layout  = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout,null);


        TextView text = layout.findViewById(R.id.toast_text);
        ImageView icon = layout.findViewById(R.id.toast_icon);
        LinearLayout toastRoot = layout.findViewById(R.id.toast_root);

        text.setText(message);
        icon.setImageResource(R.drawable.check); // change icon if needed
        toastRoot.setBackgroundResource(R.drawable.error_toast_bg);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();

    }



    public static void showWarningToast(String message, Context context) {


        View layout  = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout,null);


        TextView text = layout.findViewById(R.id.toast_text);
        ImageView icon = layout.findViewById(R.id.toast_icon);
        LinearLayout toastRoot = layout.findViewById(R.id.toast_root);

        text.setText(message);
        icon.setImageResource(R.drawable.check); // change icon if needed
        toastRoot.setBackgroundResource(R.drawable.warning_toast_bg);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();

    }

    private static AlertDialog initLoader(Context context) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.loader_layout, null);
        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
        return dialog;

    }

    public static void showLoader(Context context) {
        if (loaderDialog == null || !loaderDialog.isShowing()) {
            loaderDialog = initLoader(context);
            loaderDialog.show();
        }
    }
    public static void hideLoader(Context context) {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
            loaderDialog = null;
        }
    }
    public static void showPaymentSuccess(Context context, OnPaymentComplete listener) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.success_layout, null);

        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }

        dialog.show();

        LottieAnimationView lottie = view.findViewById(R.id.successAnimation);

        // Slight zoom-in effect (Google Pay style)
        lottie.setScaleX(0.7f);
        lottie.setScaleY(0.7f);
        lottie.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();

        lottie.playAnimation();

        // Dismiss when animation ends
        lottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dialog.dismiss();
                if (listener!=null) {
                    listener.onSuccess();
                }
            }
        });
    }


}
