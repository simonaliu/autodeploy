package com.servlet.file;

import com.ssh.Shell;

public class UploadManager implements Runnable {
	private String _name;
	private int _ins_num;
	
	
	private int step = 0;
	private Shell shell;
	private long start_time = 0,end_time = 0;
	public boolean flag;
	
	public UploadManager(String name){
		this._name = name;
		this.step = 0;
	}
	
	public void setInsNum(int ins_num){
		this._ins_num = ins_num;
		this.start_time = System.currentTimeMillis();
		this.shell = new Shell("159.226.57.11", 22, "liulinyu", "liulinyu123");
		String[] commands = {
				"scp liulinyu@159.226.57.12:/home/liulinyu/docker/tomcat/webapps/AutoDeploy/upload/*.war /home/liulinyu/docker_test/tomcat7/tomcat/webapps/",
				"cd docker_test", " docker build -t fly/test . " };
		if(shell.executeCommands(commands)){
			this.flag=true;
		}
	}
	
	public void doUpload(){
		if (step >=  _ins_num){
			step = _ins_num;
			this.flag = false;
			this.end_time = System.currentTimeMillis();
			System.out.println(UploadThreadManager.getInstance().remove(this));
			return;
		}
		//String index = _name.substring(4);
		//int _index = Integer.parseInt(index) * 10000;
		int _index = 30000;
		int port =  30000 + step;
		String[] execute = { " docker run -m 256m -d -p "+ port + ":8080  fly/test " };
		if (shell.executeCommands(execute)) {
			step++;
			long cur = System.currentTimeMillis();
			new TimeTest(port, cur, _index).init();
		}	
	}
	
	
	
	public boolean isName(String name){
		return this._name.equals(name);
	}
	
	public long getTimeInSeconds(){
		if(this.end_time>0){
			return (this.end_time-this.start_time)/1000;
		}
		else{
			long time = System.currentTimeMillis();
			return (time-this.start_time)/1000;
		}
	}
	
	public int getStep(){
		return step+1;
	}
	
	public int getProgress(){
		return (int)Math.floor(((double)(step+1)/(double)_ins_num * 100));
	}
	
	@Override
	public void run() {
		while(flag){
			doUpload();
		}
	}
}
