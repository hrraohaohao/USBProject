package com.hao.usbproject.adapter;

import android.content.Context;
import android.hardware.usb.UsbDevice;

import androidx.recyclerview.widget.RecyclerView;

import com.hao.usbproject.R;
import com.hao.usbproject.databinding.ItemUsbInfoBinding;


public class UsbListAdapter extends BaseDataBindingAdapter<UsbDevice, ItemUsbInfoBinding> {
    public UsbListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_usb_info;
    }

    @Override
    protected void onBindItem(ItemUsbInfoBinding binding, UsbDevice item, RecyclerView.ViewHolder holder) {
        binding.deviceName.setText(item.getDeviceName());
        binding.manufacturerName.setText("制造商:" + item.getManufacturerName());
        binding.productName.setText("产品:" + item.getProductName());
        binding.productId.setText("PID:" + item.getProductId());
    }
}
