package com.yangyang.reader.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageSwitcher;

import com.foxitsdk.service.FoxitDoc;
import com.foxitsdk.service.WrapPDFFunc;
import com.yangyang.reader.R;
import com.yangyang.reader.util.Constant;
import com.yangyang.reader.util.ZoomStatus;
import com.yangyang.reader.view.OpenFileDialog.CallbackBundle;

public class MainActivity extends Activity implements OnGestureListener,
		OnDoubleTapListener {

	private PDFView imageView;
	private ImageSwitcher switcher;
	private GestureDetector detector;
	private static String TAG = "YYReaer";
	private final static int openFileDialogId = 10212739;
	public WrapPDFFunc func = null;
	private FoxitDoc myDoc;
	private int currentPage;
	private ZoomStatus zoomStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = new PDFView(getApplicationContext());
		// imageView = (ImageView) findViewById(R.id.imageView);
		switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		detector = new GestureDetector(this);
		detector.setOnDoubleTapListener(this);
		switcher.addView(imageView);
		switcher.setBackgroundColor(0);
		/**
		 * super.onCreate(savedInstanceState); imageView = new
		 * PDFView(getApplicationContext());
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 * this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		 * WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		 * setContentView(imageView); detector = new GestureDetector(this);
		 * detector.setOnDoubleTapListener(this);
		 */

		// openPDF();
	}

	public void openPDF(String fileName) {
		try {
			// String fileName = "/data/data/com.foxitsample.service/demo.pdf";
			// String fileName = "/mnt/sdcard/readme.pdf";
			String strFontFilePath = "/mnt/sdcard/DroidSansFallback.ttf";
			String password = "";
			int initialMemory = 5 * 1024 * 1024;

			func = new WrapPDFFunc();
			func.InitFoxitFixedMemory(initialMemory);
			func.LoadJbig2Decoder();
			func.LoadJpeg2000Decoder();
			func.LoadCNSFontCMap();
			func.LoadKoreaFontCMap();
			func.LoadJapanFontCMap();
			func.SetFontFileMap(strFontFilePath);

			myDoc = func.createFoxitDoc(fileName, password);
			myDoc.CountPages();
			showCurrentPage();
		} catch (Exception e) {
			/*
			 * In this demo, we decide do nothing for exceptions however, you
			 * will want to handle exceptions in some way
			 */
			postToLog(e.getMessage());
		}
	}

	private void showCurrentPage() {
		if (this.zoomStatus == null) {
			Display display = getWindowManager().getDefaultDisplay();
			this.zoomStatus = new ZoomStatus(
					(int) myDoc.GetPageSizeX(currentPage),
					(int) myDoc.GetPageSizeY(currentPage), display.getWidth(),
					display.getHeight());
		}

		imageView.setBitmap(myDoc.getPageBitmap(currentPage,
				this.zoomStatus.getWidth(), this.zoomStatus.getHeight(), 0, 0));
		imageView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	float baseValue, last_x, last_y;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// return ArtFilterActivity.this.mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			baseValue = 0;
			last_x = event.getRawX();
			last_y = event.getRawY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (event.getPointerCount() == 2) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
				if (baseValue == 0) {
					baseValue = value;
				} else {
					if (value - baseValue >= 10 || value - baseValue <= -10) {
						float scale = value / (baseValue * 10);// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
						if (value - baseValue < -10)
							scale = -scale;
						Log.i(MainActivity.class.getName(), "zoom image");
						zoomStatus.nextZoom(scale);
						baseValue = 0;
						showCurrentPage();
						return true;
					}
				}
				return true;
			} else if (event.getPointerCount() == 1) {
				float x = event.getRawX();
				float y = event.getRawY();
				x -= last_x;
				y -= last_y;
				if (x >= 10 || y >= 10 || x <= -10 || y <= -10)
					// img_transport(x, y); // 移动图片位置
					last_x = event.getRawX();
				last_y = event.getRawY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

		}
		return detector.onTouchEvent(event);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() + Constant.FLIPDISTANCE <= e2.getX()
				&& Math.abs(e1.getY() - e2.getY()) < Constant.FLIPDISTANCE / 1.3
				&& currentPage > 0) {// 向右
			Log.i(MainActivity.class.getName(), "flip right");
			currentPage--;
			this.showCurrentPage();
			return true;
		} else if (e1.getX() >= Constant.FLIPDISTANCE + e2.getX()
				&& Math.abs(e1.getY() - e2.getY()) < Constant.FLIPDISTANCE / 1.3
				&& currentPage + 1 < myDoc.CountPages()) {// 向左
			Log.i(MainActivity.class.getName(), "flip left");
			currentPage++;

			this.showCurrentPage();
			return true;
		} else
			return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub

		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.openfile:
			showDialog(openFileDialogId);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == openFileDialogId) {
			return OpenFileDialog.createDialog(openFileDialogId, this,
					"Open PDF", new CallbackBundle() {
						@Override
						public void callback(Bundle bundle) {
							String filepath = bundle.getString("path");
							setTitle(filepath); // 把文件路径显示在标题上
							MainActivity.this.openPDF(filepath);
						}
					}, ".pdf;");
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onDestroy() {
		try {
			myDoc.close();
			func.DestroyFoxitFixedMemory();
		} catch (Exception e) {
			System.exit(0);
		}

		super.onDestroy();
	}

	private void postToLog(String msg) {
		Log.v(TAG, msg);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		if (this.zoomStatus != null) {
			this.zoomStatus.nextZoom(-1);
			this.showCurrentPage();
			return true;
		}
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
