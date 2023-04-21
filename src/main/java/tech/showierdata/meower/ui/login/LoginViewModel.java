package tech.showierdata.meower.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.JsonWriter;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONStringer;

import java.net.URISyntaxException;


import tech.showierdata.meower.Websocket;
import tech.showierdata.meower.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /*        self.permitted_chars_username = []
        self.permitted_chars_post = []
        for char in string.ascii_letters:
            self.permitted_chars_username.append(char)
            self.permitted_chars_post.append(char)
        for char in string.digits:
            self.permitted_chars_username.append(char)
            self.permitted_chars_post.append(char)
        for char in string.punctuation:
            self.permitted_chars_post.append(char)
        self.permitted_chars_username.extend(["_", "-"])
        self.permitted_chars_post.append(" ")

     */
    static char[] AllowedUsernameCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-".toCharArray();


    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        try {
            Websocket.getInstance().send(new JSONStringer()
                    .object()
                    .key("cmd").value("direct")
                    .key("val").object()
                        .key("cmd").value("authpswd")
                        .key("val").object()
                            .key("username").value(username)
                            .key("pswd").value(password)
                            .endObject()
                        .endObject()
                    .endObject()
                    .toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        if (username.length() > 20) {
            return false;
        }

        if (username.trim().isEmpty()) {
            return false;
        }

        for (char c : username.toCharArray()) {
            boolean found = false;
            for (char allowed : AllowedUsernameCharacters) {
                if (c == allowed) {
                    found = true;
                    break;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }

        String trimmed = password.trim();
        if (trimmed.length() < 6) {
            return false;
        }
        if (trimmed.length() > 200) {
            return false;
        }
        return true;
    }
}