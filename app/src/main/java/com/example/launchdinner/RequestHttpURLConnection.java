package com.example.launchdinner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

//서버통신을 위한 CLASS
public class RequestHttpURLConnection {

    Context mContext;
    String sessionid;
    HttpURLConnection urlConn = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String m_cookies = "";
    boolean m_session = false;

    public RequestHttpURLConnection() {
    }

    public RequestHttpURLConnection(Context context) {
        mContext = context;

    }

    public String request(String _url, ContentValues _params) {


        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();

        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            if(m_session) {
                urlConn.setRequestProperty("Cookie", m_cookies);
            }

            // [2-1]. urlConn 설정.
            urlConn.setReadTimeout(100000);
            urlConn.setConnectTimeout(50000);
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : GET/POST.
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded");
            setCookieHeader();

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(urlConn.getOutputStream()));
            pw.write(sbParams.toString());
            pw.flush(); // 출력 스트림을 flush. 버퍼링 된 모든 출력 바이트를 강제 실행.
            pw.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null) {
                page += line;
            }
            getCookieHeader();
            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }
    private void getCookieHeader(){//Set-Cookie에 배열로 돼있는 쿠키들을 스트링 한줄로 변환
        List<String> cookies = urlConn.getHeaderFields().get("Set-Cookie");
        //cookies -> [JSESSIONID=D3F829CE262BC65853F851F6549C7F3E; Path=/smartudy; HttpOnly] -> []가 쿠키1개임.
        //Path -> 쿠키가 유효한 경로 ,/smartudy의 하위 경로에 위의 쿠키를 사용 가능.
        if (cookies != null) {
            for (String cookie : cookies) {
                sessionid = cookie.split(";\\s*")[0];
                //JSESSIONID=FB42C80FC3428ABBEF185C24DBBF6C40를 얻음.
                //세션아이디가 포함된 쿠키를 얻었음.
                setSessionIdInSharedPref(sessionid);
                Log.d("getCookieHeader", sessionid);
            }
        }

    }

    private void setSessionIdInSharedPref(String sessionid){
        if(mContext!=null){
            Log.d("mCntext","isnotnull");
            pref = mContext.getSharedPreferences("sessionCookie", Activity.MODE_PRIVATE);
            editor = pref.edit();
            if(pref.getString("sessionid",null) == null){ //처음 로그인하여 세션아이디를 받은 경우
                Log.d("LOG","처음 로그인하여 세션 아이디를 pref에 넣었습니다."+sessionid);
            }else if(!pref.getString("sessionid",null).equals(sessionid)){ //서버의 세션 아이디 만료 후 갱신된 아이디가 수신된경우
                Log.d("LOG","기존의 세션 아이디"+pref.getString("sessionid",null)+"가 만료 되어서 "
                        +"서버의 세션 아이디 "+sessionid+" 로 교체 되었습니다.");
            }
            editor.putString("sessionid",sessionid);
            editor.apply(); //비동기 처리
        }
    }


    private void setCookieHeader() {
        if (mContext != null) {
            SharedPreferences pref = mContext.getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
            String sessionid = pref.getString("sessionid", null);
            if (sessionid != null) {
                Log.d("LOG", "세션 아이디" + sessionid + "가 요청 헤더에 포함 되었습니다.");
                urlConn.setRequestProperty("Cookie", sessionid);
            }
        }
    }
}

