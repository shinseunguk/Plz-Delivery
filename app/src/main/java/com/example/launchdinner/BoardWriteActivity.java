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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//게시판 작성 및 수정 화면
public class BoardWriteActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BoardWriteActivity";
    String localIp;

    EditText writer, boardTitle, boardContent;
    TextView boardDate, writeTitle;
    Button btnDeclare;

    String userId;
    String myId, title, content, date;
    long id;

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

        btnDeclare = findViewById(R.id.btnDeclare);
        writeTitle = findViewById(R.id.writeTitle);

        init(); //로그인 ID setting

        // 게시판 수정 일때
        if(getIntent() != null){
            Intent intent = getIntent();
            if(intent.getStringExtra("index").equals("revise")){
                id = intent.getLongExtra("id", 1111111111);
                title = intent.getStringExtra("title");
                content = intent.getStringExtra("content");

                boardTitle.setText(title);
                boardContent.setText(content);
                btnDeclare.setText("게시판 수정");
                writeTitle.setText("게시판 수정");
            }
        }
    }

    //로그인 ID setting
    public void init(){
        // local storage 아이디 가져옴
        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        if(userId != null){
            writer.setText(userId);
            writer.setEnabled(false);
        }else {
            writer.setText("");
            writer.setEnabled(true);
        }

        boardDate.setText(getDate()); // 현재 날짜를 가져와 formatting
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

    //게시판 작성 및 게시판 수정 onclick method
    //버튼의 타이틀이 게시판 작성일때는 insert, 게시판 수정일때는 update
    public void sendBoard(View view) {
        String btnTitle = btnDeclare.getText().toString(); // 버튼 타이틀로 분기를 태움

        if(btnTitle.equals("게시판 작성")){
            Log.d(LOG_TAG, "게시판 작성");
            if(effectiveness()){
                ContentValues values = new ContentValues();

                values.put("server", "board");
                values.put("member", myId);
                values.put("title", title);
                values.put("content", content);
                values.put("date", date);

                NetworkTask networkTask = new NetworkTask(localIp+"/boardWrite", values); // 서버통신
                networkTask.execute();
            }
        }else {
            Log.d(LOG_TAG, "게시판 수정");
            if(effectiveness()){
                ContentValues values = new ContentValues();

                values.put("server", "board");
                values.put("id", id);
                values.put("member", myId);
                values.put("title", title);
                values.put("content", content);
                values.put("date", date);

                NetworkTask networkTask = new NetworkTask(localIp+"/boardEdit", values); // 서버통신
                networkTask.execute();
            }
        }

    }

    //게시판 작성 유효성 검사
    public boolean effectiveness(){
        myId = writer.getText().toString();
        title = boardTitle.getText().toString();
        content = boardContent.getText().toString();
        date = boardDate.getText().toString();

        Log.d(LOG_TAG, "member ----> "+myId);
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
            Log.d(LOG_TAG+"!@#", result);
            if(result.equals("true")){ // 서버통신후 true값 일때 메인화면으로 이동
                Intent intent = new Intent(BoardWriteActivity.this, HomeActivity.class);
                intent.putExtra("index", "fragment4");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                sToast("작성 실패");
            }
        }
    }
}