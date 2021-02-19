package com.tenz.smsmonitor.adapter;

import android.content.Context;

import com.tenz.smsmonitor.R;
import com.tenz.smsmonitor.base.BaseQuickAdapter;
import com.tenz.smsmonitor.base.BaseViewHolder;
import com.tenz.smsmonitor.entity.SMSEntity;

import java.util.List;

public class SMSAdapter extends BaseQuickAdapter<SMSEntity> {

    public SMSAdapter(Context context, int layoutResID, List<SMSEntity> data) {
        super(context, layoutResID, data);
    }

    @Override
    public void convert(BaseViewHolder holder, SMSEntity item) {
        holder.setText(R.id.tv_content,"内容: " + item.getContent());
        holder.setText(R.id.tv_number,"发送者: " + item.getNumber());
        holder.setText(R.id.tv_time,"时间: " + item.getTime());
    }

}
