package com.tenz.smsmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.tenz.smsmonitor.entity.SMSEntity;

/**
 * 这种广播监听被大多数厂商拦截，不可用,摒弃
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //解析短信内容
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : pdus) {
            //封装短信参数的对象
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
            String number = sms.getOriginatingAddress();
            String body = sms.getMessageBody();
            SMSEntity smsEntity = new SMSEntity();
            smsEntity.setNumber(number);
            smsEntity.setContent(body);
//            EventBus.getDefault().post(new Events.SMSEvent(smsEntity));
        }
    }

}
