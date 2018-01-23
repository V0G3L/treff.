package org.pispeb.treff_client.view.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.pispeb.treff_client.data.networking.Error;

/**
 * Universal dialog to display any errors that are relevant to the user
 */

public class ErrorDialogFragment extends DialogFragment {

    private String message;
    private int code;
    private static final String CODE_KEY = "CODE";
    private static final String MESSAGE_KEY = "MESSAGE";

    /**
     * Create new ErrorDialogFragment with given parameters
     * @param message error message to display
     * @param code internal error code (also displayed)
     * @return instance with parameters in arguments
     */
    public static ErrorDialogFragment getInstance(String message, int code) {
        ErrorDialogFragment df = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(CODE_KEY, code);
        args.putString(MESSAGE_KEY, message);
        df.setArguments(args);
        return df;
    }

    /**
     * Determine message from given code
     * @param code error code
     * @return instance with parameters as arguments
     */
    public static ErrorDialogFragment getInstance (int code) {
        ErrorDialogFragment df = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(CODE_KEY, code);
        args.putString(MESSAGE_KEY, Error.getValue(code).getMessage());
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        message = getArguments().getString(MESSAGE_KEY);
        code = getArguments().getInt(CODE_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Error " + code + ": " + message);

        return builder.create();
    }
}
