package org.pispeb.treffpunkt.client.view.login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import org.pispeb.treffpunkt.client.R;

/**
 * Created by tim on 11/03/18.
 */

public class AuthFragment extends Fragment {

    protected void showServerAddressDialog() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getContext());
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this.getContext());
        EditText input = new EditText(this.getContext());
        input.setText(preferences.getString(getString(
                R.string.key_server_address), ""));
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setHint(R.string.login_server_address);
        builder.setView(input);

        builder.setNegativeButton(R.string.cancel,
                ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(R.string.ok, ((dialog, which) -> {
            setServerAddress(input.getText().toString());
            dialog.dismiss();
        }));
        builder.show();
    }

    private void setServerAddress(String serverAddress) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this.getContext());
        // if no custom server address was specified, set default flag
        boolean defaultAddress = serverAddress.equals("");
        pref.edit()
                .putBoolean(getString(
                        R.string.key_using_default_server),
                        defaultAddress)
                .putString(getString(R.string.key_server_address),
                        serverAddress)
                .apply();
    }
}
