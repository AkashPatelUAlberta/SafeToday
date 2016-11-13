package com.example.apate.safetoday;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnEngine;
    TextView lblEngine;
    TextView lblFuel;
    Map<Date, String> map = new HashMap<Date, String>();

//    new EchoOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new LineFeedOffObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new TimeoutObdCommand().run(socket.getInputStream(), socket.getOutputStream());
//    new SelectProtocolObdCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());

    //Nathan fixed our project.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
