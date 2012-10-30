package com.foxitsdk.service;

import java.nio.ByteBuffer;

import FoxitEMBSDK.EMBJavaSupport;
import FoxitEMBSDK.EMBJavaSupport.Rectangle;
import android.graphics.Bitmap;
import android.util.Log;

import com.foxitsdk.exception.memoryException;
import com.foxitsdk.exception.parameterException;

/*FoxitDoc.java
 * */
public class FoxitDoc {

	/* state variables */
	private boolean cleanUpFlag = true;
	private int fileAccessHandle = 0;
	private int docHandle = 0;
	protected int[] pageHandles;
	protected int pageCount = 0;
	private static String TAG = "FoxitDoc";
	private static int instanceCounter = 0;
	private static int nZoomFlag;

	// precondition: memory initialization has already occurred
	public FoxitDoc(String filePath, String password) {
		try {
			fileAccessHandle = EMBJavaSupport.FSFileReadAlloc(filePath);
			docHandle = EMBJavaSupport.FPDFDocLoad(fileAccessHandle, password);
		} catch (memoryException e) {
			EMBJavaSupport.FSFileReadRelease(fileAccessHandle);
			docHandle = 0;
			fileAccessHandle = 0;
			postToLog(e.getMessage());
			return;
		} catch (Exception e) {
			postToLog(e.getMessage());
			return;
		}
		instanceCounter++;
	}

	public int getDocumentHandle() {
		return this.docHandle;
	}

	/* helper functions */
	protected void InitPage(int pageIndex) {
		try {
			pageHandles[pageIndex] = EMBJavaSupport.FPDFPageLoad(docHandle,
					pageIndex);
			EMBJavaSupport.FPDFPageStartParse(pageHandles[pageIndex], 0, 0);
		} catch (memoryException e) {
			postToLog(e.getMessage());
		} catch (Exception e) {
			postToLog(e.getMessage());
		}
	}

	public int getPageHandler(int pageIndex) {
		return this.pageHandles[pageIndex];
	}

	// make sure unmanaged resources are cleaned up
	protected void finalize() {
		if (cleanUpFlag) {
			close();
			postToLog("FoxitDoc not cleaned up properly.");
		}
	}

	private void RenderPage(int pageIndex, Bitmap bm, int startX, int startY,
			float xScale, float yScale, int rotate, int flags, Rectangle rect,
			int pauseHandler) {
		try {
			int dib = EMBJavaSupport.FSBitmapCreate(bm.getWidth(),
					bm.getHeight(), 7, null, 0);
			EMBJavaSupport.FSBitmapFillColor(dib, 0xff);
			float scaledWidth = bm.getWidth() * xScale;
			float scaledHeight = bm.getHeight() * yScale;
			EMBJavaSupport.FPDFRenderPageStart(dib, pageHandles[pageIndex],
					startX, startY, (int) scaledWidth, (int) scaledHeight,
					rotate, flags, rect, pauseHandler);
			byte[] bmpbuf = EMBJavaSupport.FSBitmapGetBuffer(dib);

			ByteBuffer bmBuffer = ByteBuffer.wrap(bmpbuf);
			bm.copyPixelsFromBuffer(bmBuffer);

		} catch (memoryException e) {
			postToLog(e.getMessage());
		} catch (Exception e) {
			postToLog(e.getMessage());
		}
	}

	private void RenderPage(int pageIndex, Bitmap bm, int startX, int startY,
			int rotate, int flags, Rectangle rect, int pauseHandler) {
		try {
			int dib = EMBJavaSupport.FSBitmapCreate(bm.getWidth(),
					bm.getHeight(), 7, null, 0);
			EMBJavaSupport.FSBitmapFillColor(dib, 0xff);
			float scaledWidth = bm.getWidth();
			float scaledHeight = bm.getHeight();
			EMBJavaSupport.FPDFRenderPageStart(dib, pageHandles[pageIndex],
					startX, startY, (int) scaledWidth, (int) scaledHeight,
					rotate, flags, rect, pauseHandler);
			byte[] bmpbuf = EMBJavaSupport.FSBitmapGetBuffer(dib);

			ByteBuffer bmBuffer = ByteBuffer.wrap(bmpbuf);
			bm.copyPixelsFromBuffer(bmBuffer);

		} catch (memoryException e) {
			postToLog(e.getMessage());
		} catch (Exception e) {
			postToLog(e.getMessage());
		}
	}

	protected void postToLog(String msg) {
		Log.v(TAG, msg);
	}

	/* user functions */

	// clean up unmanaged resources
	public void close() {
		for (int i = 0; i < pageHandles.length; i++) {
			if (pageHandles[i] > 0) {// if page handle exist for that page
				try {
					EMBJavaSupport.FPDFPageClose(pageHandles[i]);
				} catch (parameterException e) {
					// Not handling parameter exception for now
				}
			}
		}

		if (fileAccessHandle != 0) {
			EMBJavaSupport.FSFileReadRelease(fileAccessHandle);
		}
		cleanUpFlag = false;
		instanceCounter--;
	}

	public boolean isValid() {
		return fileAccessHandle != 0 && docHandle != 0;
	}

	public int CountPages() {
		if (pageCount != 0)
			return pageCount;
		if (docHandle != 0) {
			try {
				pageCount = EMBJavaSupport.FPDFDocGetPageCount(docHandle);
				pageHandles = new int[pageCount];
				return pageCount;
			} catch (Exception e) {
				postToLog(e.getMessage());
			}
		}
		return 0;
	}

	public float GetPageSizeX(int pageIndex) {
		if (pageHandles[pageIndex] == 0) {
			this.InitPage(pageIndex);
		}

		try {
			return EMBJavaSupport.FPDFPageGetSizeX(pageHandles[pageIndex]);
		} catch (parameterException e) {
			postToLog(e.getMessage());
			return 0;
		}
	}

	public float GetPageSizeY(int pageIndex) {
		if (pageHandles[pageIndex] == 0) {
			this.InitPage(pageIndex);
		}

		try {
			return EMBJavaSupport.FPDFPageGetSizeY(pageHandles[pageIndex]);
		} catch (parameterException e) {
			postToLog(e.getMessage());
			return 0;
		}
	}

	public Bitmap getPageBitmap(int pageIndex, int displayWidth,
			int displayHeight, float xScaleFactor, float yScaleFactor,
			int rotateFlag) {
		if (pageHandles[pageIndex] == 0) {
			this.InitPage(pageIndex);
		}

		Bitmap bm;
		bm = Bitmap.createBitmap(displayWidth, displayHeight,
				Bitmap.Config.ARGB_8888);
		this.RenderPage(pageIndex, bm, 0, 0, xScaleFactor, yScaleFactor,
				rotateFlag, 0, null, 0);
		return bm;
	}

	public Bitmap getPageBitmap(int pageIndex, float displayWidth,
			float displayHeight, int rotateFlag, int ZoomFlag) {
		if (pageHandles[pageIndex] == 0) {
			this.InitPage(pageIndex);
		}

		Bitmap bm;
		bm = Bitmap.createBitmap((int) displayWidth, (int) displayHeight,
				Bitmap.Config.ARGB_8888);
		this.RenderPage(pageIndex, bm, 0, 0, rotateFlag, 1, null, 0);
		nZoomFlag = ZoomFlag;
		return bm;
	}
}