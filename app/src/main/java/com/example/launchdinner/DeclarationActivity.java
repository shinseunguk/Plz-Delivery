package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class DeclarationActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BoardWriteActivity";

    EditText editTextUp, editTextDown, editTextContent;
    Spinner spinner;

    String[] contentArray = {"신고 대목을 선택해주세요.", "1", "2", "3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);

        Log.d("TAG", "activity_declaration");

        editTextUp = findViewById(R.id.myId);
        editTextDown = findViewById(R.id.declarationId);
//        editTextContent = findViewById(R.id.category);

    }

    public void sendDeclare(View view) {
        Log.d(LOG_TAG, "sendDeclare");


    }

}