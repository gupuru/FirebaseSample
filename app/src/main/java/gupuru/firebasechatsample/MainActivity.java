package gupuru.firebasechatsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayAdapter<String> adapter;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mConditionRef = databaseReference.child("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        editText = (EditText) findViewById(R.id.edit_text);
        sendButton = (Button) findViewById(R.id.button_send);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConditionRef.child("post").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message message = child.getValue(Message.class);
                    if (message != null && message.data != null) {
                        adapter.add(message.data);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(adapter.getCount() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                sendMessage(msg);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        mConditionRef.removeEventListener();
    }

    private void sendMessage(String data) {
        Message msg = new Message(data);

        String key = databaseReference.child("post").push().getKey();

        Map<String, Object> postValues = msg.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/post/" + key, postValues);

        databaseReference.updateChildren(childUpdates);

        editText.setText("");
    }
}
