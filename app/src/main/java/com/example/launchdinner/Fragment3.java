package com.example.launchdinner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Fragment3 extends Fragment {

    JSONArray jsonArray = null;

    TextView userInfoName, userInfoId;
    EditText editName, editTel, editAddr1, editAddr2;
    Button btnAddr, btnModify;
    String localIp, loginId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_3, container, false);

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Preferenceszz", Context.MODE_PRIVATE);
        loginId = preferences.getString("id", "id");

        //TextView
        userInfoName = (TextView) v.findViewById(R.id.userInfoName);
        userInfoId = (TextView) v.findViewById(R.id.userInfoId);

        //EditText
        editName = (EditText) v.findViewById(R.id.editName);
        editTel = (EditText) v.findViewById(R.id.editTel);
        editAddr1 = (EditText) v.findViewById(R.id.editAddr1);
        editAddr2 = (EditText) v.findViewById(R.id.editAddr2);

        //Button
        btnAddr = (Button) v.findViewById(R.id.btnAddr);
        btnModify = (Button) v.findViewById(R.id.btnModify);

        editTel.setInputType(InputType.TYPE_CLASS_PHONE);
        editTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ContentValues values = new ContentValues();
        values.put("member", loginId);

        NetworkTask networkTask = new NetworkTask(localIp+"/checkbox", values);
        networkTask.execute();

        try {

            jsonArray = new JSONArray(networkTask.get());

            JSONObject jsonObject = jsonArray.getJSONObject(0); //i번째 Json데이터를 가져옴
            Log.d("ddddd",jsonObject.toString());

            userInfoName.setText(jsonObject.getString("name"));
            userInfoId.setText(jsonObject.getString("id"));
            editName.setText(jsonObject.getString("name"));
            editTel.setText(jsonObject.getString("tel"));
            editAddr1.setText(jsonObject.getString("address1"));
            editAddr2.setText(jsonObject.getString("address2"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!editAddr1.getText().toString().equals("")) {
            editAddr2.setEnabled(true);
        }

        btnAddr.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), addressActivity.class);
               intent.putExtra("index", "userinfo");
               startActivity(intent);
           }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String tel = editTel.getText().toString();
                String address1 = editAddr1.getText().toString();
                String address2 = editAddr2.getText().toString();

                if(!name.equals("") && !tel.equals("") && !address1.equals("") && !address2.equals("")){
                    Log.d("btnModify", name + tel + address1 + address2);

                    ContentValues values = new ContentValues();

                    values.put("name", name);
                    values.put("tel", tel);
                    values.put("address1", address1);
                    values.put("address2", address2);
                    values.put("member", loginId);

                    Fragment3.NetworkTask networkTask = new Fragment3.NetworkTask(localIp+"/userinfo", values);
                    networkTask.execute();

                    try {
                        String result = networkTask.get();

                        if(result.equals("1")){
                            Toast.makeText(getActivity(),"회원정보가 변경 되었습니다",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"회원정보가 변경 실패",Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(),"빈칸없이 작성 해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onresume","onresume");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String addressFirst = bundle.getString("arg1");
            String addressSecond = bundle.getString("arg2");
            String addressThird = bundle.getString("arg3");
            String index = bundle.getString("index");

            Log.d("Fragment1", addressFirst);
            Log.d("Fragment2", addressSecond);
            Log.d("Fragment3", addressThird);
            if (index.equals("userinfo")) {
                editAddr1.setText(addressSecond);
                editAddr2.setText("");
            }
            if (!editAddr1.getText().toString().equals("")) {
                editAddr2.setEnabled(true);
            }
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        private String mTitle = null;

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
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection(getActivity());
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
        }
    }
}