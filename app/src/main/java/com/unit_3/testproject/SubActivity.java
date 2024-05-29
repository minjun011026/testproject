package com.unit_3.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    private Button btn_move;
    private Button btn_personality;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_ai);
        btn_move = findViewById(R.id.btn_move);
        EditText language = findViewById(R.id.language);
        EditText level = findViewById(R.id.level);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity.this,MainActivity.class);
                intent.putExtra("language",language.getText().toString());
                intent.putExtra("level",level.getText().toString());
                startActivity(intent);
            }
        });
    }
}
