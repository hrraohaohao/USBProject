package com.hao.usbproject.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author raohaohao
 * @version 1.0
 * @data 2023/4/7
 */
public class UsbConnection extends MIO {

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

    private Vector<Byte> rxBuffer = new Vector();


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
    public void connect(UsbManager usbManager) throws Exception {
        // 链接
        deviceConnection = usbManager.openDevice(usbDevice);
        // 是否占用

        if (null == deviceConnection) {
            throw new Exception("UsbDeviceConnection  为null！Open Device Failed");
        }

        if (!deviceConnection.claimInterface(usbInterface, true)) {
            deviceConnection.close();
            throw new Exception("ClaimInterface Failed");
        }
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
        this.mMainLocker.lock();
        int nBytesWritten = 0;
        try {
            while (nBytesWritten < count) {
                int nPackageSize = Math.min(this.outEndpoint.getMaxPacketSize(), count - nBytesWritten);
                byte[] data = new byte[nPackageSize];
                System.arraycopy(buffer, offset + nBytesWritten, data, 0, data.length);
                int nSended = this.deviceConnection.bulkTransfer(this.outEndpoint, data, data.length, 2147483647);
                if (nSended < 0) {
                    throw new Exception("Write Failed");
                }
                nBytesWritten += nSended;
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.close();
            nBytesWritten = -1;
        } finally {
            this.mMainLocker.unlock();
        }
        this.rxBuffer.clear();
        return nBytesWritten;
    }

    @Override
    public int Read(byte[] buffer, int offset, int count, int timeout) {
        this.mMainLocker.lock();
        int nBytesReaded = 0;
        try {
            long time = System.currentTimeMillis();
            label105:
            while (true) {
                while (true) {
                    if (System.currentTimeMillis() - time >= (long) timeout) {
                        break label105;
                    }
                    if (nBytesReaded == count) {
                        break label105;
                    }

                    if (this.rxBuffer.size() > 0) {
                        buffer[offset + nBytesReaded] = (Byte) this.rxBuffer.get(0);
                        this.rxBuffer.remove(0);
                        ++nBytesReaded;
                    } else {
                        int nPackageSize = this.inEndpoint.getMaxPacketSize();
                        byte[] receive = new byte[nPackageSize];
                        int nReceived = this.deviceConnection.bulkTransfer(this.inEndpoint, receive, receive.length, 100);
                        if (nReceived > 0) {
                            for (int i = 0; i < nReceived; ++i) {
                                this.rxBuffer.add(receive[i]);
                            }
                        }
                    }
                }
            }
        } catch (Exception var15) {
            Log.e("USBBaseIO", var15.toString());
            this.close();
            nBytesReaded = -1;
        } finally {
            this.mMainLocker.unlock();
        }

        return nBytesReaded;
    }

}
