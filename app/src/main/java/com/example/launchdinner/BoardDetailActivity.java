package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class BoardDetailActivity extends AppCompatActivity {

    String LOG_TAG = "BoardDetailActivity";
    String localIp, userId;
    ImageView imageView;
    TextView title, content, writer, time;

    String[] items = {"게시판 수정", "게시판 삭제"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
        localIp = getString(R.string.localip);

        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        writer = findViewById(R.id.writer);
        time = findViewById(R.id.time);

        imageView = findViewById(R.id.revise);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ddd", "asdasd");
            }
        });
        init();
    }

    public void init(){
        if(getIntent() != null){
            Intent intent = getIntent();
            long id = intent.getLongExtra("id",1234);
            Log.d(LOG_TAG+"!!!!!", String.valueOf(id));

            ContentValues values = new ContentValues();
            values.put("id", id);

//            NetworkTask networkTask = new NetworkTask(localIp+"/boardDetail", values);
//            networkTask.execute();
        }
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
            Log.d(LOG_TAG, result);

            try {
                // result -> JSONObject 파싱 완료
                JSONObject json = new JSONObject(result);

                Log.d(LOG_TAG+"result" , json.getString("comuserm_id"));

                if(userId == json.getString("comuserm_id")){
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    imageView.setVisibility(View.INVISIBLE);
                }

                //금액 format
                title.setText(json.getString("title")); // 제목
                content.setText(json.getString("content"));// 내용
                time.setText(json.getString("localDateTime"));// 시간
                writer.setText(json.getString("comuserm_id"));// 작성자

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}