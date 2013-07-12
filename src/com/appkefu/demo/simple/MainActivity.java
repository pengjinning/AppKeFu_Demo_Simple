package com.appkefu.demo.simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.appkefu.demo.simple.R;
import com.appkefu.lib.ChatActivity;
import com.appkefu.lib.service.UsernameAndKefu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
开放两个接口：
1.Utils.isKeFuOnline("kefu1", "appkey")，获取客服的在线状态
2.参照startChat(String kefuName)方法启动聊天窗口
 

*/
public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private checkKeFuOnlineTask checkTask = null;
	
	//不要修改
	private static final String SERIAL_KEY = "com.appkefu.lib.username.serialize";
	
	private Button chatSimple;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		chatSimple = (Button)findViewById(R.id.chat_simple);
		chatSimple.setOnClickListener(listener);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
		if(checkTask == null)
		{
			checkTask = new checkKeFuOnlineTask();
			//检测客服是否在线，您需要将kefu1替换成您自己的客服账号
			checkTask.execute("admin");		
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		
	}

	private OnClickListener listener = new OnClickListener() {	    
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	switch(v.getId()){
        	case R.id.chat_simple:
        		//您需要用替换掉kefu1为您自己的客服账号
        		startChat("testusername","admin");
        		break;
        	default:
        		break;
        	}
        }
	};
	
	/**
	 * @param kefuName 客服的用户名
	 */
	private void startChat(String username,String kefuName) {
		
		Log.d(TAG, "startChat:"+kefuName);
		
		String jid = kefuName + "@appkefu.com";
		Intent intent = new Intent(this, ChatActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		UsernameAndKefu usernameAndKefu = new UsernameAndKefu();
		usernameAndKefu.setUsername(username);
		usernameAndKefu.setKefuJID(jid);
		
		//Contact contact = new Contact(jid);
		//intent.setData(contact.toUri());
		Bundle mbundle = new Bundle();
		mbundle.putSerializable(SERIAL_KEY, usernameAndKefu);
		intent.putExtras(mbundle);
		
		startActivity(intent);	
		
    }
	 
	/*
	 * 发送HTTP GET请求：http://appkefu.com:5280/status/kefu1/appkefu.com/xml,
	 * 其中，要将kefu1替换为您自己的客服账号
	 //不在线的情况是这样子的：
	  <?xml version="1.0" encoding="utf-8"?>
	  <presence user="kefu1" server="appkefu.com"/>
	 //在线的情况是这样子的：
	  <?xml version="1.0" encoding="utf-8"?>
	  <presence user="kefu1" server="appkefu.com">
		<resource name="AppKeFu_PC" show="available" priority="0">是, 我在这里.</resource>
	  </presence> 
	  在解析的时候，需要去掉：<?xml version="1.0" encoding="utf-8"?>，所以有了下列代码中的字符串截取
	 */
	private class checkKeFuOnlineTask extends AsyncTask<String, Void, Boolean>{
		
		private String kefuUsername;
		//private String appKey;
		
		checkKeFuOnlineTask(){
			Log.d(TAG, "checkKeFuOnlineTask construct");
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			kefuUsername = params[0];
			//appKey = params[1];
			
			boolean isAvailable = false;

			URL url;
			try {
				
				url = new URL("http://appkefu.com:5280/status/"+kefuUsername+"/appkefu.com/xml");
			    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();			    
			    httpURLConnection.connect();    
		        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));    
		        
		        String result = "",lines;   
		        while ((lines = reader.readLine()) != null) {    
		        	result += lines;
		        }    	
		        System.out.println(result);
		        result = result.substring(38);
		        
		        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		        factory.setNamespaceAware(true);  
		        XmlPullParser xpp = factory.newPullParser();  
		        xpp.setInput(new StringReader(result));  
		        int eventType = xpp.getEventType();  
		        while (eventType != XmlPullParser.END_DOCUMENT) 
		        {  
		          if(eventType == XmlPullParser.START_TAG) 
		          {  
		              if(xpp.getName().equals("resource"))
		              {
		            	  if(xpp.getAttributeValue("", "show").equals("available"))
		            	  {		            		  
		            		  isAvailable = true;
		            	  }	            	  
		              }		            
		          } 		          
		          eventType = xpp.next();  
		        }  
		        reader.close();    
		        httpURLConnection.disconnect();    		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			} 
						
			return isAvailable;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
			if(result)
			{
				System.out.println("is online");
				chatSimple.setText(R.string.chat_simple_online);//客服在线
			}
			else
			{
				System.out.println("is offline");
				chatSimple.setText(R.string.chat_simple_offline);//客服离线
			}			
		} 			
	}
	
}
