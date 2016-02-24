package com.servlet.file;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;

public class SqlManager {

	private final String DRIVER = "com.mysql.jdbc.Driver";
	private final String URL = "jdbc:mysql://159.226.57.12:3306/timetest";
	private final String USER = "root";
	private final String PWD = "root";

	private SqlManager() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static SqlManager instance=new SqlManager();
	
	public static SqlManager instance(){
		return instance;
	}

	public synchronized Connection getConnection() {
		Connection conn = null;
		try {
			conn = (Connection) DriverManager.getConnection(URL, USER, PWD);
			System.out.println("Connect DB Successfully！");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void insert(int port_dep,int port_now,long time){
		try {
			PreparedStatement ps =  getConnection().prepareStatement("insert into timetest(port_deploy,port_now,time) values (?,?,?)");
			ps.setInt(1, port_dep);
			ps.setInt(2, port_now);
			ps.setLong(3, time);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkAveTime(){
		try {
			PreparedStatement portsQuery = getConnection().prepareStatement("select port_deploy from timetest group by port_deploy");
			ResultSet ports = portsQuery.executeQuery();
			while(ports.next()){
				int port = ports.getInt(1);
				PreparedStatement ps = getConnection().prepareStatement("select * from timetest where port_deploy = ? and time >= ?");
				ps.setInt(1, port);
				ps.setInt(2, 20000);
				ResultSet rs = ps.executeQuery();
				int dead_num = 0;
				while(rs.next()){
					dead_num += 1;
				}
				ps.close();
				rs.close();
				
				PreparedStatement ps2 = getConnection().prepareStatement("select time from timetest where port_deploy = ? and time < ?");
				ps2.setInt(1, port);
				ps2.setInt(2, 20000);
				ResultSet rs2 = ps2.executeQuery();
				
				int ave_time = 0;
				int ave_index = 0;
				while(rs2.next()){
					ave_time += rs2.getInt(1);
					ave_index += 1;
				}
				ps2.close();
				rs2.close();
				if(ave_index!=0) 	ave_time = ave_time / ave_index;
				
				PreparedStatement tempPs = getConnection().prepareStatement("select port_deploy from ave_time where port_deploy = ?");
				tempPs.setInt(1, port);
				ResultSet tempRs = tempPs.executeQuery();
				PreparedStatement ps3  = null;
				if(tempRs.next()){
					ps3 =  getConnection().prepareStatement( "update ave_time set ave_time = ? , dead_num = ? where port_deploy = ?");
					ps3.setInt(1, ave_time);
					ps3.setInt(2, dead_num);
					ps3.setInt(3, port);
				}
				else{
					ps3 =  getConnection().prepareStatement("insert into ave_time(port_deploy,ave_time,dead_num) values (?,?,?)");
					ps3.setInt(1, port);
					ps3.setInt(2, ave_time);
					ps3.setInt(3, dead_num);
				}
				ps3.executeUpdate();
				ps3.close();
				tempPs.close();
				tempRs.close();
			}
			portsQuery.close();
			ports.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

