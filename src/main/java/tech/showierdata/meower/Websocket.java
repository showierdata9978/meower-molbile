package tech.showierdata.meower;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DnsResolver;
import android.net.NetworkInfo;
import android.util.ArrayMap;
import android.util.JsonReader;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONTokener;


import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;


import tech.showierdata.meower.ui.login.LoginActivity;

public class Websocket extends WebSocketClient {
    private ArrayList<Callback> callbacks = new ArrayList<Callback>();
    Application app = Application.getInstance();

    public void addCallback(Callback cb) {
        //add callback
        callbacks.add(cb);
    }

    public Websocket(URI serverURI) throws URISyntaxException {


        super(serverURI);
    }

    private static Websocket instance;


    @Override
    public void connect() {
        if (!app.isNetworkOpen()) {
            return; //don't connect if network is not open
        }
        super.connect();
    }

    public static Websocket getInstance() throws URISyntaxException {
        if (instance == null) {
            instance = new Websocket(new URI("wss://server.meower.org"));
        }
        return instance;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        //set current activity
        app.startActivity(new Intent(app, LoginActivity.class));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        //set current activity to main activity
        app.startActivities(new Intent[]{new Intent(app, MainActivity.class)});

        //call all callbacks
        for (Callback cb : callbacks) {
            cb.onClose();
        }


    }

    @Override
    public void onMessage(String message) {
        JSONTokener tokener = new JSONTokener(message);

        //call all callbacks
        for (Callback cb : callbacks) {
            cb.onMessage(tokener);
        }



        System.out.println("received message: " + message);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}