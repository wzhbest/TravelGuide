package baidumapsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Color;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.travel.guide.R;

/**
 * 此demo用来展示如何在地图上用GraphicsOverlay添加点、线、多边形、圆
 * 同时展示如何在地图上用TextOverlay添加文字
 *
 */
public class GeometryDemo extends Activity {

	//地图相关
	MapView mMapView = null;
	
	//UI相关
	Button resetBtn = null;
	Button clearBtn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	/**
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.activity_geometry);
        CharSequence titleLable="自定义绘制功能";
        setTitle(titleLable);
        
        //初始化地图
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().setZoom(12.5f);
        mMapView.getController().enableClick(true);
        
        //UI初始化
        clearBtn = (Button)findViewById(R.id.button1);
        resetBtn = (Button)findViewById(R.id.button2);
        
        OnClickListener clearListener = new OnClickListener(){
            public void onClick(View v){
                clearClick();
            }
        };
        OnClickListener restListener = new OnClickListener(){
            public void onClick(View v){
                resetClick();
            }
        };
        
       clearBtn.setOnClickListener(clearListener);
       resetBtn.setOnClickListener(restListener);
       
       //界面加载时添加绘制图层
       addCustomElementsDemo();
    }
    

    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo(){
    	GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
        mMapView.getOverlays().add(graphicsOverlay);
    	//添加点
        graphicsOverlay.setData(drawPoint());
    	//添加折线
        graphicsOverlay.setData(drawLine());
        //添加弧线
        graphicsOverlay.setData(drawArc());
    	//添加多边形
        graphicsOverlay.setData(drawPolygon());
    	//添加圆
        graphicsOverlay.setData(drawCircle());
    	//绘制文字
        TextOverlay textOverlay = new TextOverlay(mMapView);
        mMapView.getOverlays().add(textOverlay);
        textOverlay.addText(drawText());
        //执行地图刷新使生效
        mMapView.refresh();
    }
    
    public void resetClick(){
    	//添加绘制元素
    	addCustomElementsDemo();
    }
   
    public void clearClick(){
    	//清除所有图层
    	mMapView.getOverlays().clear();
    	mMapView.refresh();
    }
    /**
     * 绘制折线，该折线状态随地图状态变化
     * @return 折线对象
     */
    public Graphic drawLine(){
    	double mLat = 39.97923;
       	double mLon = 116.357428;
       	
    	int lat = (int) (mLat*1E6);
	   	int lon = (int) (mLon*1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
	   
	   	mLat = 39.94923;
       	mLon = 116.397428;
    	lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	   	GeoPoint pt2 = new GeoPoint(lat, lon);
	   	mLat = 39.97923;
       	mLon = 116.437428;
		lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	    GeoPoint pt3 = new GeoPoint(lat, lon);
	  
	    //构建线
  		Geometry lineGeometry = new Geometry();
  		//设定折线点坐标
  		GeoPoint[] linePoints = new GeoPoint[3];
  		linePoints[0] = pt1;
  		linePoints[1] = pt2;
  		linePoints[2] = pt3; 
  		lineGeometry.setPolyLine(linePoints);
  		//设定样式
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//生成Graphic对象
  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
  		return lineGraphic;
    }

	/**
	 * 绘制弧线
	 * 
	 * @return 折线对象
	 */
	public Graphic drawArc() {
		double mLat = 39.97923;
		double mLon = 116.357428;

		int lat = (int) (mLat * 1E6);
		int lon = (int) (mLon * 1E6);
		GeoPoint pt1 = new GeoPoint(lat, lon);

		mLat = 39.94923;
		mLon = 116.397428;
		lat = (int) (mLat * 1E6);
		lon = (int) (mLon * 1E6);
		GeoPoint pt2 = new GeoPoint(lat, lon);
		mLat = 39.97923;
		mLon = 116.437428;
		lat = (int) (mLat * 1E6);
		lon = (int) (mLon * 1E6);
		GeoPoint pt3 = new GeoPoint(lat, lon);

		Geometry arcGeometry = new Geometry();

		arcGeometry.setArc(pt1, pt3, pt2);
		// 设定样式
		Symbol arcSymbol = new Symbol();
		Symbol.Color arcColor = arcSymbol.new Color();
		arcColor.red = 255;
		arcColor.green = 0;
		arcColor.blue = 225;
		arcColor.alpha = 255;
		arcSymbol.setLineSymbol(arcColor, 4);
		// 生成Graphic对象
		Graphic arcGraphic = new Graphic(arcGeometry, arcSymbol);
		return arcGraphic;
	}

   /**
    * 绘制多边形，该多边形随地图状态变化
    * @return 多边形对象
    */
    public Graphic drawPolygon(){
    	double mLat = 39.93923;
       	double mLon = 116.357428;
    	int lat = (int) (mLat*1E6);
	   	int lon = (int) (mLon*1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
	   	mLat = 39.91923;
       	mLon = 116.327428;
		lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	    GeoPoint pt2 = new GeoPoint(lat, lon);
	    mLat = 39.89923;
       	mLon = 116.347428;
		lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	    GeoPoint pt3 = new GeoPoint(lat, lon);
	    mLat = 39.89923;
       	mLon = 116.367428;
		lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	    GeoPoint pt4 = new GeoPoint(lat, lon);
	    mLat = 39.91923;
       	mLon = 116.387428;
		lat = (int) (mLat*1E6);
	   	lon = (int) (mLon*1E6);
	    GeoPoint pt5 = new GeoPoint(lat, lon);
	    
	    //构建多边形
  		Geometry polygonGeometry = new Geometry();
  		//设置多边形坐标
  		GeoPoint[] polygonPoints = new GeoPoint[5];
  		polygonPoints[0] = pt1;
  		polygonPoints[1] = pt2;
  		polygonPoints[2] = pt3; 
  		polygonPoints[3] = pt4; 
  		polygonPoints[4] = pt5; 
  		polygonGeometry.setPolygon(polygonPoints);
  		//设置多边形样式
  		Symbol polygonSymbol = new Symbol();
 		Symbol.Color polygonColor = polygonSymbol.new Color();
 		polygonColor.red = 0;
 		polygonColor.green = 0;
 		polygonColor.blue = 255;
 		polygonColor.alpha = 126;
 		polygonSymbol.setSurface(polygonColor,1,5);
  		//生成Graphic对象
  		Graphic polygonGraphic = new Graphic(polygonGeometry, polygonSymbol);
  		return polygonGraphic;
    }
    /**
     * 绘制单点，该点状态不随地图状态变化而变化
     * @return 点对象
     */
    public Graphic drawPoint(){
       	double mLat = 39.98923;
       	double mLon = 116.397428;
    	int lat = (int) (mLat*1E6);
	   	int lon = (int) (mLon*1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
	   	
	   	//构建点
  		Geometry pointGeometry = new Geometry();
  		//设置坐标
  		pointGeometry.setPoint(pt1, 10);
  		//设定样式
  		Symbol pointSymbol = new Symbol();
 		Symbol.Color pointColor = pointSymbol.new Color();
 		pointColor.red = 0;
 		pointColor.green = 126;
 		pointColor.blue = 255;
 		pointColor.alpha = 255;
 		pointSymbol.setPointSymbol(pointColor);
  		//生成Graphic对象
  		Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
  		return pointGraphic;
    }
    /**
     * 绘制圆，该圆随地图状态变化
     * @return 圆对象
     */
    public Graphic drawCircle() {
    	double mLat = 39.90923; 
       	double mLon = 116.447428; 
    	int lat = (int) (mLat*1E6);
	   	int lon = (int) (mLon*1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
	   	
	   	//构建圆
  		Geometry circleGeometry = new Geometry();
  	
  		//设置圆中心点坐标和半径
  		circleGeometry.setCircle(pt1, 2500);
  		//设置样式
  		Symbol circleSymbol = new Symbol();
 		Symbol.Color circleColor = circleSymbol.new Color();
 		circleColor.red = 0;
 		circleColor.green = 255;
 		circleColor.blue = 0;
 		circleColor.alpha = 126;
  		circleSymbol.setSurface(circleColor,1,3, new Stroke(3, circleSymbol.new Color(0xFFFF0000)));
  		//生成Graphic对象
  		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
  		return circleGraphic;
   }
    /**
     * 绘制文字，该文字随地图变化有透视效果
     * @return 文字对象
     */
    public TextItem drawText(){
       	double mLat = 39.86923;
       	double mLon = 116.397428;
    	int lat = (int) (mLat*1E6);
	   	int lon = (int) (mLon*1E6);   	
	   	//构建文字
	   	TextItem item = new TextItem();
    	//设置文字位置
    	item.pt = new GeoPoint(lat,lon);
    	//设置文件内容
    	item.text = "百度地图SDK";
    	//设文字大小
    	item.fontSize = 40;
    	Symbol symbol = new Symbol();
    	Symbol.Color bgColor = symbol.new Color();
    	//设置文字背景色
    	bgColor.red = 0;
    	bgColor.blue = 0;
    	bgColor.green = 255;
    	bgColor.alpha = 50;
    	
    	Symbol.Color fontColor = symbol.new Color();
    	//设置文字着色
    	fontColor.alpha = 255;
    	fontColor.red = 0;
    	fontColor.green = 0;
    	fontColor.blue  = 255;
    	//设置对齐方式
    	item.align = TextItem.ALIGN_CENTER;
    	//设置文字颜色和背景颜色
    	item.fontColor = fontColor;
    	item.bgColor  = bgColor ; 
    	return item;
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
    protected void onDestroy() {
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


