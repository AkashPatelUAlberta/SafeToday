package com.example.apate.safetoday;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.pires.obd.enums.ObdProtocols;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.exceptions.UnableToConnectException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

/**
 * Created by Randi Lynn on 2016-11-12.
 */

public class Bluetooth extends Activity {
    private String OBDII_Adapter;
    private TextView rpm_textView;
    private TextView speed_textView;

    Context context;

    @Override
    protected void onCreate(Bundle data) {
        super.onCreate(data);
        setContentView(R.layout.bluetooth_activity);
        rpm_textView = (TextView) findViewById(R.id.rpm);
        speed_textView = (TextView) findViewById(R.id.speed);

        this.context = this;
        setup();

    }
    
    public void setup() {
        ArrayList deviceStrs = new ArrayList();
        final ArrayList devices = new ArrayList();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceStrs.add(device.getName() + "\n" + device.getAddress());
                devices.add(device.getAddress());
            }
        }

        // show list
        Spinner btView = new Spinner(this.context);
        ArrayAdapter adapter = new ArrayAdapter(this.context, android.R.layout.simple_spinner_item,deviceStrs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btView.setAdapter(adapter);
        //btView.setEnabled(true);
        String dv = btView.getSelectedItem().toString();
        OBDII_Adapter = dv;
         /**
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice,
                deviceStrs.toArray(new String[deviceStrs.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                //String deviceAddress = (String) devices.get(position);
                OBDII_Adapter = (String) devices.get(position);
            }
        });
        alertDialog.setTitle("Choose Bluetooth device");
        alertDialog.show();
     */
        OBD_Connect();
    }

    public void OBD_Connect() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        String OBDII[] = OBDII_Adapter.split("\\r\\n|\\n|\\r");
        BluetoothDevice device = btAdapter.getRemoteDevice(OBDII[1]);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothSocket socket = null;
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BluetoothSocket finalSocket = socket;

        try {
            new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new TimeoutCommand(50).run(socket.getInputStream(), socket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final RPMCommand engineRpmCommand = new RPMCommand();
        final SpeedCommand speedCommand = new SpeedCommand();

        Runnable pollBluetoothData = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted())
                {
                    try {
                        engineRpmCommand.run(finalSocket.getInputStream(), finalSocket.getOutputStream());
                        speedCommand.run(finalSocket.getInputStream(), finalSocket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnableToConnectException e) {
                        Log.d(TAG, "Can't connect");
                    }

                    Log.d(TAG, "RPM: " + engineRpmCommand.getFormattedResult());
                    Log.d(TAG, "Speed: " + speedCommand.getFormattedResult());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rpm_textView.setText(engineRpmCommand.getCalculatedResult());
                            speed_textView.setText(speedCommand.getFormattedResult());
                        }
                    });


                }
            }
        };

        Thread bluetoothPollingThread = new Thread(pollBluetoothData);
        bluetoothPollingThread.start();

    }



}