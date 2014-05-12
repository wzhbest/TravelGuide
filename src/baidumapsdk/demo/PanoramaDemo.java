package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.travel.guide.R;


public class PanoramaDemo extends Activity {
    //ͨ��ȫ��ID��ȫ����ʹ�õ�Ĭ��ID��ȫ��ID����ʹ��PanoramaService��ѯ�õ�
    public static final String DEFAULT_PANORAMA_ID ="0100220000130817164838355J5";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_panorama_demo);
	}

    //ͨ��poi uid ��ȫ��
    public void startPoiSelector(View v){
        Intent intent = new Intent();
        intent.setClass(this, PanoramaPoiSelectorActivity.class);
        startActivity(intent);
    }

    //ͨ����γ�����꿪��ȫ��
    public void startGeoSelector(View v){
        Intent intent = new Intent();
        intent.setClass(this, PanoramaGeoSelectorActivity.class);
        startActivity(intent);
    }
    //ͨ��ȫ��ID����ȫ��
    public void startIDSelector(View v){
        Intent intent = new Intent();
        intent.setClass(this, PanoramaDemoActivityMain.class);
        intent.putExtra("pid",DEFAULT_PANORAMA_ID);
        startActivity(intent);
    }


}
