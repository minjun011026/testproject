package com.unit_3.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json");
    String Word=" ";
    OkHttpClient client = new OkHttpClient();
    ImageButton next_btn;
    ImageButton lang_btn;
    String getWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String language = intent.getStringExtra("language");
        String level = intent.getStringExtra("level");

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        next_btn = findViewById(R.id.next_btn);
        lang_btn = findViewById(R.id.lang_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        String setting = "Show one random " +language+" word at " + level +" level. without meaning";
        //addToChat(setting, Message.SENT_BY_ME);
        callAPI(setting);
        sendButton.setOnClickListener((v) -> {
            String answer = messageEditText.getText().toString().trim();
            addToChat(answer, Message.SENT_BY_ME);
            answer = language +" "+ Word +"는 한국어로 " + answer +"란 뜻이야?";
            getWord=Word;
            messageEditText.setText("");
            callAPI(answer);
            sendButton.setVisibility(View.INVISIBLE);
        });

        next_btn.setOnClickListener((v -> {
            Intent reintent = new Intent(MainActivity.this,MainActivity.class);
            intent.putExtra("language",language);
            intent.putExtra("level",level);
            startActivity(intent);
        }));

        lang_btn.setOnClickListener((v->{
            Intent toMainIntent = new Intent(MainActivity.this,SubActivity.class);
            startActivity(toMainIntent);
        }));

    }

    void addToChat(String message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        addToChat(response,Message.SENT_BY_BOT);
    }
    void callAPI(String question){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role","user");
            obj.put("content",question);
            messageArr.put(obj);
            jsonBody.put("messages",messageArr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("\n" +
                        "https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                        .getJSONObject("message")
                                                .getString("content");
                        Word = result.trim();
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    addResponse("Failed to load response due to" + response.body().string());
                }
            }
        });
    }
}