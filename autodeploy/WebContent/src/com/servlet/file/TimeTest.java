package com.servlet.file;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class TimeTest {
	int port,index;
	long cur;
	boolean flag = false;
	//MongodbTest mongo;
	
	public TimeTest(int port,long cur,int index) {
		this.port = port;
		this.cur = cur;
		this.index = index;
	}
	

	public void init(){
		//mongo = new  MongodbTest();
		//mongo.init();
		for(int i = 0;i<=port - index;i++){
			System.out.println("build request :cur port:" + TimeTest.this.port + ",port:"+ (index+i) + ",cur time:" + System.currentTimeMillis());
			new Thread(new TimeHolder(index+i)).start();
		}
		flag = true;
	}
	
	class TimeHolder implements Runnable{
		int _port;
		boolean _flag;
		
		public TimeHolder(int port) {
			_port = port;
			_flag = true;
		}
		
		@Override
		public void run() {
			while(_flag){
				if(flag){
					System.out.println("request begin :cur port:" + TimeTest.this.port + ",port:"+_port+ ",cur time:" + System.currentTimeMillis());
					try {
						long resp_time = getUrl(_port);
						SqlManager.instance().insert(TimeTest.this.port , _port, resp_time);
						//updateDB(_port,resp_time);
					} catch (IOException e) {
						e.printStackTrace();
					}
					_flag = false;
				}
			}
		}


		private long getUrl(int port) throws IOException,SocketTimeoutException{
			String url = "http://159.226.57.11:" + port + "/12";
			URL getUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);		
			try{			
				conn.connect();
				if(conn.getResponseCode()!=200){
					getUrl(port);
				}
			}catch(ConnectException e){
				getUrl(port);
			}catch(SocketTimeoutException e){
				return 20000;
			}
			
			long cur2 = System.currentTimeMillis();
			System.out.println("cur port:" + TimeTest.this.port + ",port:"+port + ",time:"+(cur2-cur));
			return cur2-cur;
		}
		
		/*
		private void updateDB(int port,long resp_time){
			BasicDBObject port_time = new BasicDBObject();
			port_time.put(String.valueOf(port), (int)(resp_time));
			BasicDBObject  object = new BasicDBObject();
			object.put("port_info", port_time);
			object.put("port_now", TimeTest.this.port);
			mongo.insert(object);
			System.out.println(object);
		}
		*/
	}
}
