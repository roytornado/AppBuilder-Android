package com.royalnext.base.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.royalnext.base.R;
import com.royalnext.base.activity.BaseActivity;

/**
 * Created by royng on 19/6/14.
 */
public class GoogleMapDemo extends BaseActivity {
    private double mLat;
    private double mLong;
    private GoogleMap mMap;
    private Button locUse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.demo_activity_map);
        mLat = getIntent().getDoubleExtra("mLat", -1);
        mLong = getIntent().getDoubleExtra("mLong", -1);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLong), 18f));
        //locUse = (Button) findViewById(R.id.info_btn1);
        locUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = mMap.getCameraPosition().target.latitude;
                double lon = mMap.getCameraPosition().target.longitude;
                Intent intent = new Intent();
                intent.putExtra("mLat", lat);
                intent.putExtra("mLong", lon);
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }
}
