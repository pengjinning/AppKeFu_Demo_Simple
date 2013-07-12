使用说明：

1. 在 AndroidManifest.xml 的 <application></application> 中添加

   <activity
            android:name="com.appkefu.lib.ChatActivity"
            android:theme="@android:style/Theme.NoTitleBar" />
            
   <service android:name="com.appkefu.lib.service.AppService" />
   
   <meta-data android:name="appkey" android:value="你的appkey"/>

2. 参见 com.appkefu.demo.simple.MainActivity.java 中
	void startChat(String kefuName) 函数，其中kefuName为客服的用户名
	
3. 用户首次打开会话窗口，系统为此手机用户生成唯一ID（基于OpenUDID,
https://github.com/UASoftware/OpenUDID-Android），如开发者需要基于自己
的用户体系，请到AppKeFu.com咨询客服，或email: appkefu@163.com

4. 用户每次打开会话窗口,会与服务器建立连接，关闭会话窗口时，端口与服务器的连接，
如需用户 在会话窗口未打开的情况下接收来自客服人员的消息，请查看示例：AppKeFuDemoAdvanced
	





版本更新说明;

2013-06-07，0.9
	1.发送文字
	
2013-06-25,0.91
	1.添加请求客服在线状态接口
	
2013-07-06,092
	1.增加appkey验证
	
	
	
	
	
	
	
	
	
	
