package com.example.launchdinner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Fragment5 extends ListFragment {

    JSONArray jsonArray = null;

    TextView userInfoName, userInfoId;

    ListViewAdapter adapter ;
    String localIp;

    private static final String LOG_TAG = "Fragment5";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_5, container, false);
//        ListView listView = (ListView) v.findViewById(android.R.id.list);
//        FrameLayout fr = (FrameLayout) v.findViewById(R.id.FragmentFive);
        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        userInfoId = v.findViewById(R.id.userInfoId);
        userInfoName = v.findViewById(R.id.userInfoName);

        ContentValues contentValues = new ContentValues();

        NetworkTask2 networkTask = new NetworkTask2(localIp+"/checkbox", contentValues);
        networkTask.execute();

        try {

            jsonArray = new JSONArray(networkTask.get());

            JSONObject jsonObject = jsonArray.getJSONObject(0); //i번째 Json데이터를 가져옴
            Log.d("ddddd",jsonObject.toString());

            userInfoName.setText(jsonObject.getString("name"));
            userInfoId.setText(jsonObject.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String[] values = new String[]{"더치트 검색", "신고하기", "로그아웃"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String strText = (String) l.getItemAtPosition(position);
        Log.d("Fragment: ", position + ": " + strText);
//        Toast.makeText(this.getContext(), "클릭: " + position +" " + strText, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
                goTheCheat();
                break;
            case 1:
                declarationAction();
                break;
            case 2:
                logout();
                break;
            default:
                Log.d(LOG_TAG, "default");
                break;
        }
    }

    public void logout(){
        sToast("정상적으로 로그아웃되었습니다");

        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("sessionid").commit();
        editor.remove("id").commit();


        NetworkTask networkTask = new NetworkTask(localIp+"/logout");
        networkTask.execute();

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void sToast(String sToast){
        Toast.makeText(getActivity(),
                sToast, Toast.LENGTH_SHORT).show();
    }

    public void goTheCheat() {
        Intent intent = new Intent(getActivity(), WebviewActivity.class);
        startActivity(intent);
    }

    public void declarationAction() {
        Intent intent = new Intent(getActivity(), DeclarationActivity.class);
        startActivity(intent);
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        private String mTitle = null;

        NetworkTask2(String url, ContentValues values){
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