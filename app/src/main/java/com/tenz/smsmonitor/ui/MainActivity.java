package com.tenz.smsmonitor.ui;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tenz.smsmonitor.R;
import com.tenz.smsmonitor.adapter.SMSAdapter;
import com.tenz.smsmonitor.base.BaseActivity;
import com.tenz.smsmonitor.db.DBManager;
import com.tenz.smsmonitor.entity.SMSEntity;
import com.tenz.smsmonitor.event.Events;
import com.tenz.smsmonitor.receiver.SMSObserver;
import com.tenz.smsmonitor.util.LogUtil;
import com.tenz.smsmonitor.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.rv_sms)
    RecyclerView rv_sms;

    private SMSAdapter smsAdapter;
    private List<SMSEntity> smsDataList;

    private int PERMISSION_STORAGE_CODE = 10001;

    String[] PERMS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS};

    private SMSObserver smsObserver;
    protected static final int MSG_INBOX = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INBOX:
                    handleSMS();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_sms.setLayoutManager(new LinearLayoutManager(this));
        smsDataList = new ArrayList<>();
        smsAdapter = new SMSAdapter(this,R.layout.item_sms,smsDataList);
        rv_sms.setAdapter(smsAdapter);
    }


    @Override
    protected void initData() {
        super.initData();
        requestPermission();
        smsObserver = new SMSObserver(this, mHandler);
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true, smsObserver);// 注册监听短信数据库的变化

        List<SMSEntity> smsEntityAll = DBManager.newInstance().playSongDao().getSMSByStatus(0);
        if(smsEntityAll.size() > 0){
            smsDataList.addAll(smsEntityAll);
            smsAdapter.notifyDataSetChanged();
        }
    }

    private void requestPermission(){
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事

        } else {
            // 没有申请过权限，现在去申请
            /**
             *@param host Context对象
             *@param rationale  权限弹窗上的提示语。
             *@param requestCode 请求权限的唯一标识码
             *@param perms 一系列权限
             */
            EasyPermissions.requestPermissions(this, "权限申请失败", PERMISSION_STORAGE_CODE, PERMS);
        }
    }

    /**
     * 接收短信
     * @param smsEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeEvent(Events.SMSEvent smsEvent){
//        smsDataList.add(smsEvent.getSmsEntity());
//        smsAdapter.notifyDataSetChanged();
    }

    /**
     * 处理短信
     */
    private void handleSMS() {
        LogUtil.d("SMSObserver handleSMS:---------");
        Cursor cursor = null;
        // 添加异常捕捉
        try {
            cursor = getContentResolver().query(
                    Uri.parse("content://sms/inbox"),
                    new String[] { "_id", "address", "read", "body", "date" },
                    null, null, "date desc limit 1"); // datephone想要的短信号码
            if (cursor != null) {
                String _id = "";
                String body = "";
                String address = "";
                String time = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                while (cursor.moveToNext()) {
                    _id = cursor.getString(cursor.getColumnIndex("_id"));// 在这里获取短信信息id

                    SMSEntity smsEntityById = DBManager.newInstance().playSongDao().getSMSById(_id);
                    if(null == smsEntityById){
                        body = cursor.getString(cursor.getColumnIndex("body"));// 在这里获取短信信息内容
                        address = cursor.getString(cursor.getColumnIndex("address"));// 在这里获取短信地址
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        time = dateFormat.format(new Date(Long.parseLong(date)));
                        LogUtil.d("SMSObserver handleSMS:---------"+_id + "****" + time);
                        SMSEntity smsEntity = new SMSEntity();
                        smsEntity.setId(_id);
                        smsEntity.setNumber(address);
                        smsEntity.setContent(body);
                        smsEntity.setTime(time);
                        smsEntity.setStatus(0);
                        DBManager.newInstance().playSongDao().insert(smsEntity);

                        smsDataList.add(smsEntity);
                        smsAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        new Handler().postDelayed(new Runnable(){
            public void run() {

            }
        }, 2000);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showToast("权限申请失败");
        finish();
    }

}