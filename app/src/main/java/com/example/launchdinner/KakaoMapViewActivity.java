package com.example.launchdinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class KakaoMapViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = "KakaoActivity";
    private MapView mMapView;
    public Intent intent;
    MapPoint startMapPoint ;
    MapPoint endMapPoint ;
    Geocoder geocoder;
    List<Address> list1 = null;
    List<Address> list2 = null;
    String start_address1, start_address2, end_address1, end_address2;

    String url ="kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=FOOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map_view);

        geocoder = new Geocoder(getApplicationContext());


        if(getIntent() != null){
            Intent intent = getIntent();
            start_address1 = intent.getStringExtra("start_address1");
            start_address2 = intent.getStringExtra("start_address2");
            end_address1 = intent.getStringExtra("end_address1");
            end_address2 = intent.getStringExtra("end_address2");
            Log.d("list..0", start_address1+"#########"+end_address1);

        }
        try {
            list1 = geocoder.getFromLocationName(start_address1,1);
            list2 = geocoder.getFromLocationName(end_address1,1);
//            Log.d("list...", list1.toString());
//            Log.d("list...9", list1.get(0).getLatitude());
            Log.d("list..1", list1.get(0).toString());
            Log.d("list..2", list2.get(0).toString());

            startMapPoint = MapPoint.mapPointWithGeoCoord(list1.get(0).getLatitude(), list1.get(0).getLongitude());
            endMapPoint = MapPoint.mapPointWithGeoCoord(list2.get(0).getLatitude(), list2.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMapView = new MapView(this);
//        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


//        1. 위도, 경도 구하기
//        2. 지도의 중심점 설정
//        3. 마커 생성

        MapPOIItem marker1 = new MapPOIItem();
        marker1.setItemName("출발지 \n"+ start_address1+"\n"+start_address2);
        marker1.setTag(0);
        marker1.setMapPoint(startMapPoint);
        marker1.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker1.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        MapPOIItem marker2 = new MapPOIItem();
        marker2.setItemName("도착지 \n"+ end_address1+"\n"+end_address2);
        marker2.setTag(1);
        marker2.setMapPoint(endMapPoint);
        marker2.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mMapView.addPOIItem(marker1);
        mMapView.addPOIItem(marker2);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mMapView);
    }
}