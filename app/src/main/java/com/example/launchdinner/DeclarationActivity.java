package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DeclarationActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DeclarationActivity";
    String localIp;

    EditText editTextUp, editTextDown, editTextContent;

    String userId;
    String myId, declarationId, content;

    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);
        localIp = getString(R.string.localip);

        Log.d(LOG_TAG, "onCreate");

        editTextUp = findViewById(R.id.myId);
        editTextDown = findViewById(R.id.declarationId);
        editTextContent = findViewById(R.id.editTextContent);
        init();//로그인 ID setting
    }

    //로그인 ID setting
    public void init(){
        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        if(userId != null){
            editTextUp.setText(userId);
            editTextUp.setEnabled(false);
        }else {
            editTextUp.setText("");
            editTextUp.setEnabled(true);
        }
    }

    public void sendDeclare(View view) {
        Log.d(LOG_TAG, "신고내용 접수");
        if(effectiveness()){
            ContentValues values = new ContentValues();

            values.put("server", "accusation");
            values.put("comusermId", myId);
            values.put("reportedId", declarationId);
            values.put("accusationContent", content);

            NetworkTask networkTask = new NetworkTask(localIp+"/accusation", values);
            networkTask.execute();
        }
    }

    public boolean effectiveness(){
        myId = editTextUp.getText().toString();
        declarationId = editTextDown.getText().toString();
        content = editTextContent.getText().toString();

        Log.d(LOG_TAG, "myId ----> "+myId);
        Log.d(LOG_TAG, "declarationId ----> "+declarationId);
        Log.d(LOG_TAG, "content ----> "+content);

        if(myId.length()==0){
            sToast("내 ID를 확인해주세요.");
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(myId).matches()){
            sToast("내 ID를 이메일 형식으로 작성 해주세요.");
        }else if(declarationId.length()==0){
            sToast("신고자 ID를 확인해주세요.");
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(declarationId).matches()){
            sToast("신고자 ID를 이메일 형식으로 작성 해주세요.");
        }else if(content.length()==0){
            sToast("신고내용을 작성 해주세요.");
        }else {
            result = true;
        }

        return result;
    }

    public void sToast(String sToast){
        Toast.makeText(getApplicationContext(),
                sToast, Toast.LENGTH_SHORT).show();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values)
        {
            this.url = url;
            this.values = values;
        }

        NetworkTask(String url)
        {
            this.url = url;
            this.values = values;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection(getApplicationContext());
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("click...result ", result);
        }
    }
}