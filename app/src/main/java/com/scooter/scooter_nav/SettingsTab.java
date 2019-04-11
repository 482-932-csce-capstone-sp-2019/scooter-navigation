package com.scooter.scooter_nav;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.text.InputType;
import android.widget.EditText;

import java.util.Iterator;
import java.util.Set;

public class SettingsTab extends PreferenceFragmentCompat implements Handler.Callback {
    private final static int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter adapter;
    BluetoothSocket bt_socket;
    public Handler bh;
    public static BluetoothService mChatService;
    @SuppressWarnings("ResourceType")
    //@Override
 /*   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.settings_tab_fragment);
        bh = new Handler(this);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    REQUEST_ENABLE_BT);
        }

        adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            initialize_paired_devices_list();
        }

        Preference devices_screen = ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(1);
        devices_screen.setEnabled(false);
        devices_screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Title");

                // taken from StackOverflow
                final EditText input = new EditText(getActivity());

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChatService.write(input.getText().toString().getBytes());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
        });

        //EditTextPreference turnApproachingPref = (EditTextPreference)findPreference("turn_approaching_distance");
        //turnApproachingPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //EditTextPreference turnNowPref = (EditTextPreference)findPreference("turn_now_distance");
        //turnNowPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }
*/
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        super.onCreate(bundle);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.settings_tab_fragment);
        bh = new Handler(this);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    REQUEST_ENABLE_BT);
        }

        adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            initialize_paired_devices_list();
        }

        Preference devices_screen = ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(1);
        devices_screen.setEnabled(false);
        devices_screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Title");

                // taken from StackOverflow
                final EditText input = new EditText(getActivity());

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChatService.write(input.getText().toString().getBytes());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
        });

        //EditTextPreference turnApproachingPref = (EditTextPreference)findPreference("turn_approaching_distance");
        //turnApproachingPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //EditTextPreference turnNowPref = (EditTextPreference)findPreference("turn_now_distance");
        //turnNowPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message message) {
        String[] MessageStatus = new String[]{"STATE_NONE","STATE_LISTEN","STATE_CONNECTING","STATE_CONNECTED"};
        switch (message.what) {
            case 1:
                if (message.arg1 == 3)
                {
                    PreferenceScreen devices_screen = (PreferenceScreen) ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(0);
                    for (int i = 0; i < devices_screen.getPreferenceCount(); i++)
                    {
                        if (devices_screen.getPreference(i).getSummary() == null)
                        {
                            continue;
                        }
                        else if (devices_screen.getPreference(i).getSummary().equals("Connecting..."))
                        {
                            devices_screen.getPreference(i).setSummary("Connected.");
                        }
                    }
                    Preference tester = ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(1);
                    tester.setEnabled(true);
                    Preference intensity = ((PreferenceCategory) getPreferenceScreen().getPreference(1)).getPreference(0);
                    intensity.setEnabled(true);
                }
                else{
                    Preference tester = ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(1);
                    tester.setEnabled(false);
                    Preference intensity = ((PreferenceCategory) getPreferenceScreen().getPreference(1)).getPreference(0);
                    intensity.setEnabled(false);
                }
                break;
        }
        return true;
    }

    private void initialize_paired_devices_list()
    {
        Set<BluetoothDevice> paired_devices = adapter.getBondedDevices();
        Iterator<BluetoothDevice> it = paired_devices.iterator();
        while (it.hasNext()) {
            BluetoothDevice device = it.next();
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress();
            ParcelUuid[] uuids = device.getUuids();
            for (int i = 0; i < uuids.length; i++)
            {
                if (uuids[i].getUuid().toString().equals("00001101-0000-1000-8000-00805f9b34fb")) {

                    // initialize chat service
                    PreferenceScreen devices_screen = (PreferenceScreen) ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(0);
                    Preference preference = new Preference(getActivity());
                    String name = (deviceName != null) ? deviceName : "Unknown";
                    preference.setTitle(name + "\n" + deviceHardwareAddress);
                    preference.setKey(deviceHardwareAddress);
                    devices_screen.addPreference(preference);
                    preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            BluetoothDevice selected_device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(preference.getTitle().toString().split("\n")[1]);
                            if (mChatService == null)
                            {
                                mChatService = new BluetoothService(getActivity(), bh);
                            }
                            mChatService.connect(selected_device,true);
                            PreferenceScreen devices_screen = (PreferenceScreen) ((PreferenceCategory) getPreferenceScreen().getPreference(0)).getPreference(0);
                            for (int i = 0; i < devices_screen.getPreferenceCount(); i++)
                            {
                                if (devices_screen.getPreference(i).getSummary() != null)
                                {
                                    devices_screen.getPreference(i).setSummary(null);
                                }
                            }
                            preference.setSummary("Connecting...");
                            return true;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_ENABLE_BT)
        {
            initialize_paired_devices_list();
        }
    }
}


