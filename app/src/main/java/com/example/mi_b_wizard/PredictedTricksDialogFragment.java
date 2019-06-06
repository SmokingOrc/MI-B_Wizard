package com.example.mi_b_wizard;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class PredictedTricksDialogFragment extends DialogFragment  {

    /* To receive event callbacks this interface must be implemented
    of the activity, which creates an instance of the dialogFragment.
    If the host needs to query the DialogFragment, these two methodes passes the DialogFragment to the host. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener noticeDialogListener;

    // Fragment.onAttach() method has to be overridden to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check if the host activity has implemented that the callback interface
        try {
            // Instantiation of the NoticeDialogListener to send events back to the host
            noticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // When the activity doesn't implements the interface a exception is thrown
            throw new ClassCastException(context.toString()
                    + "NoticeDialogListener needs to be implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String predictedTricks;
        Bundle args = getArguments();
        predictedTricks = args.getString("PREDICTEDTRICKS");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Wollen Sie diese Stiche ansagen?")
                .setMessage(predictedTricks)
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //To send the postive click event back to the host activity
                        noticeDialogListener.onDialogPositiveClick(PredictedTricksDialogFragment.this);
                        PredictedTricksDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //To send the negative click event back to the host activity
                        noticeDialogListener.onDialogNegativeClick(PredictedTricksDialogFragment.this);
                        PredictedTricksDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and returns it
        return alertDialogBuilder.create();
    }

}
