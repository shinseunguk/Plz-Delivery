package com.example.launchdinner;

import android.content.Intent;
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

public class Fragment4 extends ListFragment{
    ListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
//        adapter = new ListViewAdapter(getActivity()) ;
//        listView.setAdapter(adapter);
//        setListAdapter(adapter) ;

        return view;
    }
}
