package baidumapsdk.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

/**
 * ��һ��Activity��չʾ�����ͼ
 */
public class MutiMapViewDemo extends FragmentActivity {

    private MapController mMapController1, mMapController2, mMapController3, mMapController4;

    private static final GeoPoint GEO_BEIJING = new GeoPoint((int) (39.945 * 1E6), (int) (116.404 * 1E6));
    private static final GeoPoint GEO_SHANGHAI = new GeoPoint((int) (31.227 * 1E6), (int) (121.481 * 1E6));
    private static final GeoPoint GEO_GUANGZHOU = new GeoPoint((int) (23.155 * 1E6), (int) (113.264 * 1E6));
    private static final GeoPoint GEO_SHENGZHENG = new GeoPoint((int) (22.560 * 1E6), (int) (114.064 * 1E6));


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
        setContentView(R.layout.activity_mutimap);
        initMap();
    }

    /**
     * ��ʼ��Map
     */
    private void initMap() {
        // ������
        if (mMapController1 == null) {
            mMapController1 = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map1)).getMapView().getController();
            mMapController1.setMapStatus(newMapStatusWithGeoPointAndZoom(GEO_BEIJING, 10));
        }

        // �Ϻ���
        if (mMapController2 == null) {
            mMapController2 = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map2)).getMapView().getController();
            mMapController2.setMapStatus(newMapStatusWithGeoPointAndZoom(GEO_SHANGHAI, 10));
        }

        // ������
        if (mMapController3 == null) {
            mMapController3 = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map3)).getMapView().getController();
            mMapController3.setMapStatus(newMapStatusWithGeoPointAndZoom(GEO_GUANGZHOU, 10));
        }

        // ������
        if (mMapController4 == null) {
            mMapController4 = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map4)).getMapView().getController();
            mMapController4.setMapStatus(newMapStatusWithGeoPointAndZoom(GEO_SHENGZHENG, 10));
        }
    }

    private MKMapStatus newMapStatusWithGeoPointAndZoom(GeoPoint p, float zoom) {
        MKMapStatus status = new MKMapStatus();
        status.targetGeo = p;
        status.zoom = zoom;
        return status;
    }

}
