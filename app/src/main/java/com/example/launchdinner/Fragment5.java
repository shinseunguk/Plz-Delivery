package com.example.launchdinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Fragment5 extends ListFragment {
    ListViewAdapter adapter ;
    String localIp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_5, container, false);
//        ListView listView = (ListView) v.findViewById(android.R.id.list);
//        FrameLayout fr = (FrameLayout) v.findViewById(R.id.FragmentFive);
        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);


        String[] values = new String[] {"더치트 검색", "신고하기", "블랙리스트", "로그아웃"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        String strText = (String) l.getItemAtPosition(position);
        Log.d("Fragment: ", position + ": " +strText);
//        Toast.makeText(this.getContext(), "클릭: " + position +" " + strText, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
                goTheCheat();
            break;
            case 1:
                declarationAction();
            break;
            case 2:
            break;
            case 3:
                logout();
            break;

        }
    }

    public void logout(){
        sToast("정상적으로 로그아웃되었습니다");

        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("sessionid").commit();

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
}