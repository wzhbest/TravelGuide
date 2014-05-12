package baidumapsdk.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

/**
 * ��ʾ��ͼ���ţ���ת���ӽǿ���
 */
public class MapControlDemo extends Activity {

	/**
	 *  MapView �ǵ�ͼ���ؼ�
	 */
	private MapView mMapView = null;
	/**
	 *  ��MapController��ɵ�ͼ���� 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener ���ڴ����ͼ�¼��ص�
	 */
	MKMapViewListener mMapListener = null;
	/**
	 * ���ڽػ�������
	 */
	MKMapTouchListener mapTouchListener = null; 
	/**
	 * ��ǰ�ص����
	 */
	private GeoPoint currentPt = null; 
	/**
	 * ���ư�ť
	 */
	private Button zoomButton = null;
	private Button rotateButton = null;
	private Button overlookButton =null;
	private Button saveScreenButton = null;
	/**
	 * 
	 */
	private String touchType = null;
	/**
	 * ������ʾ��ͼ״̬�����
	 */
	private TextView mStateBar = null;
	
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
        /**
          * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
          */
        setContentView(R.layout.activity_mapcontrol);
        mMapView = (MapView)findViewById(R.id.bmapView);
        /**
         * ��ȡ��ͼ������
         */
        mMapController = mMapView.getController();
        /**
         *  ���õ�ͼ�Ƿ���Ӧ����¼�  .
         */
        mMapController.enableClick(true);
        /**
         * ���õ�ͼ���ż���
         */
        mMapController.setZoom(12);
        
        mStateBar = (TextView) findViewById(R.id.state);
        /**
         * ��ʼ����ͼ�¼�����
         */
        initListener();
       
