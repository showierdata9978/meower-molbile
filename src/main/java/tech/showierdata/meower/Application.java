package tech.showierdata.meower;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URISyntaxException;
import java.util.Objects;

public class Application extends android.app.Application {
    private static Application instance;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Websocket getWebsocket() throws URISyntaxException {
        return Websocket.getInstance();
    }

    public Boolean isNetworkOpen() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        return (activeNetworkInfo != null && !Objects.requireNonNull(activeNetworkInfo).isConnected());
    }
}
