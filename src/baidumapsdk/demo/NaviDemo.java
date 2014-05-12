package baidumapsdk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

public class NaviDemo extends Activity {
	
	//�찲������
	double mLat1 = 39.915291; 
   	double mLon1 = 116.403857; 
   	//�ٶȴ�������
   	double mLat2 = 40.056858;   
   	double mLon2 = 116.308194;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_demo);
		TextView text = (TextView)findViewById(R.id.navi_info);
		text.setText(String.format("���:(%f,%f)\n�յ�:(%f,%f)",mLat1,mLon1,mLat2,mLon2));
	}
   /**
    * ��ʼ����		
    * @param view
    */
   public void startNavi(View view){		
		int lat = (int) (mLat1 *1E6);
	   	int lon = (int) (mLon1 *1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
		lat = (int) (mLat2 *1E6);
	   	lon = (int) (mLon2 *1E6);
	    GeoPoint pt2 = new GeoPoint(lat, lon);
	    // ���� ��������
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName= "�����￪ʼ";
        para.endPoint  = pt2;
        para.endName   = "���������";
        
        try {
        	
			 BaiduMapNavigation.openBaiduMapNavi(para, this);
			 
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
			  builder.setMessage("����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�ϰ�װ��");
			  builder.setTitle("��ʾ");
			  builder.setPositiveButton("ȷ��", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				 dialog.dismiss();
				 BaiduMapNavigation.GetLatestBaiduMapApp(NaviDemo.this);
			   }
			  });

			  builder.setNegativeButton("ȡ��", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });

			  builder.create().show();
			 }
		}

	public void startWebNavi(View view) {
		int lat = (int) (mLat1 * 1E6);
		int lon = (int) (mLon1 * 1E6);
		GeoPoint pt1 = new GeoPoint(lat, lon);
		lat = (int) (mLat2 * 1E6);
		lon = (int) (mLon2 * 1E6);
		GeoPoint pt2 = new GeoPoint(lat, lon);
		// ���� ��������
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.endPoint = pt2;
		BaiduMapNavigation.openWebBaiduMapNavi(para, this);
	}
}
