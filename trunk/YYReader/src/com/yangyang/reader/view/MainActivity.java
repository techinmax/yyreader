package com.yangyang.reader.view;

import FoxitEMBSDK.EMBJavaSupport;
import FoxitEMBSDK.EMBJavaSupport.PointF;
import FoxitEMBSDK.EMBJavaSupport.RectangleF;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import com.foxitsdk.exception.memoryException;
import com.foxitsdk.service.FoxitDoc;
import com.foxitsdk.service.WrapPDFFunc;
import com.yangyang.reader.R;
import com.yangyang.reader.util.Constant;
import com.yangyang.reader.util.ZoomStatus;
import com.yangyang.reader.view.OpenFileDialog.CallbackBundle;


public class MainActivity extends Activity implements OnGestureListener,
		OnDoubleTapListener, OnTouchListener {

	private PDFView pdfView;
	private GestureDetector detector;
	private static String TAG = "YYReaer";
	private final static int openFileDialogId = 10212739;
	public WrapPDFFunc func = null;
	private FoxitDoc myDoc;
	private int currentPage;
	private ZoomStatus zoomStatus;
	private int contentTop = 0;
	boolean editMode = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		pdfView = new PDFView(this);
		setContentView(pdfView);
		detector = new GestureDetector(this);
		detector.setOnDoubleTapListener(this);
		pdfView.setOnTouchListener(this);

		//openPDF("/mnt/sdcard/FoxitForm.pdf");

	}

	public void openPDF(String fileName) {
		try {
			String strFontFilePath = "/mnt/sdcard/DroidSansFallback.ttf";
			String password = "";
			int initialMemory = 5 * 1024 * 1024;

			func = new WrapPDFFunc(this);
			Display display = getWindowManager().getDefaultDisplay();
			func.setDisplaySize(display.getWidth(), display.getHeight());
			pdfView.InitView(func);
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
			func.setDisplaySize(display.getWidth(), display.getHeight());
		}
		func.setCurPDFPageHandler(myDoc.getPageHandler(currentPage));
		if (!this.editMode){
			pdfView.setPDFBitmap(myDoc.getPageBitmap(currentPage,
					this.zoomStatus.getWidth(), this.zoomStatus.getHeight(), 0,
					0), this.zoomStatus.getDisplayWidth(), this.zoomStatus
					.getDisplayHeight());
			
		}
		else{
			func.InitPDFPage(currentPage);
			pdfView.setPDFBitmap(
					func.getPageBitmap(this.zoomStatus.getDisplayWidth(),this.zoomStatus.getDisplayHeight()),
					this.zoomStatus.getDisplayWidth(),
					this.zoomStatus.getDisplayHeight());
		}
		
		// imageView.invalidate();
		pdfView.OnDraw();
	}

	public void invalidate(float left, float top, float right, float bottom) {
		if (right - left == 0 || bottom - top == 0)
			return;
		int l, t, r, b;
		RectangleF rect = new EMBJavaSupport().new RectangleF();
		rect.left = left;
		rect.top = top;
		rect.right = right;
		rect.bottom = bottom;
		EMBJavaSupport.FPDFPagePageToDeviceRectF(func.getCurPDFPageHandler(),
				0, 0, this.zoomStatus.getDisplayWidth(),
				this.zoomStatus.getDisplayHeight(), 0, rect);
		l = (int) rect.left;
		t = (int) rect.top;
		r = (int) rect.right;
		b = (int) rect.bottom;
		Rect rc = new Rect(l, t, r, b);
		pdfView.setDirtyRect(l, t, r, b);
		pdfView.setDirtyBitmap(func.getDirtyBitmap(rc,
				this.zoomStatus.getDisplayWidth(),
				this.zoomStatus.getDisplayHeight()));
		pdfView.OnDraw();

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
						float scale = value / (baseValue * 20);// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
						if (value - baseValue < -10)
							scale = -scale;
						Log.i(MainActivity.class.getName(), "zoom image:"
								+ scale);
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
		this.openLink((int) e.getX(), (int) e.getY());
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
		case R.id.note:
			try {
				func.addAnnot(myDoc.getPageHandler(currentPage),
						EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_NOTE);
			} catch (memoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.fileattachment:
			try {
				func.addAnnot(myDoc.getPageHandler(currentPage),
						EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_FILEATTACHMENT);
			} catch (memoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.heilight:
			try {
				func.addAnnot(myDoc.getPageHandler(currentPage),
						EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_HIGHLIGHT);
			} catch (memoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.pencil:
			try {
				func.addAnnot(myDoc.getPageHandler(currentPage),
						EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_PENCIL);
			} catch (memoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.clearall:
			func.deleteAnnot();
			break;
		case R.id.stamp:
			try {
				func.addAnnot(myDoc.getPageHandler(currentPage),
						EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_STAMP);
			} catch (memoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.previous:
			if (this.currentPage > 0)
				this.currentPage--;
			break;
		case R.id.next:
			if (this.currentPage < myDoc.CountPages() - 1)
				this.currentPage++;
			break;
		case R.id.link:
			this.openLink(0, 0);
			return true;
		case R.id.edit:
			this.editMode = !this.editMode;
			item.setTitle(this.editMode ? "unlock" : "edit");
			if(!this.editMode){
				this.func.ClosePDFPage();
				//this.myDoc.closePDFPage(currentPage);
			}
			break;
		}
		this.showCurrentPage();
		return super.onMenuItemSelected(featureId, item);
	}

	private void openLink(int x, int y) {
		PointF point = new EMBJavaSupport().new PointF();
		point.x = x;
		point.y = y;
		int textPage = EMBJavaSupport.FPDFTextLoadPage(myDoc
				.getPageHandler(this.currentPage));
		EMBJavaSupport.FPDFPageDeviceToPagePointF(
				myDoc.getPageHandler(this.currentPage), 0, contentTop,
				zoomStatus.getDisplayWidth(), zoomStatus.getDisplayHeight(), 0,
				point);
		String url = EMBJavaSupport.FPDFLinkOpenOuterLink(textPage,
				(int) point.x, (int) point.y);
		Log.i("link", url);
		if (url.length() > 0) {
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(it);

		} else {
			int pageNumber = EMBJavaSupport.FPDFLinkOpenInnerLink(
					myDoc.getDocumentHandle(),
					myDoc.getPageHandler(this.currentPage), (int) point.x,
					(int) point.y);
			if (pageNumber > 0) {
				this.currentPage = pageNumber;
				this.showCurrentPage();
			}
		}
		EMBJavaSupport.FPDFTextCloseTextPage(textPage);
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
			func.ClosePDFDoc();
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
			this.zoomStatus.nextZoom(0);
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.editMode) {
			int actionType = event.getAction() & MotionEvent.ACTION_MASK;
			int actionId = event.getAction()
					& MotionEvent.ACTION_POINTER_ID_MASK;
			actionId = actionId >> 8;

			PointF point = new EMBJavaSupport().new PointF();
			point.x = event.getX();
			point.y = event.getY();
			EMBJavaSupport.FPDFPageDeviceToPagePointF(
					func.getCurPDFPageHandler(), 0, contentTop,
					zoomStatus.getDisplayWidth(),
					zoomStatus.getDisplayHeight(), 0, point);

			switch (actionType) {
			case MotionEvent.ACTION_MOVE://
				EMBJavaSupport.FPDFFormFillOnMouseMove(
						func.getPDFFormHandler(), func.getCurPDFPageHandler(),
						0, point.x, point.y);
				break;
			case MotionEvent.ACTION_DOWN: //
				EMBJavaSupport.FPDFFormFillOnMouseMove(
						func.getPDFFormHandler(), func.getCurPDFPageHandler(),
						0, point.x, point.y);
				EMBJavaSupport.FPDFFormFillOnLButtonDown(
						func.getPDFFormHandler(), func.getCurPDFPageHandler(),
						0, point.x, point.y);
				break;
			case MotionEvent.ACTION_UP: //
				EMBJavaSupport.FPDFFormFillOnLButtonUp(
						func.getPDFFormHandler(), func.getCurPDFPageHandler(),
						0, point.x, point.y);
				break;
			}
			return true;
		}
		{
		int actionType=event.getAction()&MotionEvent.ACTION_MASK;
		int actionId=event.getAction()&MotionEvent.ACTION_POINTER_ID_MASK;
		actionId=actionId>>8;  
		
		float x = event.getX();
		float y = event.getY();		
		
		switch(actionType){
		case MotionEvent.ACTION_MOVE://
			
			AddPoint(EMBJavaSupport.PSI_ACTION_MOVE, x, y, 1f, EMBJavaSupport.FXG_PT_LINETO);
			break;
		case MotionEvent.ACTION_DOWN:	//	
			
			AddPoint(EMBJavaSupport.PSI_ACTION_DOWN, x, y, 1f, EMBJavaSupport.FXG_PT_MOVETO);
			break;
		case MotionEvent.ACTION_UP:	//
			
			AddPoint(EMBJavaSupport.PSI_ACTION_UP, x, y, 1f, EMBJavaSupport.FXG_PT_LINETO | EMBJavaSupport.FXG_PT_ENDPATH);
			break;
		}
		return false;
		}
	}

	public void createAndroidTextField(String text) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("textValue", text);
		intent.setClass(this, textfieldActivity.class);
		intent.putExtra("key", bundle);
		this.startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			Bundle bundle = data.getBundleExtra("Result");
			String text = bundle.getString("ResultValue");
			Log.i("info","info:" + text);
			String result = ""
					+ EMBJavaSupport.FPDFFormFillOnSetText(
							func.getPDFFormHandler(),
							func.getCurPDFPageHandler(), text, 0);
			Log.i("handler","result:" + func.getPDFFormHandler() + "," + func.getCurPDFPageHandler());
			Log.i("result", "result:" + result);
		}
		super.onActivityResult(requestCode, resultCode, data);
		// this.showCurrentPage();
	}
	
	 public void AddPoint(int nActionType, float x, float y, float nPressures, int flag){
	    	pdfView.addAction(nActionType, x, y, nPressures, flag);
	    }
}
