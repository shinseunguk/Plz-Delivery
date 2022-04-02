package com.example.launchdinner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Fragment4 extends ListFragment {
    ListViewAdapter adapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_4, container, false);
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        FrameLayout fr = (FrameLayout) v.findViewById(R.id.FragmentFourth);

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter(getActivity()) ;
//        listView.setAdapter(adapter);
        setListAdapter(adapter) ;


        return inflater.inflate(R.layout.fragment_2, container, false);
    }

}