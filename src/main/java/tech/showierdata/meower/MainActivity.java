package tech.showierdata.meower;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_main);

        try {
            Websocket.getInstance().connect();
        } catch (URISyntaxException e) {
            //log error
            Log.println(Log.ERROR, "Websocket", e.getMessage());
        }



    }
}