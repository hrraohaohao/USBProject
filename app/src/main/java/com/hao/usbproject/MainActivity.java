package com.hao.usbproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hao.usbproject.adapter.BaseDataBindingAdapter;
import com.hao.usbproject.adapter.UsbListAdapter;
import com.hao.usbproject.databinding.ActivityMainBinding;
import com.hao.usbproject.helper.USBHelper;
import com.hao.usbproject.print.MPOS;
import com.hao.usbproject.print.Prints;
import com.hao.usbproject.usb.UsbConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements BaseDataBindingAdapter.OnItemClickListener<UsbDevice> {
    private String TAG = MainActivity.class.getName();

    private UsbManager mUsbManager;
    private List<UsbDevice> devices;
    private ActivityMainBinding binding;

    private UsbListAdapter usbListAdapter = new UsbListAdapter(this);

    MPOS mPos = new MPOS();
    ExecutorService es = Executors.newScheduledThreadPool(2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(usbListAdapter);
        initUsb();
    }

    private void initUsb() {
        devices = new ArrayList<>();
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        USBHelper.findDevices(mUsbManager, devices);
        usbListAdapter.notifyData(devices);
        usbListAdapter.setOnItemClickListener(this);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
    }


    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.i(TAG, "onReceive: success");
                    } else {
                    }
                }
            }
        }
    };
    UsbConnection usbConnection;

    @Override
    public void onItemClick(UsbDevice item, int position) {
        try {
            usbConnection = USBHelper.getConnection(item);
            usbConnection.connect(mUsbManager);
            mPos.Set(usbConnection);
            Toast.makeText(this, "链接成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usbConnection != null) {
            usbConnection.close();
        }
        if (mUsbManager != null) {
            mUsbManager = null;
        }
        this.unregisterReceiver(mUsbReceiver);
    }

    public void print(View view) {
        es.submit(new Runnable() {
            @Override
            public void run() {
                Prints.PrintTicket(getApplicationContext(), mPos);
            }
        });
    }
}