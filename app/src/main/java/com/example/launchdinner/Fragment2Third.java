package com.example.launchdinner;

import android.content.ContentValues;
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

public class Fragment2Third extends ListFragment {

    ListViewAdapter adapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("onCreateView", "Fragment2Third");
        View v = inflater.inflate(R.layout.fragment_2third, container, false);
        ListView listView = (ListView) v.findViewById(android.R.id.list);

//         Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter(getActivity()) ;
        setListAdapter(adapter) ;

        //이쯤에서 서버통신후 adapter.additem ~
        Fragment2Third.NetworkTask networkTask = new Fragment2Third.NetworkTask("http://172.30.1.50:8008/mydelivery");
        networkTask.execute();

        try {
            Log.d("Fragment2Third List",networkTask.get());
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

        return inflater.inflate(R.layout.fragment_2third, container, false);
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        private String mTitle = null;

        NetworkTask(String url){
            this.url = url;
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