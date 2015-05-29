package net.mready.rccarcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * Created by andreivasilescu on 29/05/15.
 */
public class BluetoothManager extends Thread {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice carDevice;
    private BluetoothSocket socket;
    private CarState carState;
    private CarController controller;

    private static final String CAR_MAC = "00:06:66:75:5C:25";

    public BluetoothManager(CarState carState, CarController controller) {
        this.controller = controller;
        this.carState = carState;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        carDevice = bluetoothAdapter.getRemoteDevice(CAR_MAC);

        try {
            Method m = carDevice.getClass().getMethod("createRfcommSocket",
                    new Class[] { int.class });

            socket = (BluetoothSocket) m.invoke(carDevice, Integer.valueOf(1));

            socket.connect();
            socket.getOutputStream().write('P');
            Thread.sleep(1000);
            socket.getOutputStream().write('L');
            socket.getOutputStream().write('H');
            Thread.sleep(1000);
            socket.getOutputStream().write('N');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run () {
        while (true) {
            sendContinuosActions();
        }
    }

    public void sendContinuosActions() {
        if (carState.traction == CarState.Traction.FORWARD) {
            sendAction('W');
        }

        if (carState.traction == CarState.Traction.REVERSE) {
            sendAction('S');
        }

        if (carState.traction == CarState.Traction.NONE) {
            sendAction('Z');
        }

        if (carState.steering == CarState.Steering.LEFT) {
            sendAction('A');
        }

        if (carState.steering == CarState.Steering.RIGHT) {
            sendAction('D');
        }

        if (carState.steering == CarState.Steering.FRONT) {
            sendAction('X');
        }

        if (carState.headlights == Boolean.TRUE) {
            sendAction('H');
        } else {
            sendAction('N');
        }

        if (carState.policelights == Boolean.TRUE) {
            sendAction('P');
        } else {
            sendAction('L');
        }
    }

    public void sendAction(int action) {
        try {
            if (!socket.isConnected()) {
                socket.connect();
            }

            socket.getOutputStream().write(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
