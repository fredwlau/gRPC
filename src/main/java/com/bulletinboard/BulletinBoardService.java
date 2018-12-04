package com.bulletinboard;

import com.bulletinboard.postMessage.Builder;

public class BulletinBoardService extends BulletinBoardRPCGrpc.BulletinBoardRPCImplBase {
	
	public class LinkNode{
		LinkNode next;
		String title;
		String body;
		public LinkNode(String title, String body) {
			this.title=title;
			this.body=body;
		}
		
	}
	LinkNode head;
	
	public void post(String title, String body) {
		
		if(head == null) {
			head = new LinkNode(title, body);
			return;
		}
		LinkNode temp = head;
			while (temp.next!=null) {
				temp=temp.next;
			}
			LinkNode newNode = new LinkNode(title, body);
			temp.next = newNode;
	}
		
	public LinkNode get(String title) {
			
		LinkNode temp = head;
		while(temp!=null) {
			if (temp.title.equals(title)) {
				return temp;
			}
			temp = temp.next;
		}
		return temp;
	}
		
	public boolean delete(String title) {
		LinkNode prev = head;
		//only one post and it is the post we are looking for
		if(prev.title.equals(title) && prev.next==null) {
			head=null;
			return true;
		}
		//head is target but more posts after that
		else if(prev.title.equals(title) && prev.next != null) {
			head = prev.next;
			return true;
		}
		//only one post and it is not target
		else if (prev.next == null) {
			return false;
		}
		else {
			LinkNode temp = prev.next;
			while(temp!=null) {
				if(temp.title.equals(title)) {
					prev.next = temp.next;
					return true;
				}
				prev = prev.next;
				temp = temp.next;
			}
			return false;
		}
	}
	
	
	@Override
	public void get (com.bulletinboard.getMessage request,
	        io.grpc.stub.StreamObserver<com.bulletinboard.postMessage> responseObserver) {
		
		System.out.println(request);
		
		LinkNode tempNode = get(request.getTitle());
		
		com.bulletinboard.postMessage response;
		
		if(tempNode == null) {
			response = com.bulletinboard.postMessage.newBuilder()
					.setTitle("A post with that title does not exist")
					.build();
		}
		
		else {
			response = com.bulletinboard.postMessage.newBuilder()
					.setTitle(tempNode.title)
					.setBody(tempNode.body)
					.build();
		}
		
		responseObserver.onNext(response);
		
		responseObserver.onCompleted();
	}
	
	@Override
	public void post (com.bulletinboard.postMessage request,
	        io.grpc.stub.StreamObserver<com.bulletinboard.serverReply> responseObserver) {
		
		System.out.println(request);
		
		post(request.getTitle(), request.getBody());
		
		com.bulletinboard.serverReply response = com.bulletinboard.serverReply.newBuilder()
				.setSuccess("Successfully posted")
				.build();
		
		responseObserver.onNext(response);
		
		responseObserver.onCompleted();
	}
	
	@Override
	public void list (com.bulletinboard.listMessage request,
	        io.grpc.stub.StreamObserver<com.bulletinboard.postMessage> responseObserver){
		
		System.out.println(request);
		
		LinkNode temp = head;
		
		if(temp == null) {
			com.bulletinboard.postMessage response = com.bulletinboard.postMessage.newBuilder()
					.setTitle("No posts to list")
					.build();
			System.out.println(response);
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		
		else {
		
			while(temp!=null) {
				com.bulletinboard.postMessage response = com.bulletinboard.postMessage.newBuilder()
						.setTitle(temp.title)
						.build();
				responseObserver.onNext(response);
				System.out.println(response);
				temp = temp.next;
			}
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void delete (com.bulletinboard.getMessage request,
	        io.grpc.stub.StreamObserver<com.bulletinboard.serverReply> responseObserver){
		
		System.out.println(request);
		
		boolean didDelete = delete(request.getTitle());
		
		com.bulletinboard.serverReply response;
		
		if(didDelete) {
			response = com.bulletinboard.serverReply.newBuilder()
					.setSuccess("Post successfully deleted")
					.build();
		}
		else {
			response = com.bulletinboard.serverReply.newBuilder()
					.setSuccess("Post not found")
					.build();
		}
		
		responseObserver.onNext(response);
		
		responseObserver.onCompleted();
	}
}
