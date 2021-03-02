# SMSMonitor
Android实现监听短信

第一种：BroadcastReceiver，这种广播监听会被某些厂商拦截，但我测试的几款手机都没问题

第二种：ContentObserver监听，onChange方法会被多次调用，需要处理重复

![image](https://github.com/TenzLiu/SMSMonitor/blob/master/image/1.jpg)

