package com.example.launchdinner;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
// fragment 두번째화면의 tabbar2 내 직거래 신청 목록
public class Fragment2Second extends ListFragment {

    ListViewAdapter adapter ;
    String localIp, loginId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("onCreateView", "Fragment2Second");

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        View v = inflater.inflate(R.layout.fragment_2second, container, false);
        ListView listView = (ListView) v.findViewById(android.R.id.list);

//         Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter(getActivity()) ;
        setListAdapter(adapter) ;

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Preferenceszz", Context.MODE_PRIVATE);
        loginId = preferences.getString("id", "id");

        ContentValues values = new ContentValues();
        values.put("member", loginId);

        //이쯤에서 서버통신후 adapter.additem ~
        Fragment2Second.NetworkTask networkTask = new Fragment2Second.NetworkTask(localIp+"/myapplylist" ,values);
        networkTask.execute();

        try {
            Log.d("Fragment2Second List",networkTask.get());
            try {
                JSONArray jsonArray = new JSONArray(networkTask.get());

                if(jsonArray.length() == 0){
                    Toast.makeText(getActivity(),"요청된 직거래가 없습니다",Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i); //i번째 Json데이터를 가져옴
                        Log.d("dd",jsonObject.getString("start_address1"));
                        adapter.addItem("시작주소  -  " + jsonObject.getString("start_address1")+" "+jsonObject.getString("start_address2"),"받는주소  -  " + jsonObject.getString("end_address1")+ " "+ jsonObject.getString("end_address2"),"희망 수거 시간  -  " + jsonObject.getString("departure_time"), jsonObject.getString("arrive_time"),"거래 물품  -  " + jsonObject.getString("exchange_item"), "거래 번호 - "+  jsonObject.getString("seq"), jsonObject.getString("delivery_yn"));
                    }
                    listView.getLayoutParams().height = jsonArray.length() * 100;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2second, container, false);
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        private String mTitle = null;

        NetworkTask(String url, ContentValues values) {
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