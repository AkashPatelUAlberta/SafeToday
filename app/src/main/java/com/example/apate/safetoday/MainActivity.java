package com.example.apate.safetoday;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Map<String,ArrayList<Date>> map = new HashMap<String, ArrayList<Date>>();
    Button btnEngine;
    TextView lblEngine;
    TextView lblFuel;

    // Bluetooth blue = new Bluetooth();
    // map that knows how many times people have called
    //private Map<String, ArrayList<Date>> map = new HashMap<String, ArrayList<Date>>();

//    new EchoOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new LineFeedOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new TimeoutObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new SelectProtocolObdCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
    public static Map<String, ArrayList<Date>> getMap() {
        return map;
    }

    public static void putMap(Map<String, ArrayList<Date>> m) {
        map = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);

        btnEngine = (Button)findViewById(R.id.engineBtn);

        lblEngine = (TextView)findViewById(R.id.engineTest);
        lblFuel = (TextView)findViewById(R.id.fuelLabel);

        btnEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (lblEngine.getText() == "Engine Test Working!") {
                    lblEngine.setText("");
                } else {
                    lblEngine.setText("Engine Test Working!");
                }
            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
