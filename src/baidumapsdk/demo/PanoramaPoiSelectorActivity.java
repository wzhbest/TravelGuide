package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

public class PanoramaPoiSelectorActivity extends FragmentActivity {

    MKSearch mSearch = null;
    MapView mMapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new DemoApplication.MyGeneralListener());
        }
        /**
         * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
         */
		setContentView(R.layout.activity_panorama_poi_selector);
        initMap();
        initSearcher();
	}

    private void initMap(){
        mMapView = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapView();
        GeoPoint p = new GeoPoint((int)(39.945 * 1E6), (int)(116.404 * 1E6));
        mMapView.getController().setCenter(p);
        mMapView.getController().setZoom(13);
    }

    private void initSearcher(){
        mSearch = new MKSearch();
        mSearch.init(((DemoApplication) this.getApplication()).mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiResult(MKPoiResult res, int type, int iError) {
                if (iError != 0){
                    Toast.makeText(PanoramaPoiSelectorActivity.this,
                            "��Ǹ��δ���ҵ����",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (res.getCurrentNumPois() > 0) {
                    // ��poi�����ʾ����ͼ��
                    SelectPoiOverlay poiOverlay = new SelectPoiOverlay(PanoramaPoiSelectorActivity.this, mMapView);
                    poiOverlay.setData(res.getAllPoi());
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    //��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
                    for( MKPoiInfo info : res.getAllPoi() ){
                        if ( info.pt != null ){
                            mMapView.getController().animateTo(info.pt);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
            }

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
            }

            @Override
            public void onGetAddrResult(MKAddrInfo result, int iError) {
            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
            }

            @Override
            public void onGetPoiDetailSearchResult(int type, int iError) {
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {

            }
        });
    }

    public void doPoiSearch(View v) {
        mSearch.poiSearchInCity(((EditText) findViewById(R.id.city)).getText().toString(),
                ((EditText) findViewById(R.id.key)).getText().toString());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
       if (mSearch!=null){
           mSearch.destory();
           mSearch = null;
       }
    }

    private class SelectPoiOverlay extends PoiOverlay {


        public SelectPoiOverlay(Activity activity, MapView mapView) {
            super(activity, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            super.onTap(i);
            MKPoiInfo info = getPoi(i);
            if (!info.isPano) {
               Toast.makeText(PanoramaPoiSelectorActivity.this,
                       "��ǰPOI��������ȫ����Ϣ",Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent();
                intent.setClass(PanoramaPoiSelectorActivity.this,
                        PanoramaDemoActivityMain.class);
                intent.putExtra("uid",info.uid);
                startActivity(intent);
            }
            return true;
        }
    }

}
