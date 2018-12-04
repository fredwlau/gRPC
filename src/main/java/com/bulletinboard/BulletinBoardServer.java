package com.bulletinboard;

import io.grpc.*;

public class BulletinBoardServer {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		Server server = ServerBuilder.forPort(8080)
		.addService(new BulletinBoardService())
		.build();
		
		server.start();
		System.out.println("Server start success");
		
		server.awaitTermination();
	}

}
