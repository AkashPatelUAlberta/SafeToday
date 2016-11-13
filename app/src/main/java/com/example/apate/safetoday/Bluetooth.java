package com.example.apate.safetoday;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;

//import com.github.pires.obd.enums.ObdProtocols;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.engine.RPMCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Randi Lynn on 2016-11-12.
 */

public class Bluetooth extends Activity {
    private String OBDII_Adapter;

    private void setup() {
        ArrayList deviceStrs = new ArrayList();
        final ArrayList devices = new ArrayList();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceStrs.add(device.getName() + "\n" + device.getAddress());
                devices.add(device.getAddress());
            }
        }

        // show list
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

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
        OBD_Connect();
    }

    public void OBD_Connect() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(OBDII_Adapter);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothSocket socket = null;
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new TimeoutCommand(10).run(socket.getInputStream(), socket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RPMCommand engineRpmCommand = new RPMCommand();
        SpeedCommand speedCommand = new SpeedCommand();
        while (!Thread.currentThread().isInterrupted())
        {
            try {
                engineRpmCommand.run(socket.getInputStream(), socket.getOutputStream());
                speedCommand.run(socket.getInputStream(), socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "RPM: " + engineRpmCommand.getFormattedResult());
            Log.d(TAG, "Speed: " + speedCommand.getFormattedResult());
        }
    }


}