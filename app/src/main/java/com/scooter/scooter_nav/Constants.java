package com.scooter.scooter_nav;

public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Message constants to send to bluetooth
    // variables for bluetooth
    public static final char RIGHT_TURN = 'R';
    public static final char LEFT_TURN = 'L';
    public static final char RIGHT_TURN_APPROACHING = 'R';
    public static final char LEFT_TURN_APPROACHING = 'L';
    public static final char CONTINUE = '4'; // we dont have one yet
    public static final char ARRIVAL = '5'; // same
    public static final char UTURN = 'U';
    public static final char CONNECTED = '7'; //same
    public static final char LOW_POWER = '8'; //add
    public static final char HIGH_POWER = '9'; //add
    public static final char ROUTE_START = 'b'; //add
}