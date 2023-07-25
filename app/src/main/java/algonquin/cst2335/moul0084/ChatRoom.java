package algonquin.cst2335.moul0084;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import algonquin.cst2335.moul0084.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.moul0084.databinding.ReceiveMessageBinding;
import algonquin.cst2335.moul0084.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;

    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;
    MessageDatabase db;

    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);
        boolean IAmTablet = fragmentLocation != null;
        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
            //add all privious database
            List<ChatMessage> allMessages = mDAO.getAllMessages();
            messages.addAll(allMessages);
        });
        if (messages == null){
            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);
        }
        binding.sendButton.setOnClickListener(click ->{
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean type = true; // modified from true to false

         //   messages.add(new ChatMessage(msg, currentDateandTime, type));
            ChatMessage newMessage = new ChatMessage(msg , currentDateandTime , type);
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                newMessage.id = mDAO.insertMessage(newMessage);
            });
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");  //clears the text input field
        });
        binding.receiveButton.setOnClickListener(click ->{
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean type = false;
           // messages.add(new ChatMessage(msg, currentDateandTime, type));
            ChatMessage newMessage = new ChatMessage(msg , currentDateandTime , type);
            Executor thread2 = Executors.newSingleThreadExecutor();
            thread2.execute(() -> {
                newMessage.id = mDAO.insertMessage(newMessage);
            });
            messages.add(newMessage);
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
        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            if (newMessageValue != null) {
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                fragmentLocation.setVisibility(View.VISIBLE);
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
                tx.add(R.id.fragmentLocation, chatFragment);
                tx.replace(R.id.fragmentLocation, chatFragment);
                tx.addToBackStack(null);
                tx.commit();
            }
        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
/*
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
*/
                    int position = getAbsoluteAdapterPosition();
                    ChatMessage selected = messages.get(position);

                    chatModel.selectedMessage.postValue(selected);
            });
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}