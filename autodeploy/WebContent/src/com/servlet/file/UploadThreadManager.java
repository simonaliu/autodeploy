package com.servlet.file;

import java.util.Vector;

public class UploadThreadManager {
	private Vector<UploadManager> threadPool;
	
	private UploadThreadManager() {
		this.threadPool = new Vector<UploadManager>();
	}
	
	private static UploadThreadManager instance = new UploadThreadManager();
	
	public synchronized static UploadThreadManager getInstance(){
		return instance;
	}
	
	public void addThread(UploadManager thread){
		if(this.threadPool.contains(thread)) return;
		this.threadPool.add(thread);
	}
	
	public boolean remove(UploadManager manager){
		return this.threadPool.remove(manager);
	}
	
	public UploadManager getThreadByUserName(String name){
		for(int i = 0;i<this.threadPool.size();i++){
			if(this.threadPool.get(i).isName(name)){
				return this.threadPool.get(i);
			}
		}
		UploadManager manager = new UploadManager(name);
		this.threadPool.add(manager);
		return manager;
	}
}
