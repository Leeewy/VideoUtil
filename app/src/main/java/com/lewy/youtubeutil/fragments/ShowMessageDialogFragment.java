package com.lewy.youtubeutil.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.lewy.youtubeutil.R;
import com.lewy.youtubeutil.interfaces.MessageDialogCallback;

/**
 * Created by dawid on 15.05.2016.
 */
public class ShowMessageDialogFragment extends DialogFragment {

    private static MessageDialogCallback messageDialogCallback;
    private static String message;
    private static String title;

    public static ShowMessageDialogFragment newInstance(MessageDialogCallback messageDialogCallback, String message, String title){
        ShowMessageDialogFragment exitDialogFragment = new ShowMessageDialogFragment();
        ShowMessageDialogFragment.messageDialogCallback = messageDialogCallback;
        ShowMessageDialogFragment.message = message;
        ShowMessageDialogFragment.title = title;

        return exitDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (messageDialogCallback != null) {
                            messageDialogCallback.messageClose();
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (messageDialogCallback != null) {
                            messageDialogCallback.messageClose();
                        }
                    }
                });

        setCancelable(false);

        return builder.create();
    }
}
