package com.example.ggmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

public class SearchResultActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_search_result);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx18a7622afffe4a6191d0850d7beae5e0");
        tMapView.setZoomLevel(15);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        Intent receiveIntent = getIntent();
        String address = receiveIntent.getStringExtra("address");

        TextView tv_search_address2 = findViewById(R.id.tv_search_address2);
        tv_search_address2.setText(address);

        TMapData tMapData = new TMapData();
        tMapData.findAllPOI(address, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    TMapPOIItem item = arrayList.get(i);

                    ArrayList<String> point = new ArrayList<>(Arrays.asList(item.getPOIPoint().toString().split(" ")));
                    float lat = Float.parseFloat(point.get(1));
                    float lon = Float.parseFloat(point.get(3));

                    //?????? ??????
                    markerItems.add(new TMapMarkerItem());
                    tMapPoint = new TMapPoint(lat, lon);

                    Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_result);
                    markerItems.get(i).setIcon(bitmap_start); // ?????? ????????? ??????
                    markerItems.get(i).setPosition(0.5f, 1.0f); // ????????? ???????????? ??????, ???????????? ??????
                    markerItems.get(i).setTMapPoint(tMapPoint); // ????????? ?????? ??????
                    markerItems.get(i).setName(item.getPOIName()); // ????????? ????????? ??????

                    //ic_input_get
                    Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(SearchResultActivity.this, R.drawable.ic_selection)).getBitmap();
                    markerItems.get(i).setCalloutRightButtonImage(right);

                    markerItems.get(i).setCanShowCallout(true);
                    markerItems.get(i).setCalloutTitle(item.getPOIName());
                    tMapView.addMarkerItem("searchItem" + i, markerItems.get(i)); // ????????? ?????? ??????


                    //????????? ??????
                    Button startSetBtn = (Button) findViewById(R.id.btn_set_start);
                    startSetBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(SearchResultActivity.this, "???????????? ??????????????????.", Toast.LENGTH_LONG).show();

                            //????????? ????????? ?????????
                            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
                                @Override
                                public void onCalloutRightButton(TMapMarkerItem markerItem){

                                    final String getName = markerItem.getName();
                                    final double getlat = markerItem.getTMapPoint().getLatitude();
                                    final double getlon = markerItem.getTMapPoint().getLongitude();

                                    System.out.println(getName + getlat + getlon);

                                    double start_lat = getlat;
                                    double start_lon = getlon;

                                    //????????? ??????
                                    tMapPointStart = new TMapPoint(start_lat, start_lon);
                                    Toast.makeText(SearchResultActivity.this, "???????????? ?????????????????????.", Toast.LENGTH_LONG).show();

                                    //?????? ??????
                                    TMapMarkerItem markerItem_start = new TMapMarkerItem();

                                    //?????? ????????? ????????????
                                    Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.location_depart);
                                    //????????? ??????
                                    markerItem_start.setIcon(bitmap_start); // ?????? ????????? ??????
                                    markerItem_start.setPosition(0.5f, 1.0f); // ????????? ???????????? ??????, ???????????? ??????
                                    markerItem_start.setTMapPoint(tMapPointStart); // ????????? ?????? ??????
                                    markerItem_start.setName("????????? ??????"); // ????????? ????????? ??????
                                    tMapView.addMarkerItem("markerItem_start", markerItem_start); // ????????? ?????? ??????
                                }
                            });
                        }
                    });

                    //????????? ??????
                    Button endSetBtn = (Button) findViewById(R.id.btn_set_end);
                    endSetBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(SearchResultActivity.this, "???????????? ??????????????????.", Toast.LENGTH_LONG).show();

                            //????????? ????????? ?????????
                            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
                                @Override
                                public void onCalloutRightButton(TMapMarkerItem markerItem){

                                    final String getName = markerItem.getName();
                                    final double getlat = markerItem.getTMapPoint().getLatitude();
                                    final double getlon = markerItem.getTMapPoint().getLongitude();

                                    System.out.println(getName + getlat + getlon);

                                    double end_lat = getlat;
                                    double end_lon = getlon;

                                    //????????? ??????
                                    tMapPointEnd = new TMapPoint(end_lat, end_lon);
                                    Toast.makeText(SearchResultActivity.this, "???????????? ?????????????????????.", Toast.LENGTH_LONG).show();


                                    //?????? ??????
                                    TMapMarkerItem markerItem_end = new TMapMarkerItem();

                                    //?????? ????????? ????????????
                                    Bitmap bitmap_end = BitmapFactory.decodeResource(getResources(), R.drawable.location_arriv);
                                    //????????? ??????
                                    markerItem_end.setIcon(bitmap_end); // ?????? ????????? ??????
                                    markerItem_end.setPosition(0.5f, 1.0f); // ????????? ???????????? ??????, ???????????? ??????
                                    markerItem_end.setTMapPoint(tMapPointEnd); // ????????? ?????? ??????
                                    markerItem_end.setName("????????? ??????"); // ????????? ????????? ??????
                                    tMapView.addMarkerItem("markerItem_end", markerItem_end); // ????????? ?????? ??????
                                }
                            });
                        }
                    });

                    //????????? ??????
                    ImageButton streetfindBtn = (ImageButton) findViewById(R.id.btn_streetfind);
                    streetfindBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //???????????? ?????? ??????
                            tMapView.setCenterPoint((float) tMapPointEnd.getLongitude(), (float) tMapPointEnd.getLatitude());

                            //?????????
                            passListPoint = new TMapPoint(37.591620,127.019373);
                            passList.add(passListPoint);

                            //????????? ????????? PolyLine ?????????
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
                }
                tMapView.setCenterPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude());
            }

        });

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
