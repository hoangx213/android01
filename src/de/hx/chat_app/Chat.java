package de.hx.chat_app;

import io.socket.IOCallback;
import io.socket.SocketIO;

import java.net.MalformedURLException;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class Chat extends Thread {
	
	private SocketIO socket;
	private IOCallback callback;
	private ArrayAdapter<String> adapter;
	
	public Chat(IOCallback callback, ArrayAdapter<String> adapter){
		this.callback = callback;
		this.adapter = adapter;
	}
	
	public void run(){
		try{
			socket = new SocketIO("http://192.168.4.100:3000/", callback);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
	}
	
	public void sendMessage(String message){
		socket.emit("chat", message);
	}
	
	public void updateMessageList(Activity activity){
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();
			}
		});
	}
}    
