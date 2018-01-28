package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

public class LoginViewModel extends ViewModel {

    private SingleLiveEvent<State> state;
    private String username;
    private String password;
    private UserLoginTask userLoginTask;


    public LoginViewModel() {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
        username = "";
        password = "";
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswort(String password) {
        this.password = password;
    }


    public void onLogin() {

        state.setValue(new State(ViewCall.LOGIN, 0));
        userLoginTask = new UserLoginTask(username, password);
        userLoginTask.execute((Void) null);


    }

    public void onGoToRegister() {
        state.setValue(new State(ViewCall.GO_TO_REGISTER, 0));
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: send request to requestEncoder
            //Dummy timer to simulate the connection
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;

            if (success) {
                state.setValue(new State(ViewCall.SUCCESS, 0));
            } else {
                state.setValue(new State(ViewCall.IDLE, 0));
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            state.setValue(new State(ViewCall.IDLE, 0));
        }
    }
}
