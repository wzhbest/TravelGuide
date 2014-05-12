package baidumapsdk.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.panorama.Panorama;
import com.baidu.mapapi.panorama.PanoramaLink;
import com.baidu.mapapi.panorama.PanoramaMarker;
import com.baidu.mapapi.panorama.PanoramaOverlay;
import com.baidu.mapapi.panorama.PanoramaService;
import com.baidu.mapapi.panorama.PanoramaService.PanoramaServiceCallback;
import com.baidu.mapapi.panorama.PanoramaView;
import com.baidu.mapapi.panorama.PanoramaViewCamera;
import com.baidu.mapapi.panorama.PanoramaViewListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

/**
 * ȫ��Demo��Activity
 */
public class PanoramaDemoActivityMain extends Activity implements PanoramaViewListener{
    @SuppressWarnings("unused")
    private static final String LTAG = PanoramaDemoActivityMain.class.getSimpleName();
    private PanoramaView mPanoramaView;
    private PanoramaService mService;
    private PanoramaServiceCallback mCallback;
    private MyOverlay mOverlay = null;
    private int mSrcType = -1;
    private Button mBtn = null;
    private boolean isShowOverlay = true;
    private ProgressDialog pd;
    private TextView mRoadName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //�ȳ�ʼ��BMapManager
        DemoApplication app = (DemoApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);

            app.mBMapManager.init(
                    new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.activity_panorama_main);
        mPanoramaView = (PanoramaView) findViewById(R.id.panorama);
        //UI��ʼ��
        mBtn = (Button) findViewById(R.id.button);
        mBtn.setVisibility(View.INVISIBLE);
        //��·��
        mRoadName = (TextView)findViewById(R.id.road);
        mRoadName.setVisibility(View.VISIBLE);
        mRoadName.setText("�ٶ�ȫ��");
        mRoadName.setBackgroundColor(Color.argb(200, 5, 5, 5));  //����͸����
        mRoadName.setTextColor(Color.argb(255, 250, 250, 250));  //����͸����
        mRoadName.setTextSize(22);
        //��ת������
        pd = new ProgressDialog(PanoramaDemoActivityMain.this);
        pd.setMessage("��ת�С���");   
        pd.setCancelable(true);//���ý������Ƿ���԰��˻ؼ�ȡ�� 

        //��ʼ��Searvice
        mService = PanoramaService.getInstance(getApplicationContext());
        mCallback = new PanoramaServiceCallback() {
            public void onGetPanorama(Panorama p, int error) {
                //ʹ��pid����ʱ��ӱ�ע
                if ( error != 0){
                    Toast.makeText(PanoramaDemoActivityMain.this,
                            "��Ǹ��δ�ܼ�����ȫ������",Toast.LENGTH_LONG).show();
                }
                if (p != null) {
                    mPanoramaView.setPanorama(p);
                    mRoadName.setText(p.getStreetName());
                }
            }
        };
        //����ȫ��ͼ����
        mPanoramaView.setPanoramaViewListener(this);
                
        //��������
        parseInput();
        
    }

    private void parseInput() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (intent.hasExtra("uid")) {
            mSrcType = 1;
            mService.requestPanoramaByPoi(b.getString("uid"), mCallback);
            return;
        }
        if (intent.hasExtra("lat") && intent.hasExtra("lon")) {
            mSrcType = 2;
            mService.requestPanoramaByGeoPoint(new GeoPoint(b.getInt("lat"), b.getInt("lon")), mCallback);
            return;
        }
        if (intent.hasExtra("pid")) {
            mSrcType = 3;
            mService.requestPanoramaById(b.getString("pid"), mCallback);

        }
    }

    //����button���
    public void onButtonClick(View v) {
        if (isShowOverlay) {
            addOverlay();
            mBtn.setText("ɾ����ע");
        } else {
            removeOverlay();
            mBtn.setText("��ӱ�ע");
        }
        isShowOverlay = !isShowOverlay;

    }

    //��ӱ�ע
    private void addOverlay() {
        //�찲������
        GeoPoint p = new GeoPoint(39914195,116403928);
        mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka),
                mPanoramaView);
        PanoramaMarker item = new PanoramaMarker(p);
        item.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        mOverlay.addMarker(item);
        mPanoramaView.getOverlays().add(mOverlay);
        mPanoramaView.refresh();
    }

    //ɾ����ע
    private void removeOverlay() {
        if (mOverlay != null) {
            mPanoramaView.getOverlays().remove(mOverlay);
            mPanoramaView.refresh();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPanoramaView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoramaView.onResume();
        if (mSrcType == 3){
            mBtn.setVisibility(View.VISIBLE);
            ((ViewGroup)findViewById(R.id.layout))
                    .bringChildToFront(mBtn);
        }
    }

    @Override
    protected void onDestroy() {
        mPanoramaView.destroy();
        mService.destroy();
        super.onDestroy();
    }

    public class MyOverlay extends PanoramaOverlay {

        public MyOverlay(Drawable defaultMarker, PanoramaView mapView) {
            super(defaultMarker, mapView);
        }

        @Override
        public boolean onTap(int index) {
            Toast.makeText(PanoramaDemoActivityMain.this,
                    "��ע�ѱ����", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    @Override
    public void beforeMoveToPanorama(String pId) {
        // TODO Auto-generated method stub
        //����������
        pd.show();
    }

    @Override
    public void afterMovetoPanorama(String pId) {
        // TODO Auto-generated method stub
        //���ؽ�����
        pd.dismiss();
    }

    @Override
    public void onPanoramaCameraChange(PanoramaViewCamera camera) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClickPanoramaLink(PanoramaLink link) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPanoramaMoveStart() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPanoramaMoveFinish() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPanoramaAnimationStart() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPanoramaAnimationEnd() {
        // TODO Auto-generated method stub
        
    }

}
