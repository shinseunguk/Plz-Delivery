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
    String localIp, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        localIp = getString(R.string.localip);

        Log.d(LOG_TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        FrameLayout fr = (FrameLayout) view.findViewById(R.id.FragmentFourth);
        Button btnBoard = (Button) view.findViewById(R.id.btnBoard);

        btnBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("123","123!!!!!!!");
                //게시판 작성으로 이동
                Intent intent = new Intent(getActivity(), BoardWriteActivity.class);
                intent.putExtra("index", "write");
                startActivity(intent);
            }
        });

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter2(getActivity()) ;
        listView.setAdapter(adapter);
        setListAdapter(adapter) ;

        //이쯤에서 서버통신후 adapter.additem ~
        NetworkTask networkTask = new NetworkTask(localIp+"/boardList");
//        NetworkTask networkTask = new NetworkTask(localIp+"/applylist");
        networkTask.execute();

        try {
            Log.d("Fragment4 List",networkTask.get());
            try {
                JSONArray jsonArray = new JSONArray(networkTask.get());

                if(jsonArray.length() == 0){
                    Toast.makeText(getActivity(),"작성된 게시글이 없습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-(i+1)); //i번째 Json데이터를 가져옴
                        Log.d("jsonObject F4", jsonObject.getString("member"));
                        date = jsonObject.getString("localDateTime").replaceAll("-","/");
                        adapter.addItem(jsonObject.getString("title"), date, "작성자 : "+ jsonObject.getString("member"), jsonObject.getLong("id"));
//                        adapter.addItem("아진짜 자고싶다!!~!~!~", "2021/04/18" , "작성자 : 테스트", 21341231);
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
            Log.d(LOG_TAG+" onPostExecute", result);
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
        }
    }
}
