package com.hao.usbproject.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

/**
 * @author raohaohao
 * @version 1.0
 * @data 2023/4/7
 */
public class UsbConnection extends BaseUSB{

    private UsbDevice usbDevice;
    private UsbInterface usbInterface;

    private UsbEndpoint inEndpoint;

    private UsbEndpoint outEndpoint;

    private UsbDeviceConnection deviceConnection;

    public UsbEndpoint getInEndpoint() {
        return inEndpoint;
    }

    public UsbEndpoint getOutEndpoint() {
        return outEndpoint;
    }

    public UsbDeviceConnection getDeviceConnection() {
        return deviceConnection;
    }

    public UsbConnection(UsbDevice usbDevice, UsbInterface usbInterface, UsbEndpoint inEndpoint, UsbEndpoint outEndpoint) {
        this.usbDevice = usbDevice;
        this.usbInterface = usbInterface;
        this.inEndpoint = inEndpoint;
        this.outEndpoint = outEndpoint;
    }

    /**
     * 设备连接
     *
     * @param usbManager
     * @throws NullPointerException
     */
    public void connect(UsbManager usbManager) throws NullPointerException {
        if (deviceConnection == null) {
            throw new NullPointerException("UsbDeviceConnection  为null！");
        }
        // 链接
        deviceConnection = usbManager.openDevice(usbDevice);
        // 是否占用
        deviceConnection.claimInterface(usbInterface, true);
    }

    /**
     * 设备断开
     */
    public void close() {
        if (deviceConnection != null) {
            deviceConnection.releaseInterface(usbInterface);
            deviceConnection.close();
        }
    }

    @Override
    public int Write(byte[] buffer, int offset, int count) {
        return super.Write(buffer, offset, count);



    }

    @Override
    public int Read(byte[] buffer, int offset, int count, int timeout) {
        return super.Read(buffer, offset, count, timeout);


    }
}
