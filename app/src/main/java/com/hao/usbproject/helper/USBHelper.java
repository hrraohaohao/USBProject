package com.hao.usbproject.helper;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.hao.usbproject.usb.UsbConnection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class USBHelper {

    private static USBHelper mUSBHelper;
    private static Context mContext;

    private USBHelper(Context _context) {


    }

    public static USBHelper getInstance(Context _context) {
        if (mUSBHelper == null) mUSBHelper = new USBHelper(_context);
        mContext = _context;
        return mUSBHelper;
    }

    /**
     * 获取设备
     *
     * @param usbManager
     * @param devices
     */
    public static void findDevices(UsbManager usbManager, List<UsbDevice> devices) {
        Map<String, UsbDevice> deviceMap = usbManager.getDeviceList();
        Collection<UsbDevice> values = deviceMap.values();
        Iterator<UsbDevice> iterator = values.iterator();
        while (iterator.hasNext()) {
            devices.add(iterator.next());
        }
    }

    /**
     * 连接设备
     *
     * @param usbDevice
     * @return
     */
    public static UsbConnection getConnection(UsbDevice usbDevice) {

        for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_MASS_STORAGE
                    && usbInterface.getInterfaceSubclass() == 0x06
                    && usbInterface.getInterfaceProtocol() == 0x50) {
                //每个存储设备一定有两个端点：in 和 out
                UsbEndpoint outEndpoint = null, inEndpoint = null;
                for (int j = 0; j < usbInterface.getEndpointCount(); j++) {
                    UsbEndpoint endpoint = usbInterface.getEndpoint(j);
                    if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                        outEndpoint = endpoint;
                    } else {
                        inEndpoint = endpoint;
                    }
                }
                return new UsbConnection(usbDevice, usbInterface, inEndpoint, outEndpoint);
            }
        }
        return null;
    }


}
