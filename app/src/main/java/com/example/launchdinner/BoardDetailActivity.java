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

        //스피너 어뎁터
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    if(i == 1){ // 게시판 수정 boardEdit
                        Log.d(LOG_TAG, "게시판 수정");
                        Intent intent = new Intent(BoardDetailActivity.this, BoardWriteActivity.class);
                        intent.putExtra("index", "revise");
                        intent.putExtra("id", id);
                        intent.putExtra("title", title.getText().toString());
                        intent.putExtra("content", content.getText().toString());
                        intent.putExtra("writer", writer.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(i == 2){ // 게시판 삭제
                        ContentValues values = new ContentValues();
                        values.put("id", id);

                        NetworkTask2 networkTask2 = new NetworkTask2(localIp+"/boardDelete", values);
                        networkTask2.execute();

                        // 테스트
//                        Intent intent = new Intent(BoardDetailActivity.this, HomeActivity.class);
//                        intent.putExtra("index", "fragment4");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);

                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // 화면 기능을 실행할 준비를 함ㅁ
//        Toast.makeText(getApplicationContext(), "onStart() 호출됨", Toast.LENGTH_LONG).show();
        spinner.setSelection(0);
    }

    public void init(){
        if(getIntent() != null){
            Intent intent = getIntent();
            id = intent.getLongExtra("id",1234);
            Log.d(LOG_TAG+"!!!!!", String.valueOf(id));

            ContentValues values = new ContentValues();
            values.put("id", id);

            // 테스트
            NetworkTask networkTask = new NetworkTask(localIp+"/boardDetail", values);
            networkTask.execute();
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

                Log.d(LOG_TAG+"result" , json.getString("member"));

                if(userId.equals(json.getString("member"))){
                    spinner.setVisibility(View.VISIBLE);
                }else {
                    spinner.setVisibility(View.INVISIBLE);
                }

                //금액 format
                title.setText(json.getString("title")); // 제목
                content.setText(json.getString("content"));// 내용
                time.setText("작성날짜  -  "+json.getString("localDateTime").replaceAll("-","/"));// 시간
                writer.setText("작성자  -  "+json.getString("member"));// 작성자

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask2(String url, ContentValues values)
        {
            this.url = url;
            this.values = values;
        }

        NetworkTask2(String url)
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
                sToast("삭제 실패");
            }else{ // 삭제 성공
                Intent intent = new Intent(BoardDetailActivity.this, HomeActivity.class);
                intent.putExtra("index", "fragment4");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


}