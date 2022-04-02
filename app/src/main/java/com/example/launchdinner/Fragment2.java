package com.example.launchdinner;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Fragment2 extends ListFragment {

    ListViewAdapter adapter ;
    ViewPager viewPager;
    TabLayout tabLayout;
    String localIp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen

        View v = inflater.inflate(R.layout.fragment_2, container, false);
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        FrameLayout fr = (FrameLayout) v.findViewById(R.id.FragmentSecond);

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter(getActivity()) ;
//        listView.setAdapter(adapter);
        setListAdapter(adapter) ;
        Log.d("getHeight", listView.getHeight()+" ");
//        setListViewHeightBasedOnChildren(listView);


        //이쯤에서 서버통신후 adapter.additem ~
        NetworkTask networkTask = new NetworkTask(localIp+"/applylist");
        networkTask.execute();

        try {
            Log.d("Fragment2 List",networkTask.get());
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

        Log.d("Fragment2", "first");

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.

//        Log.d("click",position+" "+ id+ " ");

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        Log.d("fjweiofjiweo","fnwieofnwio");

        ListAdapter listAdapter = listView.getAdapter();
        Log.d("fjweiofjiweo",listView.getAdapter()+ " ");
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        Log.d("desiredWidth", desiredWidth+" ");
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Log.d("listAdapter.getCount()", listAdapter.getCount()+" ");
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        Log.d("totalHeight", totalHeight+" ");
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
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

