package com.tenz.smsmonitor.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.tenz.smsmonitor.util.LogUtil;

public class App extends Application {

    private static App mApplication;
    protected static Context mContext;
    protected static Handler sHandler;
    protected static int sMainThreadId;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = this;
        sHandler = new Handler();
        sMainThreadId = android.os.Process.myTid();
        LogUtil.init(true);
    }


    public static App getApplication(){
        return mApplication;
    }

    public static Context getContext(){
        return mContext;
    }

    /**
     * 是否运行在UI主线程
     *
     * @return
     */
    public static boolean isRunOnUIThread() {
        int myTid = android.os.Process.myTid();
        if (myTid == sMainThreadId) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 运行在UI主线程
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            runnable.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            sHandler.post(runnable);
        }
    }

}
