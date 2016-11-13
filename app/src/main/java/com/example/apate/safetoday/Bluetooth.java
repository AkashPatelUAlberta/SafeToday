package com.example.apate.safetoday;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Randi Lynn on 2016-11-12.
 */

public class Bluetooth extends Activity {
    private void setup() {
        ArrayList deviceStrs = new ArrayList();
        final ArrayList devices = new ArrayList();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device: pairedDevices) {
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
                String deviceAddress = (String) devices.get(position);
                // TODO save deviceAddress
            }
        });
        alertDialog.setTitle("Choose Bluetooth device");
        alertDialog.show();
    }
}
