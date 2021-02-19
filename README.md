# SMSMonitor
Android实现监听短信

第一种：BroadcastReceiver，这种广播监听被大多数厂商拦截，不可用
第二种：ContentObserver监听（可用），onChange方法会被多次调用，需要处理重复

![image](https://github.com/TenzLiu/SMSMonitor/tree/master/image/1.png)

