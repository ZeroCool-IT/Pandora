package it.zerocool.pandora.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import it.zerocool.pandora.R;

/**
 * Created by Marco on 14/01/2015.
 */
public class WarningDialog extends DialogFragment {

    private String title;
    private String message;
    private Drawable icon;

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String ICON = "icon";


    public WarningDialog() {
    }


    /**
     * Action performed on creation of dialog
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        title = getArguments().getString(TITLE);
        message = getArguments().getString(MESSAGE);
        icon = getActivity().getResources().getDrawable(getArguments().getInt(ICON));
        builder.setIcon(icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_button_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
