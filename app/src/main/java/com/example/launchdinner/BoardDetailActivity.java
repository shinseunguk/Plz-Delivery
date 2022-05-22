package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class BoardDetailActivity extends AppCompatActivity {

    String LOG_TAG = "BoardDetailActivity";
    String localIp, userId;
    ImageView imageView;
    TextView title, content, writer, time;
    long id;
    Spinner spinner;

    List<String> listview_items;
    ArrayAdapter<String> listview_adapter;
    String[] items = {"수정 / 삭제", "게시판 수정", "게시판 삭제"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
        localIp = getString(R.string.localip);

        init();

        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        writer = findViewById(R.id.writer);
        time = findViewById(R.id.time);

        spinner = (Spinner) findViewById(R.id.spinner2);

        spinner.setSelection(0);
        //스피너 어뎁터
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    if(i == 1){ // 게시판 수정
                        Log.d(LOG_TAG, "게시판 수정");
                    }else if(i == 2){ // 게시판 삭제
                        ContentValues values = new ContentValues();
                        values.put("id", id);

                        NetworkTask1 networkTask1 = new NetworkTask1(localIp+"/boardEdit", values);
                        networkTask1.execute();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        imageView = findViewById(R.id.revise);
//        imageView.setClickable(true);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ddd", "asdasd");
//            }
//        });


    }

    public void init(){
        if(getIntent() != null){
            Intent intent = getIntent();
            id = intent.getLongExtra("id",1234);
            Log.d(LOG_TAG+"!!!!!", String.valueOf(id));

            ContentValues values = new ContentValues();
            values.put("id", id);

//            NetworkTask networkTask = new NetworkTask(localIp+"/boardDetail", values);
//            networkTask.execute();
        }
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

    public class NetworkTask1 extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask1(String url, ContentValues values)
        {
            this.url = url;
            this.values = values;
        }

        NetworkTask1(String url)
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

            if(result.equals("false")){ // 삭제 실패

            }else{ // 삭제 성공

            }
        }
    }


}