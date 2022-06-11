package com.example.launchdinner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

//직거래 view 화면
public class ApplyViewActivity extends AppCompatActivity {

    TextView applyName, applyTel, address, departureTime, arriveTime, applyItem, applyPrice, arriveName, arriveTel, arriveAddress, seq;
    Button btnShowMipMap, btnDelivery, btnDelete, btnChat;
    String loginId;
    String start_address1, start_address2, end_address1, end_address2;

    String localIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_view);

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        SharedPreferences pref = getSharedPreferences("Preferenceszz", Activity.MODE_PRIVATE);
        loginId = pref.getString("id", "id");

        Log.d("ApplyViewActivity", "loginId..."+ loginId);

        //TextVIew 객체 가져오기
        applyName = findViewById(R.id.applyName);
        applyTel = findViewById(R.id.applyTel);
        address = findViewById(R.id.address);
        departureTime = findViewById(R.id.departureTime);
        arriveTime = findViewById(R.id.arriveTime);
        applyItem = findViewById(R.id.applyItem);
        applyPrice = findViewById(R.id.applyPrice);
        arriveName = findViewById(R.id.arriveName);
        arriveTel = findViewById(R.id.arriveTel);
        arriveAddress = findViewById(R.id.arriveAddress);
        seq = findViewById(R.id.seq);

        //Button 객체 가져오기
        btnShowMipMap = findViewById(R.id.btnShowMipMap);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setVisibility(View.GONE);

        //지도보기 onclick 함수
        btnShowMipMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("click",start_address1 + start_address2);
//                start_address1 = json.getString("start_address1");
//                start_address2 = json.getString("start_address2");
//                end_address1 = json.getString("end_address1");
//                end_address2 = json.getString("end_address2");

                Intent intent = new Intent(ApplyViewActivity.this, KakaoMapViewActivity.class);
                intent.putExtra("start_address1", start_address1);
                intent.putExtra("start_address2", start_address2);
                intent.putExtra("end_address1", end_address1);
                intent.putExtra("end_address2", end_address2);
                startActivity(intent);
            }
        });

        //배달하기 버튼 눌렀을때
        btnDelivery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(btnDelivery.getText().equals("배달하기")){
                    Log.d("btnDelivery","배달하기");
                    String seqText= (String)seq.getText();

                    ContentValues values = new ContentValues();
                    values.put("seq", seqText);
                    values.put("member", loginId);

                    NetworkTask networkTask = new NetworkTask(localIp+"/deliver", values);
                    networkTask.execute();

                    try {
                        String result = networkTask.get();
                        if(result.equals("1")) { // 서버통신후 return 값이 1일때
                            Intent intent = new Intent(ApplyViewActivity.this, HomeActivity.class); // 메인화면으로 이동
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(btnDelivery.getText().equals("배달완료")){ // 버튼의 Title 이 배달완료 일때
                    Log.d("btnDelivery","배달완료");

                    String seqText= (String)seq.getText();

                    ContentValues values = new ContentValues();
                    values.put("seq", seqText);
                    values.put("member", loginId);

                    NetworkTask networkTask = new NetworkTask(localIp+"/deliverysuc", values);
                    networkTask.execute();

                    try {
                        String result = networkTask.get();
                        if(result.equals("1")) {
                            Intent intent = new Intent(ApplyViewActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //배달삭제 onclick 함수
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String seqText= (String)seq.getText();
                Log.d("click",seqText);

                ContentValues values = new ContentValues();
                values.put("seq", seqText);

                NetworkTask networkTask = new NetworkTask(localIp+"/deletedelivery", values);
                networkTask.execute();

                try {
                    String result = networkTask.get();
                    Log.d("deletedelivery...", result);
                    if(result.equals("1")) { // 서버 통신후 return이 1일때
                        Intent intent = new Intent(ApplyViewActivity.this, HomeActivity.class); // 메인화면으로 이동
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        sToast("직거래가 삭제 되었습니다");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        if(getIntent() != null){
            Intent intent = getIntent();
            String seq = intent.getStringExtra("seq");
            Log.d("seq", seq);

            ContentValues values = new ContentValues();
            values.put("seq", seq);

            NetworkTask networkTask = new NetworkTask(localIp+"/applyview", values);
            networkTask.execute();
        }
    }


    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* progress bar를 보여주는 등등의 행위 */
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
            Log.d("click!!", result);

            try {
                // result -> JSONObject 파싱 완료
                JSONObject json = new JSONObject(result);
                Log.d("onPostExcute", json.getString("start_address1"));

                //금액 format
                DecimalFormat myFormatter = new DecimalFormat("###,###");
                String formattedStringPrice = myFormatter.format(Integer.parseInt(json.getString("apply_price")));

                seq.setText(json.getString("seq"));

                applyName.setText(json.getString("apply_name"));

                applyTel.setText(json.getString("apply_tel"));
                address.setText(json.getString("start_address1")+" "+json.getString("start_address2"));
                departureTime.setText(json.getString("departure_time"));
                arriveTime.setText(json.getString("arrive_time"));
                applyItem.setText(json.getString("exchange_item"));
                applyPrice.setText(formattedStringPrice + " 원");
                arriveName.setText(json.getString("arrive_name"));
                arriveTel.setText(json.getString("arrive_tel"));
                arriveAddress.setText(json.getString("end_address1")+" "+json.getString("end_address2"));

                start_address1 = json.getString("start_address1");
                start_address2 = json.getString("start_address2");
                end_address1 = json.getString("end_address1");
                end_address2 = json.getString("end_address2");
//                Log.d("##@@1", json.getString("apply_id"));
//                Log.d("##@@2", loginId);

                if(loginId.equals(json.getString("apply_id"))) {
                    Log.d("ApplyView..","1");
                    btnShowMipMap.setVisibility(View.GONE);
                    btnDelivery.setVisibility(View.GONE);

                    btnDelete.setVisibility(View.VISIBLE);
                }else if(loginId.equals(json.getString("delivery_id"))){
                    Log.d("ApplyView..","2");
                    btnDelivery.setText("배달완료");

                    btnDelete.setVisibility(View.GONE);
//                    btnChat.setVisibility(View.VISIBLE);
                    //채팅하기, 추가
                }

                if(json.getString("delivery_yn").equals("Y")){
                    Log.d("ApplyView..","3");

                    btnShowMipMap.setVisibility(View.GONE);
                    btnDelivery.setVisibility(View.GONE);

                    //채팅하기 추가
                    btnDelete.setVisibility(View.GONE);
//                    btnChat.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void sToast(String sToast){
        Toast.makeText(getApplicationContext(),
                sToast, Toast.LENGTH_SHORT).show();
    }
}