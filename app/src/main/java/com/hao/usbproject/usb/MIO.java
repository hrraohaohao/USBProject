package com.hao.usbproject.usb;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author raohaohao
 * @version 1.0
 * @data 2023/4/6
 */
public class MIO {

    public final ReentrantLock mMainLocker = new ReentrantLock();

    public int Write(byte[] buffer, int offset, int count) {
        return -1;
    }

    public int Read(byte[] buffer, int offset, int count, int timeout) {
        return -1;
    }

    public void SkipAvailable() {
    }

    public boolean IsOpened() {
        return true;
    }

}
