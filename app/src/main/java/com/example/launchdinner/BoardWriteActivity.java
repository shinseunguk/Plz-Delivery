package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BoardWriteActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BoardWriteActivity";
    String localIp;

    EditText writer, boardTitle, boardContent;
    TextView boardDate;

    String userId;
    String myId, title, content, date;

    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        localIp = getString(R.string.localip);

        Log.d(LOG_TAG, "onCreate");

        writer = findViewById(R.id.writer);
        boardTitle = findViewById(R.id.boardTitle);
        boardContent = findViewById(R.id.boardContent);
        boardDate = findViewById(R.id.boardDate);

        init(); //로그인 ID setting
    }

    //로그인 ID setting
    public void init(){
        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        if(userId != null){
            writer.setText(userId);
            writer.setEnabled(false);
        }else {
            writer.setText("");
            writer.setEnabled(true);
        }

        boardDate.setText(getDate());
    }

    public String getDate(){
    // 현재 날짜 구하기
        LocalDate now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDate.now();
        }
        // 포맷 정의
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        }
        // 포맷 적용
        String formatedNow = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatedNow = now.format(formatter);
        }
        // 결과 출력
//        System.out.println(formatedNow); // 2021/06/17

        return formatedNow;
    }

    public void sendBoard(View view) {
        Log.d(LOG_TAG, "게시판 작성");
        if(effectiveness()){
            ContentValues values = new ContentValues();

            //영재한테 변수명 물어보기
            values.put("server", "board");
            values.put("writer", myId);
            values.put("title", title);
            values.put("content", content);
            values.put("date", date);

            NetworkTask networkTask = new NetworkTask(localIp+"/board/write", values);
            networkTask.execute();
        }
    }

    public boolean effectiveness(){
        myId = writer.getText().toString();
        title = boardTitle.getText().toString();
        content = boardContent.getText().toString();
        date = boardDate.getText().toString();

        Log.d(LOG_TAG, "myId ----> "+myId);
        Log.d(LOG_TAG, "title ----> "+title);
        Log.d(LOG_TAG, "content ----> "+content);
        Log.d(LOG_TAG, "date ----> "+date);

        if(myId.length()==0){
            sToast("내 ID를 확인해주세요.");
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(myId).matches()){
            sToast("내 ID를 이메일 형식으로 작성 해주세요.");
        }else if(title.length()==0){
            sToast("제목을 작성 해주세요.");
        }else if(content.length()==0){
            sToast("게시판 내용을 작성 해주세요.");
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