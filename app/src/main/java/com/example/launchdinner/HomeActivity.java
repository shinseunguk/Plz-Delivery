package com.example.launchdinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    Fragment1 fragment1 = new Fragment1();
    Fragment2 fragment2 = new Fragment2();
    Fragment3 fragment3 = new Fragment3();
    Fragment4 fragment4 = new Fragment4();

    Fragment2Second fragment2Second = new Fragment2Second();
    Fragment2Third fragment2Third = new Fragment2Third();

    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call","oncreate home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        FragmentManager fragmentManager = getSupportFragmentManager();


            if(getIntent() != null){
                Intent intent = getIntent();
                if(intent.getStringExtra("index") != null){ // 직거래 신청 후
                    String intentIndex =intent.getStringExtra("index");
                    Log.d("intentIndex..... ", intentIndex);

                    fragmentManager.beginTransaction().add(R.id.main_layout, fragment2, "second").commit();
                    bottomNavigationView.setSelectedItemId(R.id.tab2);
                    tabLayout.addTab(tabLayout.newTab().setText("배달원 모집중"));
                    tabLayout.addTab(tabLayout.newTab().setText("내 직거래 신청 목록"));
//                    tabLayout.addTab(tabLayout.newTab().setText("내 물건 진행상황"));
                    tabLayout.addTab(tabLayout.newTab().setText("내가 배달중인 직거래"));
                }else{// 어플 첫 진입시
                    fragmentManager.beginTransaction().add(R.id.main_layout, fragment2, "second").commit();
                    bottomNavigationView.setSelectedItemId(R.id.tab2);  //선택된 아이템 지정
                    tabLayout.addTab(tabLayout.newTab().setText("배달원 모집중"));
                    tabLayout.addTab(tabLayout.newTab().setText("내 직거래 신청 목록"));
//                    tabLayout.addTab(tabLayout.newTab().setText("내 물건 진행상황"));
                    tabLayout.addTab(tabLayout.newTab().setText("내가 배달중인 직거래"));

                }
            }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("0","0");
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("second")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment2, "second").commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        break;
                    case 1:
                        Log.d("1","1");
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment2Second, "Fragment2Second").commit();
                        }
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        break;
                    case 2:
                        Log.d("2","2");
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment2Third, "Fragment2Third").commit();
                        }
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.d("2","2");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.d("3","3");
            }
        });

        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()) {
                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.

                    //직거래 신청
                    case R.id.tab1: {
                        tabLayout.removeAllTabs();
                        if(fragmentManager.findFragmentByTag("first") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("first")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment1, "first").commit();
                        }
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("third")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("fourth") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fourth")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        return true;
                    }
                    //배달
                    case R.id.tab2: {
                        tabLayout.removeAllTabs();
                        tabLayout.addTab(tabLayout.newTab().setText("배달원 모집중"));
                        tabLayout.addTab(tabLayout.newTab().setText("내 직거래 신청 목록"));
//                        tabLayout.addTab(tabLayout.newTab().setText("내 물건 진행상황"));
                        tabLayout.addTab(tabLayout.newTab().setText("내가 배달중인 직거래"));
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("second")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment2, "second").commit();
                        }
                        if(fragmentManager.findFragmentByTag("first") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("first")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("third")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("fourth") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fourth")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        return true;
                    }
                    //홈
                    case R.id.tab3: {
                        tabLayout.removeAllTabs();
                        if(fragmentManager.findFragmentByTag("third") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("third")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment3, "third").commit();
                        }
                        if(fragmentManager.findFragmentByTag("first") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("first")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("fourth") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fourth")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        return true;
                    }
                    //채팅
                    case R.id.tab4: {
                        tabLayout.removeAllTabs();
                        if(fragmentManager.findFragmentByTag("fourth") != null){
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("fourth")).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.main_layout, fragment4, "fourth").commit();
                        }
                        if(fragmentManager.findFragmentByTag("first") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("first")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("third")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Second") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Second")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("Fragment2Third") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Fragment2Third")).commit();
                        }
                        return true;
                    }
                    //로그아웃
                    case R.id.tab5: {
                        sToast("정상적으로 로그아웃되었습니다");

                        SharedPreferences pref = getSharedPreferences("sessionCookie", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.remove("sessionid").commit();

                        NetworkTask networkTask = new NetworkTask("http://175.212.211.98:8008/logout");
                        networkTask.execute();

                        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }
                    default:
                        return false;
                }
            }
        });
    }


    // 주소검색
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (getIntent() != null) {
            Log.d("호출", "onNewIntent");
//            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("first")).commit();

            if(getIntent().getStringExtra("index").equals("departure") || getIntent().getStringExtra("index").equals("arrive")){
                Intent secondIntent = getIntent();
                String arg1 = secondIntent.getStringExtra("arg1");
                String arg2 = secondIntent.getStringExtra("arg2");
                String arg3 = secondIntent.getStringExtra("arg3");
                String index = secondIntent.getStringExtra("index");

                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment1, "first").commit();


                //번들객체 생성, text값 저장
                Bundle bundle = new Bundle();
                bundle.putString("arg1",arg1);
                bundle.putString("arg2",arg2);
                bundle.putString("arg3",arg3);
                bundle.putString("index", index);
                Log.d("arg123",arg1+arg2+arg3+" : "+index+"!!!");

                fragment1.setArguments(bundle);
            }else if(getIntent().getStringExtra("index").equals("userinfo")) {
                Intent secondIntent = getIntent();
                String arg1 = secondIntent.getStringExtra("arg1");
                String arg2 = secondIntent.getStringExtra("arg2");
                String arg3 = secondIntent.getStringExtra("arg3");
                String index = secondIntent.getStringExtra("index");

                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment3, "third").commit();

                //번들객체 생성, text값 저장
                Bundle bundle = new Bundle();
                bundle.putString("arg1",arg1);
                bundle.putString("arg2",arg2);
                bundle.putString("arg3",arg3);
                bundle.putString("index", index);
                Log.d("arg123",arg1+arg2+arg3+" : "+index+"!!!");

                fragment3.setArguments(bundle);
            }
        }
    }


    public void sToast(String sToast) {
        Toast.makeText(getApplicationContext(),
                sToast, Toast.LENGTH_SHORT).show();
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
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection(getApplicationContext());
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("logout", result);
        }
    }
}