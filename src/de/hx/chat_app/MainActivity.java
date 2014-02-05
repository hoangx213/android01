package de.hx.chat_app;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener, IOCallback {

	EditText messageEditText;
	Button sendButton;
	static SocketIO socket;
	ListView messageListView;
	ArrayList<String> messageList;
	ArrayAdapter<String> messageListAdapter;
	boolean flag = false;
	Chat chat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		messageEditText = (EditText) findViewById(R.id.messageEditText);
		sendButton = (Button) findViewById(R.id.sendButton);

		sendButton.setOnClickListener(this);
		messageListView = (ListView) findViewById(R.id.messageListView);

		messageList = new ArrayList<String>();
		messageListAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.sym_text_view, messageList);
		messageListView.setAdapter(messageListAdapter);
		startChat();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startChat(){
		chat = new Chat(this, messageListAdapter);
		chat.start();
	}
	
	@Override
	public void onClick(View v) {
		chat.sendMessage(messageEditText.getText().toString());
		messageEditText.setText("");
	}


	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		// TODO Auto-generated method stub
		if ("chat".equals(event) && args.length > 0) {
			messageList.add(args[0].toString());
			chat.updateMessageList(this);
		}
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(SocketIOException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		
	}
}