        /**
         * ����ͼ�ƶ����찲��
         * ʹ�ðٶȾ�γ�����꣬����ͨ��http://api.map.baidu.com/lbsapi/getpoint/index.html��ѯ��������
         * �����Ҫ�ڰٶȵ�ͼ����ʾʹ����������ϵͳ��λ�ã��뷢�ʼ���mapapi@baidu.com��������ת���ӿ�
         */
        double cLat = 39.945 ;
        double cLon = 116.404 ;
        GeoPoint p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
        mMapController.setCenter(p);
        
    }
    
    private void initListener() {
    	/**
         * ���õ�ͼ����¼����� 
         */
        mapTouchListener = new MKMapTouchListener(){
			@Override
			public void onMapClick(GeoPoint point) {
				touchType = "����";
				currentPt = point;
				updateMapState();
				
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				touchType = "˫��";
				currentPt = point;
				updateMapState();
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				touchType = "����";
				currentPt = point;
				updateMapState();
			}
        };
        mMapView.regMapTouchListner(mapTouchListener);
        /**
         * ���õ�ͼ�¼�����
         */
        mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * �ڴ˴����ͼ�ƶ���ɻص�
				 * ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
				 */
				updateMapState();
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * �ڴ˴����ͼpoi����¼�
				 * ��ʾ��ͼpoi���Ʋ��ƶ����õ�
				 * ���ù��� mMapController.enableClick(true); ʱ���˻ص����ܱ�����
				 * 
				 */
				
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 *  �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ����
				 *  ���ڴ˱����ͼ���洢�豸
				 */
				File file = new File("/mnt/sdcard/test.png");
                FileOutputStream out;
                try{
                    out = new FileOutputStream(file);
                    if(b.compress(Bitmap.CompressFormat.PNG, 70, out)) 
                    {
                        out.flush();
                        out.close();
                    }
                    Toast.makeText(MapControlDemo.this, 
                    	    "��Ļ��ͼ�ɹ���ͼƬ����: "+file.toString(),	
                    		 Toast.LENGTH_SHORT)
                         .show();
                } 
                catch (FileNotFoundException e) 
                {
                    e.printStackTrace();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace(); 
                }
                
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
				 */
				updateMapState();
			}

			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager, mMapListener);
		/**
		 * ���ð�������
		 */
		zoomButton        = (Button)findViewById(R.id.zoombutton);
		rotateButton      = (Button)findViewById(R.id.rotatebutton);
		overlookButton    = (Button)findViewById(R.id.overlookbutton);
		saveScreenButton  = (Button)findViewById(R.id.savescreen);
		OnClickListener onClickListener = new OnClickListener(){
			@Override
			public void onClick(View view) {
				if ( view.equals(zoomButton)){
					perfomZoom();
				}
				else if( view.equals(rotateButton)){
					perfomRotate();
				}
				else if( view.equals(overlookButton)){
					perfomOverlook();
				}
				else if ( view.equals(saveScreenButton)){
					//��ͼ����MKMapViewListener�б���ͼƬ
				     mMapView.getCurrentMap();	
				     Toast.makeText(MapControlDemo.this, 
				    		 "���ڽ�ȡ��ĻͼƬ...", 
				    		 Toast.LENGTH_SHORT ).show();
				          
				}
				updateMapState();
			}
			
		};
		zoomButton.setOnClickListener(onClickListener);
		rotateButton.setOnClickListener(onClickListener);
		overlookButton.setOnClickListener(onClickListener);
		saveScreenButton.setOnClickListener(onClickListener);
    }
    /**
     * ��������
     * sdk ���ż���Χ�� [3.0,19.0]
     */
    private void perfomZoom(){
    	EditText  t  = (EditText) findViewById(R.id.zoomlevel);
    	try{
    	    float zoomLevel = Float.parseFloat(t.getText().toString());
    	    mMapController.setZoom(zoomLevel);
    	}catch(NumberFormatException e){
    		Toast.makeText(this, 
    	         "��������ȷ�����ż���", Toast.LENGTH_SHORT)
    		     .show();
    	}
    }
    /**
     * ������ת 
     * ��ת�Ƿ�Χ�� -180 ~ 180 , ��λ����   ��ʱ����ת  
     */
    private void perfomRotate(){
    	EditText  t  = (EditText) findViewById(R.id.rotateangle);
    	try{
    	    int rotateAngle = Integer.parseInt(t.getText().toString());
    	    mMapController.setRotation(rotateAngle);
    	}catch(NumberFormatException e){
    		Toast.makeText(this, 
    	         "��������ȷ����ת�Ƕ�", Toast.LENGTH_SHORT)
    		     .show();
    	}
    }
    /**
     * ������
     * ���Ƿ�Χ��  -45 ~ 0 , ��λ�� ��
     */
    private void perfomOverlook(){
    	EditText  t  = (EditText) findViewById(R.id.overlookangle);
    	try{
    	    int overlookAngle = Integer.parseInt(t.getText().toString());
    	    mMapController.setOverlooking(overlookAngle);
    	}catch(NumberFormatException e){
    		Toast.makeText(this, 
    	         "��������ȷ�ĸ���", Toast.LENGTH_SHORT)
    		     .show();
    	}	
    }
    
    /**
     * ���µ�ͼ״̬��ʾ���
     */
    private void updateMapState(){
    	   if ( mStateBar == null){
    		   return ;
    	   }
    		String state  = "";
    		if ( currentPt == null ){
    			state = "�����������˫����ͼ�Ի�ȡ��γ�Ⱥ͵�ͼ״̬";
    		}
    		else{
    			state = String.format(touchType+",��ǰ���� �� %f ��ǰγ�ȣ�%f",currentPt.getLongitudeE6()*1E-6,currentPt.getLatitudeE6()*1E-6);
    		}
    		state += "\n";
    		state += String.format("zoom level= %.1f    rotate angle= %d   overlaylook angle=  %d",
                mMapView.getZoomLevel(), 
                mMapView.getMapRotation(),
                mMapView.getMapOverlooking() 
    	    );
    		mStateBar.setText(state);
    }
    
    @Override
    protected void onPause() {
    	/**
    	 *  MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
    	 */
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	/**
    	 *  MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
    	 */
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	/**
    	 *  MapView������������Activityͬ������activity����ʱ�����MapView.destroy()
    	 */
        mMapView.destroy();
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
    
}
