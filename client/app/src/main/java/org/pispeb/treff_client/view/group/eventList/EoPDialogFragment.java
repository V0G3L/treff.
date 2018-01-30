package org.pispeb.treff_client.view.group.eventList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import org.pispeb.treff_client.R;

/**
 * Dialog to let the user decide whether to create an
 * Event or a Poll (EoP). Passing decision back to attached activity
 * (not Fragment! even though it is called from Fragment)
 */

public class EoPDialogFragment extends DialogFragment {

    private EoPDialogListener mListener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // define dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // set message
        builder.setMessage(R.string.group_dialog_add);
        // add buttons for poll and event option
        builder.setNegativeButton(R.string.poll,
                (DialogInterface dialog, int which) -> {
//                        User wants to create a poll
                    mListener.onPollClick();
                });
        builder.setPositiveButton(R.string.event,
                (DialogInterface dialog, int which) -> {
//                        User wants to create an event
                    mListener.onEventClick();
                    });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EoPDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    // interface which must be implemented by attached activity
    // to allow callbacks
    public interface EoPDialogListener {
        void onPollClick();
        void onEventClick();
    }
}
