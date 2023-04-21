package tech.showierdata.meower;

import org.json.JSONTokener;

public interface Callback {
    void onMessage(JSONTokener message);
    void onLogin(String token);
    void onClose();

}
