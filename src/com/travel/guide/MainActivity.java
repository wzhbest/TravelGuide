package com.travel.guide;

import baidumapsdk.demo.LocationOverlayDemo;
import baidumapsdk.demo.PoiSearchDemo;
import baidumapsdk.demo.RoutePlanDemo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
	}

	public void onClickButton(View v) {
		int id = v.getId();
		if (id == R.id.main_about_btn) {
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
		} else if (id == R.id.main_attractions_btn) {
			Intent intent = new Intent();
			intent.setClass(this, PoiSearchDemo.class);
			startActivity(intent);
		} else if (id == R.id.main_route_btn) {
			Intent intent = new Intent();
			intent.setClass(this, RoutePlanDemo.class);
			startActivity(intent);
		} else if (id == R.id.main_travellog_btn) {
			Intent intent = new Intent();
			intent.setClass(this, TravelLogActivity.class);
			startActivity(intent);
		} else if (id == R.id.main_favorites_btn) {
			Intent intent = new Intent();
			intent.setClass(this, FavoritesActivity.class);
			startActivity(intent);
		} else if (id == R.id.current_site) {
            Intent intent = new Intent();
            intent.setClass(this, LocationOverlayDemo.class);
            startActivity(intent);
        }

	}

}
