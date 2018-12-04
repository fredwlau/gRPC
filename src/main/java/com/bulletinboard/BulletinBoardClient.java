package com.bulletinboard;

import io.grpc.*;
import io.grpc.stub.*;
import java.util.*;

public class BulletinBoardClient {
	
	static ManagedChannel channel;
	static BulletinBoardRPCGrpc.BulletinBoardRPCStub asyncStub;
	static BulletinBoardRPCGrpc.BulletinBoardRPCBlockingStub blockingStub;
	

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		int exit = 0;
		channel = ManagedChannelBuilder.forTarget("localhost:8080")
		        .usePlaintext()
		        .build();
		
		asyncStub = BulletinBoardRPCGrpc.newStub(channel);
		blockingStub = BulletinBoardRPCGrpc.newBlockingStub(channel);
		
		System.out.println("Client successfully connected to Fred's Bulletin Board");
		
		while(exit == 0) {
			System.out.println("AVAILABLE COMMANDS");
			System.out.println("-----------------------------------------------------");
			System.out.println("1. post");
			System.out.println("2. get");
			System.out.println("3. delete");
			System.out.println("4. list");
			System.out.println("5. exit");
			System.out.println("-----------------------------------------------------");
			System.out.println("Please select a command number (1-5):");
		
			Scanner sc = new Scanner(System.in);
			int choice = 0;
			String input;
			boolean isNotValid = true;
			while(isNotValid) {
				input = sc.next();
				try {
					choice = Integer.parseInt(input);
					isNotValid = false;
				}catch(NumberFormatException e) {
					System.out.println("That is not a valid input.  Please select a valid command (1-5)");
					System.out.println("-----------------------------------------------------");
					System.out.println("AVAILABLE COMMANDS");
					System.out.println("1. post");
					System.out.println("2. get");
					System.out.println("3. delete");
					System.out.println("4. list");
					System.out.println("5. exit");
					System.out.println("-----------------------------------------------------");
				}
			}
			
			if(choice==1) {
					
				Scanner scanner = new Scanner(System.in);
				System.out.println("Enter your post title:");
				
				String title = scanner.nextLine();
				
				System.out.println("Enter your post body:");
				String body = scanner.nextLine();
				
				com.bulletinboard.postMessage request =
						com.bulletinboard.postMessage.newBuilder()
						.setTitle(title)
						.setBody(body)
						.build();
				
				com.bulletinboard.serverReply response =
					blockingStub.post(request);
				
				System.out.println(response);
				System.out.println("-----------------------------------------------------");
			}
			
			else if(choice==2) {
				
				Scanner scanner = new Scanner(System.in);
				System.out.println("Enter the post title you want to retrieve:");
				
				String title = scanner.nextLine();
				
				com.bulletinboard.getMessage request =
						com.bulletinboard.getMessage.newBuilder()
						.setTitle(title)
						.build();
				
				com.bulletinboard.postMessage response =
					blockingStub.get(request);
				
				System.out.println(response);
				System.out.println("-----------------------------------------------------");
			}
			
			else if(choice==3) {
				
				Scanner scanner = new Scanner(System.in);
				System.out.println("Enter the post title you want to delete:");
				
				String title = scanner.nextLine();
				
				com.bulletinboard.getMessage request =
						com.bulletinboard.getMessage.newBuilder()
						.setTitle(title)
						.build();
				
				com.bulletinboard.serverReply response =
					blockingStub.delete(request);
				
				System.out.println(response);
				System.out.println("-----------------------------------------------------");
			}
			
			else if(choice ==4) {
				
				com.bulletinboard.listMessage request =
						com.bulletinboard.listMessage.newBuilder()
						.setHolder("list")
						.build();
				
				Iterator<com.bulletinboard.postMessage> responses;
				responses = blockingStub.list(request);
				System.out.println("Posts:");
				for(int i = 1; responses.hasNext(); i++) {
					com.bulletinboard.postMessage response = responses.next();
					System.out.println(+i+". "+ response.getTitle());
				}
				System.out.println("-----------------------------------------------------");
			}
			
			else if(choice ==5) {
				channel.shutdown();
				System.out.println("Goodbye");
				exit = 1;
			}
			
			else {
				System.out.println("That is not a valid input.  Please select a valid command (1-5)");
				System.out.println("-----------------------------------------------------");
			}
		}
		
	}

}
