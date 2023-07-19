package algonquin.cst2335.moul0084;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import algonquin.cst2335.moul0084.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.moul0084.databinding.ReceiveMessageBinding;
import algonquin.cst2335.moul0084.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;

    private RecyclerView.Adapter myAdapter;
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                // get the data from database then load the RecyclerView
                messages.addAll( mDAO.getAllMessages() );
                runOnUiThread( () ->  binding.recyclerView.setAdapter( myAdapter ));
            });
        }
        binding.sendButton.setOnClickListener(click ->{
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean type = true;
            messages.add(new ChatMessage(msg, currentDateandTime, type));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");  //clears the text input field
        });
        binding.receiveButton.setOnClickListener(click ->{
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean type = false;
            messages.add(new ChatMessage(msg, currentDateandTime, type));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");  //clears the text input field
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.message);
                holder.timeText.setText(obj.timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                ChatMessage txt = messages.get(position);
                if(txt.isSentButton()){
                    return 0;
                }else{
                    return 1;
                }
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{

                int position = getAbsoluteAdapterPosition();
                RecyclerView.ViewHolder newHolder = myAdapter.onCreateViewHolder(null, myAdapter.getItemViewType(position));

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message:"+messageText.getText());
                builder.setTitle("question");
                builder.setNegativeButton("No",(dialog, cl) -> { });
                builder.setPositiveButton("Yes",(dialog, cl) -> {
                    ChatMessage m = messages.get(position);
                    Executor thread1 = Executors.newSingleThreadExecutor();
                    thread1.execute(()-> {
                        mDAO.deleteMessage(m);
                        messages.remove(position);

                        runOnUiThread(() -> {
                            myAdapter.notifyDataSetChanged();
                        });
                    });
                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click -> {
                                Executor thread2 = Executors.newSingleThreadExecutor();
                                thread2.execute(() -> {
                                    mDAO.insertMessage(m);
                                    messages.add(position, m);
                                    runOnUiThread(() -> {
                                        myAdapter.notifyDataSetChanged();
                                    });
                                });
                            })
                            .show();
                }).create().show();
            });
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}