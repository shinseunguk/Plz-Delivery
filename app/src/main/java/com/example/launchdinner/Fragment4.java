package com.example.launchdinner;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Fragment4 extends ListFragment{
    String LOG_TAG = "Fragment4";
    ListViewAdapter2 adapter;
    String localIp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        localIp = getString(R.string.localip);

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_4, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        FrameLayout fr = (FrameLayout) view.findViewById(R.id.FragmentFourth);
        Button btnBoard = (Button) view.findViewById(R.id.btnBoard);

        btnBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("123","123!!!!!!!");
                //게시판 작성으로 이동
                Intent intent = new Intent(getActivity(), BoardWriteActivity.class);
                startActivity(intent);
            }
        });

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter2(getActivity()) ;
        listView.setAdapter(adapter);
        setListAdapter(adapter) ;


        //이쯤에서 서버통신후 adapter.additem ~
//        NetworkTask networkTask = new NetworkTask(localIp+"/boardList");
        NetworkTask networkTask = new NetworkTask(localIp+"/applylist");
        networkTask.execute();

        try {
            Log.d("Fragment4 List",networkTask.get());
            try {
                JSONArray jsonArray = new JSONArray(networkTask.get());

                if(jsonArray.length() == 0){
                    Toast.makeText(getActivity(),"요청된 직거래가 없습니다",Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i); //i번째 Json데이터를 가져옴
//                        Log.d("dd",jsonObject.getString("start_address1"));
//                        adapter.addItem("시작주소  -  " + jsonObject.getString("start_address1")+" "+jsonObject.getString("start_address2"),"받는주소  -  " + jsonObject.getString("end_address1")+ " "+ jsonObject.getString("end_address2"),"희망 수거 시간  -  " + jsonObject.getString("departure_time"), jsonObject.getString("arrive_time"),"거래 물품  -  " + jsonObject.getString("exchange_item"), "거래 번호 - "+  jsonObject.getString("seq"), jsonObject.getString("delivery_yn"));
//                        adapter.addItem(jsonObject.getString("title"), jsonObject.getString("localDateTime"), "작성자 : "+ jsonObject.getString("comusermVO"));
                        adapter.addItem("아진짜 자고싶다!!~!~!~", "2021/04/18" , "작성자 : 테스트");
                    }
//                    listView.getLayoutParams().height = jsonArray.length() * 100;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "first");

        return view;
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
