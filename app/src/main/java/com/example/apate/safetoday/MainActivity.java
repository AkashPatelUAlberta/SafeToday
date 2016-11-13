package com.example.apate.safetoday;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnEngine;
    TextView lblEngine;
    TextView lblFuel;

//    Bluetooth blue = new Bluetooth();
    // map that knows how many times people have called
    private Map<String, ArrayList<Date>> map = new HashMap<String, ArrayList<Date>>();

//    new EchoOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new LineFeedOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new TimeoutObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new SelectProtocolObdCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
    public Map<String, ArrayList<Date>> getMap() {
        return map;
    }

    public void putMap(Map<String, ArrayList<Date>> m) {
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

    }


    //Bluetooth blue = new Bluetooth(this.getApplicationContext());
    //bluetooth



    public void ins(Map<String, ArrayList<Date>> map) {
        this.map = map;
    }
}
