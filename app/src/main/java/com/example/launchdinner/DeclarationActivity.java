package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// 신고하기 화면
public class DeclarationActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DeclarationActivity";
    String localIp;

    EditText editTextUp, editTextDown, editTextContent;
    TextView tv_error_email;

    String userId;
    String myId, declarationId, content;

    boolean result = false;
    boolean idCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);
        localIp = getString(R.string.localip);

        Log.d(LOG_TAG, "onCreate");

        editTextUp = findViewById(R.id.myId);
        editTextDown = findViewById(R.id.declarationId);
        editTextContent = findViewById(R.id.editTextContent);
        tv_error_email = findViewById(R.id.tv_error_email);
        init();//로그인 ID setting

        //텍스트필드가 변경될때마다 서버통신하여 아이디가 유효한지 확인
        editTextDown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.d("호출","beforeTextChanged");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("호출","onTextChanged");
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");
                    tv_error_email.setTextColor(Color.parseColor("#FF0000"));
                    idCheck = false;
                }else{
                    if(editTextUp.getText().toString().equals(editTextDown.getText().toString())){
                        tv_error_email.setText("자신은 신고할 수 없습니다.");
                        tv_error_email.setTextColor(Color.parseColor("#FF0000"));
                        idCheck = false;
                    }else{
                        String identity = editTextDown.getText().toString();

                        ContentValues values = new ContentValues();
                        values.put("id", identity);

                        NetworkTask networkTask = new NetworkTask(localIp+"/checkEmail", values);
                        networkTask.execute();
                    }
                }
            }
        });
    }



    //로그인 ID setting
    public void init(){
        // local storage 에서 아이디 값을 가져옴(로그인 할때 set)
        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        userId = pref.getString("id", "id");

        if(userId != null){ //자동으로 신고 ID에 세팅
            editTextUp.setText(userId);
            editTextUp.setEnabled(false);
        }else {
            editTextUp.setText("");
            editTextUp.setEnabled(true);
        }
    }

    // 하단 신고버튼 Onclick method
    public void sendDeclare(View view) {
        Log.d(LOG_TAG, "신고내용 접수");
        if(effectiveness()){
            ContentValues values = new ContentValues();

            values.put("server", "accusation");
            values.put("member", myId);
            values.put("reported_Id", declarationId);
            values.put("accusation_content", content);

            NetworkTask2 networkTask = new NetworkTask2(localIp+"/accusation", values); // content를 hashmap으로 묶어 서버 통신
            networkTask.execute();
        }
    }

    //유효성 검사
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
        }else if(!idCheck){// 신고자 id 유효성 검사
            sToast("신고자 ID가 유효하지 않습니다.\n 다시 한번 확인해주세요.");
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
            if(result.equals("false")){ //신고가능한 아이디.
                tv_error_email.setText("신고가능한 아이디입니다.");
                tv_error_email.setTextColor(Color.rgb(0,147,33));
                idCheck = true;
            }else{
                tv_error_email.setText("존재하지 않는 아이디입니다.");
                tv_error_email.setTextColor(Color.parseColor("#FF0000"));
                idCheck = false;
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
            Log.d("click...result ", result);
            if(result.equals("true")){
                Intent intent = new Intent(DeclarationActivity.this, HomeActivity.class);
                intent.putExtra("index", "fragment5");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sToast("신고 접수 되었습니다. \n 관리자가 내용을 검토 합니다.");
                startActivity(intent);
            }else{
                sToast("작성 실패");
            }
        }
    }
}