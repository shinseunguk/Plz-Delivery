package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {
    Spinner spinner, spinner1;
    String[] month, gender;
    EditText id , passWord, recheckPassword,  name, tel, year ,day,address, address_last;
//    Task task;
    TextView tv_error_email;
    boolean idCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("호출","호출");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("log","onCreate");
//        task = new Task();


        id = (EditText)findViewById(R.id.email);
        passWord = (EditText)findViewById(R.id.Password);
        recheckPassword = (EditText)findViewById(R.id.recheckPassword);
        name = (EditText)findViewById(R.id.name);
        tel = (EditText)findViewById(R.id.tel);
        year = (EditText)findViewById(R.id.year);
        day = (EditText)findViewById(R.id.day);
        address = (EditText)findViewById(R.id.address);
        address_last = (EditText)findViewById(R.id.address_last);
        tv_error_email = (TextView)findViewById(R.id.tv_error_email);
        spinner =(Spinner)findViewById(R.id.month);
        spinner1 =(Spinner)findViewById(R.id.sex);

        month = new String[]{"월","01","02","03","04","05","06","07","08","09","10","11","12"};
        gender = new String[]{"성별","남","여"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter1);

        id.addTextChangedListener(new TextWatcher() {
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
                    String identity = id.getText().toString();

                    ContentValues values = new ContentValues();
                    values.put("id", identity);

                    NetworkTask networkTask = new NetworkTask("175.212.211.98:8008/checkEmail", values);
                    networkTask.execute();
                }
            }
        });

        tel.setInputType(InputType.TYPE_CLASS_PHONE);
        tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values){
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
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
//            Toast.makeText(getApplicationContext(), result.getClass().getName()+" "+result, Toast.LENGTH_LONG).show();
            if(result.equals("false")){
                tv_error_email.setText("사용가능한 아이디 입니다.");
                tv_error_email.setTextColor(Color.rgb(0,147,33));
                idCheck = true;
            }else{
                tv_error_email.setText("이메일 형식으로 입력해주세요.");
                tv_error_email.setTextColor(Color.parseColor("#FF0000"));
                idCheck = false;
            }
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if(getIntent()!=null){
//            Log.d("호출","호출2");
            Intent secondIntent = getIntent();
            String arg1 = secondIntent.getStringExtra("arg1");
            String arg2 = secondIntent.getStringExtra("arg2");
            String arg3 = secondIntent.getStringExtra("arg3");

            address.setText(arg2);
        }

        if(!address.getText().toString().equals("")){
            Log.d("address.get",address.getText().toString());
            address_last.setEnabled(true);
        }
//        Log.d("호출","호출3");
    }


    public void signup(View view) {

        String identity = id.getText().toString();
        String PassWord = passWord.getText().toString();
        String rechkPassword = recheckPassword.getText().toString();
        String userName = name.getText().toString();
        String mobileTel = tel.getText().toString();
        String birthY = year.getText().toString();
        String birthD = day.getText().toString();
        String birthM = spinner.getSelectedItem().toString(); //월
        String gender = spinner1.getSelectedItem().toString(); //성별
        String firstAddress = address.getText().toString();// 첫번째 주소
        String secondAddress = address_last.getText().toString(); // 두번째 주소
        String birthDay = birthY + birthM + birthD;

        ContentValues values = new ContentValues();
        values.put("server", "signup");
        values.put("id", identity);
        values.put("password", PassWord);
        values.put("name", userName);
        values.put("tel", mobileTel);
        values.put("birth", birthDay);
        values.put("gender", gender);
        values.put("address1", firstAddress);
        values.put("address2", secondAddress);

        if(idCheck == false){
            id.requestFocus();
            sToast("아이디를 확인 해주세요");
        }
        else if(PassWord.length() < 8){
            passWord.requestFocus();
            sToast("비밀번호는 8글자 이상 입력 해주세요");
        }else if(!PassWord.equals(rechkPassword)){
            passWord.requestFocus();
            sToast("비밀번호 입력과 비밀번호 재입력이 다릅니다");
        }else if(birthM.equals("월")){
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            spinner.requestFocus();
            sToast("생년월일을 확인 해주세요");
        }else if (gender.equals("성별")){
            spinner1.setFocusable(true);
            spinner1.setFocusableInTouchMode(true);
            spinner1.requestFocus();
            sToast("성별을 확인 해주세요");
        }else if(identity.equals("") || PassWord.equals("") || rechkPassword.equals("") || userName.equals("") || mobileTel.equals("") || birthY.equals("") || birthD.equals("") || firstAddress.equals("") || secondAddress.equals("")){
            sToast("빈칸없이 작성 해주세요");
        }else {
//            task.execute(map);
            NetworkTask networkTask = new NetworkTask("http://175.212.211.98:8008/signup", values);
            networkTask.execute();

            Intent intent = new Intent(registerActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            sToast("회원가입 완료");
        }

    }


    public void btnAddress(View view) {
        Log.d("click!!!!!!","click!!!!!!");

        Intent intent = new Intent(registerActivity.this, addressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void sToast(String sToast){
        Toast.makeText(getApplicationContext(),
                sToast, Toast.LENGTH_SHORT).show();
    }
}