package com.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongodbTest {
	private Mongo mongo;
	private BasicDBObject object;
	private DBCollection collection;
	public MongodbTest(){
	}

	@SuppressWarnings("deprecation")
	public void init(){
		try{
			mongo = new Mongo("159.226.57.12",27017);
			System.out.println("Connect to database successfully!");
			DB db = mongo.getDB("test");
		    collection = db.getCollection("time");
			System.out.println("Collection get successfully!");
		}catch(Exception e){
			 System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	public void insert(BasicDBObject object){
		collection.insert(object);
	}
	
	public void close(){
		mongo.close();
	}
}
