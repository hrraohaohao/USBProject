package com.hao.usbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UsbManager mUsbManager;
    private List<UsbDevice> devices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUsb();
    }

    private void initUsb() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Collection<UsbDevice> values = deviceList.values();
        Iterator<UsbDevice> iterator = values.iterator();
        while (iterator.hasNext()) {
            devices.add(iterator.next());
        }
    }





    public void connect(View view) {

    }

    public void send(View view) {


    }
}