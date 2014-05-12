package baidumapsdk.demo;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.cloud.BoundSearchInfo;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

public class CloudSearchActivity extends Activity implements CloudListener {
    
    MapView mMapView;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        /**
         * 浣跨敤鍦板浘sdk鍓嶉渶鍏堝垵濮嬪寲BMapManager.
         * BMapManager鏄叏灞�殑锛屽彲涓哄涓狹apView鍏辩敤锛屽畠闇�鍦板浘妯″潡鍒涘缓鍓嶅垱寤猴紝
         * 骞跺湪鍦板浘鍦板浘妯″潡閿�瘉鍚庨攢姣侊紝鍙杩樻湁鍦板浘妯″潡鍦ㄤ娇鐢紝BMapManager灏变笉搴旇閿�瘉
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 濡傛灉BMapManager娌℃湁鍒濆鍖栧垯鍒濆鍖朆MapManager
             */
            app.mBMapManager.init(new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.lbssearch);
        CloudManager.getInstance().init(CloudSearchActivity.this);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
        mMapView.setDoubleClickZooming(true);
        findViewById(R.id.regionSearch).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						LocalSearchInfo info = new LocalSearchInfo();
						info.ak = "B266f735e43ab207ec152deff44fec8b";
						info.geoTableId = 31869;
						info.tags = "";
						info.q="天安门";
						info.region = "北京市";
						CloudManager.getInstance().localSearch(info);
					}
				});
		findViewById(R.id.nearbySearch).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						NearbySearchInfo info = new NearbySearchInfo();
						info.ak = "D9ace96891048231e8777291cda45ca0";
						info.geoTableId = 32038;
						info.filter="time:20130801,20130810";
						info.location = "116.403689,39.914957";
						info.radius = 30000;
						CloudManager.getInstance().nearbySearch(info);
					}
				});

		findViewById(R.id.boundsSearch).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						BoundSearchInfo info = new BoundSearchInfo();
						info.ak = "B266f735e43ab207ec152deff44fec8b";
						info.geoTableId = 31869;
						info.q = "天安门";
						info.bound="116.401663,39.913961;116.406529,39.917396";
						CloudManager.getInstance().boundSearch(info);
					}
				});
		findViewById(R.id.detailsSearch).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						DetailSearchInfo info = new DetailSearchInfo();
						info.ak = "B266f735e43ab207ec152deff44fec8b";
						info.geoTableId = 31869;
						info.uid = 18622266;
						CloudManager.getInstance().detailSearch(info);
					}
				});
    }
    
    
    @Override
    protected void onDestroy() {
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }

	public void onGetDetailSearchResult(DetailSearchResult result, int error) {
		if (result != null) {
            if (result.poiInfo != null) {
                Toast.makeText(CloudSearchActivity.this, result.poiInfo.title, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CloudSearchActivity.this, "status:" + result.status, Toast.LENGTH_SHORT).show();
            }
        }
	}

	public void onGetSearchResult(CloudSearchResult result, int error) {
		if (result != null && result.poiList!= null && result.poiList.size() > 0) {
            CloudOverlay poiOverlay = new CloudOverlay(this,mMapView);
            poiOverlay.setData(result.poiList);
            mMapView.getOverlays().clear();
            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            mMapView.getController().animateTo(new GeoPoint((int)(result.poiList.get(0).latitude * 1e6), (int)(result.poiList.get(0).longitude * 1e6)));
        }
	}
}
class CloudOverlay extends ItemizedOverlay {

    List<CloudPoiInfo> mLbsPoints;
    Activity mContext;
    
    public CloudOverlay(Activity context ,MapView mMapView) {
        super(null,mMapView);
        mContext = context;
    }

    public void setData(List<CloudPoiInfo> lbsPoints) {
        if (lbsPoints != null) {
            mLbsPoints = lbsPoints;
        }
        for ( CloudPoiInfo rec : mLbsPoints ){
            GeoPoint pt = new GeoPoint((int)(rec.latitude * 1e6), (int)(rec.longitude * 1e6));
            OverlayItem item = new OverlayItem(pt , rec.title, rec.address);
            Drawable marker1 = this.mContext.getResources().getDrawable(R.drawable.icon_marka);
            item.setMarker(marker1);
            addItem(item);
        }
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
    @Override
    protected boolean onTap(int arg0) {
        CloudPoiInfo item = mLbsPoints.get(arg0);
        Toast.makeText(mContext, item.title,Toast.LENGTH_LONG).show();
        return super.onTap(arg0);
    }
    
}
