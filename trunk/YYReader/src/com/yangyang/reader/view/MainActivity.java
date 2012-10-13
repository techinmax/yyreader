package com.yangyang.reader.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.foxitsdk.service.FoxitDoc;
import com.foxitsdk.service.WrapPDFFunc;
import com.yangyang.reader.R;
import com.yangyang.reader.util.Constant;
import com.yangyang.reader.view.OpenFileDialog.CallbackBundle;

public class MainActivity extends Activity implements OnGestureListener {

	private ImageView imageView;
	private ImageSwitcher switcher;
	private GestureDetector detector;
	private static String TAG = "YYReaer";
	private final static int openFileDialogId = 10212739;
	public WrapPDFFunc func = null;
	private FoxitDoc myDoc;
	private int currentPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageView = (ImageView) findViewById(R.id.imageView);
		switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		detector = new GestureDetector(this);

		// openPDF();
	}

	public void openPDF(String fileName) {
		try {
			// String fileName = "/data/data/com.foxitsample.service/demo.pdf";
			// String fileName = "/mnt/sdcard/readme.pdf";
			String strFontFilePath = "/mnt/sdcard/DroidSansFallback.ttf";
			String password = "";
			int initialMemory = 5 * 1024 * 1024;
			float xScaleFactor = 1f;
			float yScaleFactor = 1f;

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
			showCurrentPage(xScaleFactor, yScaleFactor);
		} catch (Exception e) {
			/*
			 * In this demo, we decide do nothing for exceptions however, you
			 * will want to handle exceptions in some way
			 */
			postToLog(e.getMessage());
		}
	}

	private void showCurrentPage(float xScaleFactor, float yScaleFactor) {
		Display display = getWindowManager().getDefaultDisplay();
		imageView.setImageBitmap(myDoc.getPageBitmap(currentPage,
				display.getWidth(), display.getHeight(), xScaleFactor,
				yScaleFactor, 0));
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() + Constant.FLIPDISTANCE <= e2.getX() && currentPage > 0) {// 向右
			Log.i(MainActivity.class.getName(), "flip right");
			currentPage--;
			this.showCurrentPage(1, 1);
			return true;
		} else if (e1.getX() >= Constant.FLIPDISTANCE + e2.getX()
				&& currentPage + 1 < myDoc.CountPages()) {// 向左
			Log.i(MainActivity.class.getName(), "flip left");
			currentPage++;

			this.showCurrentPage(1, 1);
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
}
