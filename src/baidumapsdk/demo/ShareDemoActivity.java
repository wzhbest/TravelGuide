package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
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

/**
 * ��ʾpoi�������� 
 */
public class ShareDemoActivity extends Activity {
	
	private MapView mMapView = null;
	private MKSearch mSearch = null;   // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	//�������������ַ
	private String currentAddr = null;
	//�������� 
	private String mCity = "����";
	//�����ؼ���
	private String searchKey = "�͹�";
	//��������������
	private GeoPoint mPoint = new GeoPoint((int)(40.056878*1E6),(int)(116.308141*1E6));
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_share_demo_activity);
        mMapView = (MapView)findViewById(R.id.bmapView);
		mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
		
		// ��ʼ������ģ�飬ע�������¼�����
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
        	
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            /**
             * �ڴ˴���poi������� , ��poioverlay ��ʾ
             */
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                // ����ſɲο�MKEvent�еĶ���
                if (error != 0 || res == null) {
                    Toast.makeText(ShareDemoActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
                // ����ͼ�ƶ�����һ��POI���ĵ�
                if (res.getCurrentNumPois() > 0) {
                    // ��poi�����ʾ����ͼ��
                    PoiShareOverlay poiOverlay = new PoiShareOverlay(ShareDemoActivity.this, mMapView);
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
            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                    int error) {
            }
            public void onGetTransitRouteResult(MKTransitRouteResult res,
                    int error) {
            }
            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                    int error) {
            }
            /**
             * �ڴ˴����������
             */
            public void onGetAddrResult(MKAddrInfo res, int error) {
            	// ����ſɲο�MKEvent�еĶ���
                if (error != 0 || res == null) {
                    Toast.makeText(ShareDemoActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
                AddrShareOverlay addrOverlay = new AddrShareOverlay(
                		getResources().getDrawable(R.drawable.icon_marka),mMapView , res);
                mMapView.getOverlays().clear();
                mMapView.getOverlays().add(addrOverlay);
                mMapView.refresh();
                
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }
            
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				//����̴����
				Intent it = new Intent(Intent.ACTION_SEND);  
				it.putExtra(Intent.EXTRA_TEXT, "��������ͨ���ٶȵ�ͼSDK��������һ��λ��: "+
						       currentAddr+
						       " -- "+result.url);  
				it.setType("text/plain");  
				startActivity(Intent.createChooser(it, "���̴�����"));
				
			}
        });
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
    protected void onDestroy(){
    	mMapView.destroy();
    	mSearch.destory();
    	super.onDestroy();
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
    
    private void initMapView() {
        mMapView.setLongClickable(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
    }
   
	public void sharePoi(View view){
		//����poi����
    	mSearch.poiSearchInCity(mCity, searchKey);
    	Toast.makeText(this,
    			"��"+mCity+"���� "+searchKey,
    			Toast.LENGTH_SHORT).show();
    }
    
    public void shareAddr(View view){
    	//���𷴵����������
    	mSearch.reverseGeocode(mPoint);
    	Toast.makeText(this,
    			String.format("����λ�ã� %f��%f", (mPoint.getLatitudeE6()*1E-6),(mPoint.getLongitudeE6()*1E-6)),
    			Toast.LENGTH_SHORT).show();
    }
    
    /**
     * ʹ��PoiOverlay չʾpoi�㣬��poi�����ʱ����̴�����.
     *
     */
    private class PoiShareOverlay extends PoiOverlay {

        public PoiShareOverlay(Activity activity, MapView mapView) {
            super(activity, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            MKPoiInfo info = getPoi(i);
            currentAddr = info.address;  	
            mSearch.poiDetailShareURLSearch(info.uid);
            return true;
        }        
    }
    /**
     * ʹ��ItemizevOvelrayչʾ����������λ�ã����õ㱻���ʱ����̴�����.
     *
     */
  private class AddrShareOverlay extends ItemizedOverlay {

	  private MKAddrInfo addrInfo ;
	  public AddrShareOverlay(Drawable defaultMarker, MapView mapView , MKAddrInfo addrInfo) {
		super(defaultMarker, mapView);
		this.addrInfo = addrInfo;
		addItem(new OverlayItem(addrInfo.geoPt,addrInfo.strAddr,addrInfo.strAddr));
	}
	  
	@Override
	public boolean onTap(int index){
		currentAddr = addrInfo.strAddr;
	    mSearch.poiRGCShareURLSearch(addrInfo.geoPt, "�����ַ", addrInfo.strAddr);	
	    return true;
	}
	  
  }
}
