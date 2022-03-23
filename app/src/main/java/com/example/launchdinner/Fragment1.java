package com.example.launchdinner;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Fragment1 extends Fragment implements View.OnClickListener {

    Spinner spinner, spinner1;
    String[] firstHour, secondHour;
    EditText address1, address2, address3, address4, applyName, applyTel, applyItem, applyPrice, arriveName, arriveTel;
    Button btnAddressFragment, btnArriveAddressFragment, btnApply;
    String a;
    int index = 0;
    Fragment2 fragment2;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("call", "onCreateView Fragment1");
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        fragment2 = new Fragment2();

        //Button
        btnAddressFragment = (Button) v.findViewById(R.id.btnAddressFragment);
        btnArriveAddressFragment = (Button) v.findViewById(R.id.btnArriveAddressFragment);
        btnApply = (Button) v.findViewById(R.id.btnApply);

        //EditText
        address1 = (EditText) v.findViewById(R.id.address);
        address2 = (EditText) v.findViewById(R.id.address_last);
        address3 = (EditText) v.findViewById(R.id.arriveAddress);
        address4 = (EditText) v.findViewById(R.id.arriveAddress_last);
        applyName = (EditText) v.findViewById(R.id.applyName);
        applyTel = (EditText) v.findViewById(R.id.applyTel);
        applyItem = (EditText) v.findViewById(R.id.applyItem);
        applyPrice = (EditText) v.findViewById(R.id.applyPrice);
        arriveName = (EditText) v.findViewById(R.id.arriveName);
        arriveTel = (EditText) v.findViewById(R.id.arriveTel);


        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner1 = (Spinner) v.findViewById(R.id.spinner1);

        firstHour = new String[144];
        secondHour = new String[144];

        for (int i = 0; i <= 23; i++) {
            for (int j = 0; j <= 50; j += 10) {
                if (i == 0 && j == 0) {
                    a = i + "0" + " : " + j + "0";
                } else if ((int) (Math.log10(i) + 1) <= 1) {
                    if (j == 0) {
                        a = "0" + i + " : " + j + "0";
                    } else {
                        a = "0" + i + " : " + j;
                    }
                } else if (j == 0) {
                    a = i + " : " + j + "0";
                } else {
                    a = i + " : " + j;
                }
//                    Log.d("spinnerResult",a);
//                    Log.d("index",index+"");
                firstHour[index] = secondHour[index] = a;
                index++;
            }
        }
        index = 0;

        //스피너 어뎁터
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, firstHour);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, secondHour);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter1);


        //button
        btnAddressFragment.setOnClickListener(this);
        btnArriveAddressFragment.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        //전화번호 formating
        applyTel.setInputType(InputType.TYPE_CLASS_PHONE);
        arriveTel.setInputType(InputType.TYPE_CLASS_PHONE);
        applyTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        arriveTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        checkBox = (CheckBox) v.findViewById(R.id.check1) ;
        checkBox.setOnClickListener(new CheckBox.OnClickListener() {

            @Override
            public void onClick(View v) {
                NetworkTask networkTask = new NetworkTask("http://175.212.211.98:8008/checkbox");
                networkTask.execute();


                if (checkBox.isChecked()) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(networkTask.get());

                        JSONObject jsonObject = jsonArray.getJSONObject(0); //i번째 Json데이터를 가져옴
                        applyName.setText(jsonObject.getString("name"));
                        applyTel.setText(jsonObject.getString("tel"));
                        address1.setText(jsonObject.getString("address1"));
                        address2.setText(jsonObject.getString("address2"));
                    } catch (JSONException e) {
                         e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    applyName.setText("");
                    applyTel.setText("");
                    address1.setText("");
                    address2.setText("");
                }
            }
        }) ;


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();


        Bundle bundle = getArguments();
        if (bundle != null) {
            String addressFirst = bundle.getString("arg1");
            String addressSecond = bundle.getString("arg2");
            String addressThird = bundle.getString("arg3");
            String index = bundle.getString("index");

            Log.d("Fragment1", addressFirst);
            Log.d("Fragment2", addressSecond);
            Log.d("Fragment3", addressThird);
            if (index.equals("departure")) {
                address1.setText(addressSecond);
            } else if (index.equals("arrive"))
                address3.setText(addressSecond);
        }
        if (!address1.getText().toString().equals("")) {
            address2.setEnabled(true);
        }
        if (!address3.getText().toString().equals("")) {
            address4.setEnabled(true);
        }
    }


    // 버튼 클릭 이벤트 리스너
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //출발주소 검색 버튼
            case R.id.btnAddressFragment:
                Log.d("click", "R.id.btnAddressFragment");

                Intent intent = new Intent(getActivity(), addressActivity.class);
                intent.putExtra("index", "departure");
                startActivity(intent);
                break;
            //도착주소 검색 버튼
            case R.id.btnArriveAddressFragment:

                intent = new Intent(getActivity(), addressActivity.class);
                intent.putExtra("index", "arrive");
                startActivity(intent);
                break;
            //신청하기 버튼
            case R.id.btnApply:
                String sAddress1 = address1.getText().toString();
                String sAddress2 = address2.getText().toString();
                String sAddress3 = address3.getText().toString();
                String sAddress4 = address4.getText().toString();
                String sApplyName = applyName.getText().toString();
                String sApplyTel = applyTel.getText().toString();
                String sApplyItem = applyItem.getText().toString();
                String sApplyPrice = applyPrice.getText().toString();
                String sArriveName = arriveName.getText().toString();
                String sArriveTel = arriveTel.getText().toString();
                String startingDate = spinner.getSelectedItem().toString();
                String EndingDate = spinner1.getSelectedItem().toString();

                if (sAddress1.equals("") ||
                        sAddress2.equals("") ||
                        sAddress3.equals("") ||
                        sAddress4.equals("") ||
                        sApplyName.equals("") ||
                        sApplyTel.equals("") ||
                        sApplyItem.equals("") ||
                        sApplyPrice.equals("") ||
                        sArriveName.equals("") ||
                        sArriveTel.equals("")
                ) {
                    Toast.makeText(getActivity(),"빈칸없이 작성 해주세요",Toast.LENGTH_SHORT).show();
                }else{


                    ContentValues values = new ContentValues();

                    values.put("server", "apply");
                    values.put("startAddress1", sAddress1);
                    values.put("startAddress2", sAddress2);
                    values.put("endingAddress1", sAddress3);
                    values.put("endingAddress2", sAddress4);
                    values.put("applyName", sApplyName);
                    values.put("applyTel", sApplyTel);
                    values.put("applyItem", sApplyItem);
                    values.put("applyPrice", sApplyPrice);
                    values.put("arriveName", sArriveName);
                    values.put("arriveTel", sArriveTel);
                    values.put("startingDate", startingDate);
                    values.put("endingDate", EndingDate);

                    NetworkTask networkTask = new NetworkTask("http://175.212.211.98:8008/apply", values);
                    networkTask.execute();

                    intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("index", "fragment1");
                    startActivity(intent);
                }


                break;
        }
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
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection(getActivity());
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("click...result ", result);
        }
    }
}