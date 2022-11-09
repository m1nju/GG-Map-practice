package com.example.ggmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NewSearchResultActivity extends AppCompatActivity {
    private TMapView tMapView;
    private ArrayList<TMapMarkerItem> markerItems = new ArrayList<>();
    private TMapPoint tMapPoint;
    public static TMapPoint tMapPointStart;
    public static TMapPoint tMapPointEnd;

    public static TMapPoint passListPoint;
    public static ArrayList<TMapPoint> passList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search_result);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx18a7622afffe4a6191d0850d7beae5e0");
        tMapView.setZoomLevel(15);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        TMapData tMapData = new TMapData();

        //New code - 시작점, 도착점 가져오기
        Intent receiveIntent = getIntent();

        double start_latitude = receiveIntent.getDoubleExtra("start_latitude",0.0);
        double start_longitude = receiveIntent.getDoubleExtra("start_longitude",0.0);
        tMapPointStart = new TMapPoint(start_latitude, start_longitude);

        double end_latitude = receiveIntent.getDoubleExtra("end_latitude",0.0);
        double end_longitude = receiveIntent.getDoubleExtra("end_longitude",0.0);
        tMapPointEnd = new TMapPoint(end_latitude, end_longitude);


        // -- 출발지 좌표 마커 생성 --
        TMapMarkerItem markerItem_start = new TMapMarkerItem();

        //마커 아이콘 가져오기
        Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.location_depart);
        //출발지 마커
        markerItem_start.setIcon(bitmap_start); // 마커 아이콘 지정
        markerItem_start.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem_start.setTMapPoint(tMapPointStart); // 마커의 좌표 지정
        markerItem_start.setName("출발지 마커"); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem_start", markerItem_start); // 지도에 마커 추가

        // -- 도착지 좌표 마커 생성 --
        TMapMarkerItem markerItem_end = new TMapMarkerItem();

        //마커 아이콘 가져오기
        Bitmap bitmap_end = BitmapFactory.decodeResource(getResources(), R.drawable.location_arriv);
        //출발지 마커
        markerItem_end.setIcon(bitmap_end); // 마커 아이콘 지정
        markerItem_end.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem_end.setTMapPoint(tMapPointEnd); // 마커의 좌표 지정
        markerItem_end.setName("도착지 마커"); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem_end", markerItem_end); // 지도에 마커 추가

        //해당 좌표로 지도 이동
        //tMapView.setCenterPoint(start_latitude, start_longitude); // 이거 넣으면 티맵이 안보임


        //길찾기 버튼
        ImageButton streetfindBtn = (ImageButton) findViewById(R.id.btn_streetfind);
        streetfindBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //목적지로 지도 이동
                tMapView.setCenterPoint((float) tMapPointEnd.getLongitude(), (float) tMapPointEnd.getLatitude());

                //경유지
                passListPoint = new TMapPoint(37.591620,127.019373);
                passList.add(passListPoint);

                //보행자 경로로 PolyLine 띄우기
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            TMapPolyLine tMapPolyLine1 = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                            tMapPolyLine1.setLineColor(Color.BLUE);
                            tMapPolyLine1.setLineWidth(20);
                            tMapPolyLine1.setOutLineWidth(20);
                            tMapPolyLine1.setLineColor(Color.parseColor("#3094ff"));
                            tMapPolyLine1.setOutLineColor(Color.parseColor("#002247"));
                            tMapView.addTMapPolyLine("PolyLine_streetfind", tMapPolyLine1);

                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, passList, 4);
                            tMapPolyLine.setLineColor(Color.RED);
                            tMapPolyLine.setLineWidth(10);
                            tMapPolyLine.setOutLineWidth(10);
                            tMapPolyLine.setLineColor(Color.parseColor("#FF0000"));
                            tMapPolyLine.setOutLineColor(Color.parseColor("#FF0000"));
                            tMapView.addTMapPolyLine("PolyLine_streetfind1", tMapPolyLine);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }

        });

        // 기존 findAllPOI 제거
        // findAllPOI 안에 있던 길찾기 그냥 빼서 위에다가 씀

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
