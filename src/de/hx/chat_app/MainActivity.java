package de.hx.chat_app;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	EditText messageEditText;
	Button sendButton;
	static SocketIO socket;
	ListView messageListView;
	ArrayList<String> messageList;
	ArrayAdapter<String> messageListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		messageEditText = (EditText) findViewById(R.id.messageEditText);
		sendButton = (Button) findViewById(R.id.sendButton);

		socket = getSocket();

		sendButton.setOnClickListener(this);
		messageListView = (ListView) findViewById(R.id.messageListView);

		messageList = new ArrayList<String>();
		messageListAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.sym_text_view, messageList);
		messageListView.setAdapter(messageListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private SocketIO getSocket() {
		SocketIO thisSocket = null;
		try {
			thisSocket = new SocketIO("http://192.168.4.100:3000/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thisSocket.connect(new IOCallback() {

			@Override
			public void on(String event, IOAcknowledge ack, Object... args) {
				if ("chat".equals(event) && args.length > 0) {
					messageList.add(args[0].toString());
					messageListAdapter.notifyDataSetChanged();
					messageListView.setSelection(messageListView.getCount() - 1);
				}
			}

			@Override
			public void onMessage(JSONObject json, IOAcknowledge ack) {
			}

			@Override
			public void onMessage(String data, IOAcknowledge ack) {
			}

			@Override
			public void onError(SocketIOException socketIOException) {
				System.out.println("Connection error!");
				// try {
				// socket = new SocketIO("http://192.168.4.100:3000/");
				// } catch (MalformedURLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// socket.reconnect();
			}

			@Override
			public void onDisconnect() {
				// Toast.makeText(MainActivity.this, "DISCONNECTED",
				// Toast.LENGTH_LONG).show();
			}

			@Override
			public void onConnect() {
			}
		});
		return thisSocket;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		socket.emit("chat", messageEditText.getText().toString());
		messageEditText.setText("");
	}
	
	private Runnable scrollList = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			messageListAdapter.notifyDataSetChanged();
			messageListView.setSelection(messageListView.getCount() - 1);
			runOnUiThread(returnRes);
		}
	};
	
	private Runnable returnRes = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			messageListAdapter.notifyDataSetChanged();
		}
	};

}
