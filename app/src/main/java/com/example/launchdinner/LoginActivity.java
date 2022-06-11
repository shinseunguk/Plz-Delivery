package com.example.launchdinner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
//로그인 acitivity
public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;

    EditText id, pw;

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    private Button loginBtn, beLoginBtn, registerBtn;
    String localIp;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        id= (EditText)findViewById(R.id.editTextTextEmailAddress);
        pw = (EditText)findViewById(R.id.editTextTextPassword2);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        editor = pref.edit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getHashKey(); //hash 값 가져오기
    }

    //카카오 로그인 관련(deprecated) 사용하지 않음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("kakao", "Session Open");
            //로그인 세션이 열렸을 때.
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    //로그인에 실패했을 때. 인터넷 연결이 불안정한 경우도 여기에 해당한다.
                    Log.d("kakako", "Login Fail");
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.d("kakao", "onSessionClosed"+ errorResult);
                    //로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    //로그인 성공
                    Log.d("kakao","Login Success");
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    Log.d("name", result.getNickname());
                    Log.d("profile", result.getProfileImagePath());
                    startActivity(intent);
                    finish();

                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            //로그인 세션이 정상적으로 열리지 않았을 때.
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //로그인화면 -> 로딩화면 돌아가기
    @Override public void onBackPressed() {

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다", Toast.LENGTH_LONG);
            toast.show();
//            finishAndRemoveTask();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast.show();
        }
    }

    //로그인
    public void login(View view) {
        String identity = id.getText().toString();
        String passWord = pw.getText().toString();

        ContentValues values = new ContentValues();
        values.put("id", identity);
        values.put("pw", passWord);

        Log.d("localIp/login ", localIp+"/login");
        NetworkTask networkTask = new NetworkTask(localIp+"/login", values);
        if(networkTask!=null){
            networkTask.execute();
        }else{
            sToast("인터넷 연결 상태를 확인해주세요");
        }
    }

    //회원가입
    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, registerActivity.class);
        startActivity(intent);
    }

    //비로그인 처리후 메인화면으로 이동
    public void beLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
        startActivity(intent);
    }

    public void kakaoOnClick(View view) {
        Log.d("touch","kakao");
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
    }

    public void facebookOnClick(View view) {
        Log.d("touch","facebook");
    }

    public void naverOnClick(View view) {
        Log.d("touch","naver");
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

        //서버 통신후 return 값으로 분기태움
        @Override
        protected void onPostExecute(String result) {
            String identity = id.getText().toString();
            if(result!=null){
                Log.d("result...",result);
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(identity).matches()){
                    sToast("이메일 형식의 아이디를 입력해주세요");
                }else if(!result.equals("false")){
                    sToast("아이디와 비밀번호 정보가 맞지 않습니다");
                }else if(result.equals("false")) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);
                    editor.putString("id", identity);
                    editor.commit();

                }
            }else{
                sToast("아이디와 비밀번호 정보가 맞지 않습니다");
            }
        }
    }

    public void sToast(String sToast){
        Toast.makeText(getApplicationContext(),
                sToast, Toast.LENGTH_SHORT).show();
    }

    private void getHashKey()
    {
        PackageInfo packageInfo = null;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
            catch (NoSuchAlgorithmException e)
            {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

}