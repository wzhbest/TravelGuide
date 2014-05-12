package baidumapsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.travel.guide.R;

/**
 * ��ʾpoi�������� 
 */
public class PoiSearchDemo extends Activity {
	
	private MapView mMapView = null;
	private MKSearch mSearch = null;   // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	/**
	 * �����ؼ������봰��
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
    private int load_Index;
	
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
         setContentView(R.layout.activity_poisearch);
        mMapView = (MapView)findViewById(R.id.bmapView);
		mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
		
		// ��ʼ������ģ�飬ע�������¼�����
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
            //�ڴ˴�������ҳ���
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
                if (error != 0) {
                    Toast.makeText(PoiSearchDemo.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("wzhwzh","wzhwzh--onGetPoiResult---this is !!!");
                    Toast.makeText(PoiSearchDemo.this, "�ɹ����鿴����ҳ��", Toast.LENGTH_SHORT).show();
                }
            }
            /**
             * �ڴ˴���poi�������
             */
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                // ����ſɲο�MKEvent�еĶ���
                if (error != 0 || res == null) {
                    Toast.makeText(PoiSearchDemo.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
                // ����ͼ�ƶ�����һ��POI���ĵ�
                if (res.getCurrentNumPois() > 0) {
                    // ��poi�����ʾ����ͼ��
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(PoiSearchDemo.this, mMapView, mSearch);
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
                } else if (res.getCityListNum() > 0) {
                	//������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
                    String strInfo = "��";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "�ҵ����";
                    Log.e("wzhwzh","wzhwzh--onGetPoiResult---strInfo = " + strInfo);
                    Toast.makeText(PoiSearchDemo.this, strInfo, Toast.LENGTH_LONG).show();
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
            public void onGetAddrResult(MKAddrInfo res, int error) {
                Log.e("wzhwzh","wzhwzh--onGetAddrResult---res = " + res);
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
                Log.e("wzhwzh","wzhwzh--MKBusLineResult---result = " + result);
            }
            /**
             * ���½����б�
             */
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
                Log.e("wzhwzh","wzhwzh--onGetSuggestionResult---res = " + res);
            	if ( res == null || res.getAllSuggestions() == null){
            		return ;
            	}
            	sugAdapter.clear();
            	for ( MKSuggestionInfo info : res.getAllSuggestions()){
            		if ( info.key != null)
            		    sugAdapter.add(info.key);
            	}
            	sugAdapter.notifyDataSetChanged();
                
            }
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
			    Log.e("wzhwzh","wzhwzh--onGetShareUrlResult---result = " + result);
				
			}
        });
        
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        
        /**
         * ������ؼ��ֱ仯ʱ����̬���½����б�
         */
        keyWorldsView.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 String city =  ((EditText)findViewById(R.id.city)).getText().toString();
				 /**
				  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				  */
                 mSearch.suggestionSearch(cs.toString(), city);				
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
    /**
     * Ӱ��������ť����¼�
     * @param v
     */
    public void searchButtonProcess(View v) {
          EditText editCity = (EditText)findViewById(R.id.city);
          EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
          mSearch.poiSearchInCity(editCity.getText().toString(), 
                  editSearchKey.getText().toString());
    }
   public void goToNextPage(View v) {
        //������һ��poi
        int flag = mSearch.goToPoiPage(++load_Index);
        if (flag != 0) {
            Toast.makeText(PoiSearchDemo.this, "��������ʼ��Ȼ����������һ������", Toast.LENGTH_SHORT).show();
        }
    }
}
