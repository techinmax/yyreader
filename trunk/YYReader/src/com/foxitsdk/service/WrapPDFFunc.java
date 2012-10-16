package com.foxitsdk.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import FoxitEMBSDK.EMBJavaSupport;
import FoxitEMBSDK.EMBJavaSupport.RectangleF;
import android.graphics.Bitmap;

import com.foxitsdk.exception.invalidLicenseException;
import com.foxitsdk.exception.memoryException;
import com.foxitsdk.exception.parameterException;

/**
 * defined for a wrap for All PDF implements��
 * 
 * @author Foxit
 * 
 */

public class WrapPDFFunc {
	/** state variables */
	private static final String TAG = "WrapPDFFunc";
	private static final String fileName = "/mnt/sdcard/FoxitText.pdf";
	private static final String password = "";
	private int nFileRead = 0;
	private int nPDFDocHandler = 0;
	private int nPDFCurPageHandler = 0;
	private int nDisplayX = 0;
	private int nDisplayY = 0;
	private List<Integer> arrIndexList = null;

	/** */
	public WrapPDFFunc(int x, int y) {
		nDisplayX = x;
		nDisplayY = y;
		arrIndexList = new ArrayList<Integer>();
	}

	public WrapPDFFunc() {
	}

	public boolean InitFoxitFixedMemory(int initMemSize)
			throws parameterException, invalidLicenseException {
		EMBJavaSupport.FSMemInitFixedMemory(initMemSize);
		EMBJavaSupport.FSInitLibrary(0);
		EMBJavaSupport.FSUnlock("SDKEDTEMP",
				"3C86F25880658927118E766271BEB68454E49DFD");
		return true;
	}

	public FoxitDoc createFoxitDoc(String fileName, String password) {
		return new FoxitDoc(fileName, password);
	}

	public void SetFontFileMap(String strFontFilePath)
			throws parameterException {
		EMBJavaSupport.FSSetFileFontmap(strFontFilePath);
	}

	/** Init EMB SDK */
	public boolean InitFoxitFixedMemory() throws parameterException,
			invalidLicenseException {
		EMBJavaSupport.FSMemInitFixedMemory(5 * 1024 * 1024);
		EMBJavaSupport.FSInitLibrary(0);
		EMBJavaSupport.FSUnlock("SDKEDTEMP",
				"3C86F25880658927118E766271BEB68454E49DFD");

		return true;
	}

	/** Destroy EMB SDK */
	public int DestroyFoxitFixedMemory() {

		EMBJavaSupport.FSDestroyLibrary();

		EMBJavaSupport.FSMemDestroyMemory();

		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	/** Load jbig2 decoder. */
	public void LoadJbig2Decoder() {
		EMBJavaSupport.FSLoadJbig2Decoder();
	}

	/** Load jpeg2000 decoder. */
	public void LoadJpeg2000Decoder() {
		EMBJavaSupport.FSLoadJpeg2000Decoder();
	}

	/** */
	public void LoadJapanFontCMap() {
		EMBJavaSupport.FSFontLoadJapanCMap();
		EMBJavaSupport.FSFontLoadJapanExtCMap();
	}

	/** */
	public void LoadCNSFontCMap() {
		EMBJavaSupport.FSFontLoadGBCMap();
		EMBJavaSupport.FSFontLoadGBExtCMap();
		EMBJavaSupport.FSFontLoadCNSCMap();
	}

	/** */
	public void LoadKoreaFontCMap() {
		EMBJavaSupport.FSFontLoadKoreaCMap();
	}

	/** Load PDF Document. */
	public int InitPDFDoc() {

		/** Init a FS_FileRead structure */
		try {
			nFileRead = EMBJavaSupport.FSFileReadAlloc(fileName);
			nPDFDocHandler = EMBJavaSupport.FPDFDocLoad(nFileRead, password);
			if (nPDFDocHandler == 0) {
				return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	/** Close PDF Document. */
	public int ClosePDFDoc() {

		if (nPDFDocHandler == 0) {
			return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
		}

		try {
			EMBJavaSupport.FPDFDocClose(nPDFDocHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		nPDFDocHandler = 0;

		EMBJavaSupport.FSFileReadRelease(nFileRead);
		nFileRead = 0;

		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	/** Load and parser a PDF page. */
	public int InitPDFPage(int nPageIndex) {

		if (nPDFDocHandler == 0) {
			return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
		}

		try {
			nPDFCurPageHandler = EMBJavaSupport.FPDFPageLoad(nPDFDocHandler,
					nPageIndex);

			if (nPDFCurPageHandler == 0) {
				return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
			}

			EMBJavaSupport.FPDFPageStartParse(nPDFCurPageHandler, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	/** Close a PDF Page. */
	public int ClosePDFPage() {

		if (nPDFCurPageHandler == 0) {
			return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
		}

		try {
			EMBJavaSupport.FPDFPageClose(nPDFCurPageHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		nPDFCurPageHandler = 0;

		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	/** Count PDF page. */
	public int GetPageCounts() {

		if (nPDFDocHandler == 0) {
			return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
		}

		int nPageCount = 0;
		try {
			nPageCount = EMBJavaSupport.FPDFDocGetPageCount(nPDFDocHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nPageCount;
	}

	/** Render pdf to bitmap. */
	public Bitmap getPageBitmap(int displayWidth, int displayHeight) {
		if (nPDFCurPageHandler == 0) {
			return null;
		}

		Bitmap bm;
		bm = Bitmap.createBitmap(displayWidth, displayHeight,
				Bitmap.Config.ARGB_8888);

		int dib;
		try {
			dib = EMBJavaSupport.FSBitmapCreate(displayWidth, displayHeight, 7,
					null, 0);

			EMBJavaSupport.FSBitmapFillColor(dib, 0xff);
			EMBJavaSupport.FPDFRenderPageStart(dib, nPDFCurPageHandler, 0, 0,
					displayWidth, displayHeight, 0, 1, null, 0);

			byte[] bmpbuf = EMBJavaSupport.FSBitmapGetBuffer(dib);

			ByteBuffer bmBuffer = ByteBuffer.wrap(bmpbuf);
			bm.copyPixelsFromBuffer(bmBuffer);

			EMBJavaSupport.FSBitmapDestroy(dib);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bm;
	}

	public int addAnnot(int nAnnotType) throws memoryException {

		switch (nAnnotType) {

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_UNKNOWN: {
			return EMBJavaSupport.EMBJavaSupport_RESULT_ERROR;
		}

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_NOTE: {
			RectangleF pdfRect = new EMBJavaSupport().new RectangleF();
			pdfRect.left = 10;
			pdfRect.top = 220;
			pdfRect.right = 30;
			pdfRect.bottom = 200;
			int nCount = EMBJavaSupport.FPDFAnnotGetCount(nPDFCurPageHandler);
			int nNoteInfoItem = EMBJavaSupport.FPDFNoteInfoAlloc("James",
					0x0000ff, 80, pdfRect, "I like note", nPDFCurPageHandler);
			nCount = EMBJavaSupport.FPDFAnnotGetCount(nPDFCurPageHandler);
			int nIndex = EMBJavaSupport
					.FPDFAnnotAdd(nPDFCurPageHandler,
							EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_NOTE,
							nNoteInfoItem);
			arrIndexList.add(nIndex);
			EMBJavaSupport.FPDFNoteInfoRelease(nNoteInfoItem);

			/** device space convert to pdf space. */
			RectangleF deviceRect = new EMBJavaSupport().new RectangleF();
			deviceRect.left = 10;
			deviceRect.top = 100;
			deviceRect.right = 30;
			deviceRect.bottom = 120;
			int nRet = EMBJavaSupport.FPDFPageDeviceToPageRectF(
					nPDFCurPageHandler, 0, 0, nDisplayX, nDisplayY, 0,
					deviceRect);
			deviceRect.right = deviceRect.left + 20;
			deviceRect.bottom = deviceRect.top - 20;
			nNoteInfoItem = EMBJavaSupport.FPDFNoteInfoAlloc("James", 0xff00ff,
					80, deviceRect, "I like note too", nPDFCurPageHandler);
			nIndex = EMBJavaSupport
					.FPDFAnnotAdd(nPDFCurPageHandler,
							EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_NOTE,
							nNoteInfoItem);
			arrIndexList.add(nIndex);
			EMBJavaSupport.FPDFNoteInfoRelease(nNoteInfoItem);

			break;
		}

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_HIGHLIGHT: {

			int TextPage = EMBJavaSupport.FPDFTextLoadPage(nPDFCurPageHandler);
			int nRectNum = EMBJavaSupport.FPDFTextCountRects(TextPage, 0, 5);
			RectangleF rect = EMBJavaSupport.FPDFTextGetRect(TextPage, 0);
			EMBJavaSupport.FPDFTextCloseTextPage(TextPage);
			int nQuadsCount = 1;
			int nHighlightInfo = EMBJavaSupport.FPDFHighlightInfoAlloc("James",
					0x00ff00, 80, nQuadsCount);
			float[] quads = { rect.left, rect.top, rect.right, rect.top,
					rect.left, rect.bottom, rect.right, rect.bottom };
			int nQuadsInfo = EMBJavaSupport.FPDFQuadsInfoAlloc(nQuadsCount,
					quads);
			EMBJavaSupport.FPDFHighlightSetQuads(nHighlightInfo, nQuadsInfo);

			int nIndex = EMBJavaSupport.FPDFAnnotAdd(nPDFCurPageHandler,
					EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_HIGHLIGHT,
					nHighlightInfo);
			arrIndexList.add(nIndex);

			EMBJavaSupport.FPDFHighlightInfoRelease(nHighlightInfo);
			EMBJavaSupport.FPDFQuadsInfoRelease(nQuadsInfo);

			break;
		}

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_PENCIL: {
			int line_count = 2;
			int nPencilInfoItem = EMBJavaSupport.FPDFPencilInfoAlloc("James",
					0xff0000, 80, true, true, 5, line_count);
			int nLineInfo = EMBJavaSupport.FPDFLineInfoAlloc(line_count);
			float[] points1 = { 300f, 100f, 400f, 200f };
			float[] points2 = { 400f, 200f, 500f, 200f, 400f, 100f };
			EMBJavaSupport.FPDFLineInfoSetPointInfo(nLineInfo, 0, 2, points1);
			EMBJavaSupport.FPDFLineInfoSetPointInfo(nLineInfo, 1, 3, points2);
			EMBJavaSupport
					.FPDFPencilInfoSetLineInfo(nPencilInfoItem, nLineInfo);

			int nIndex = EMBJavaSupport.FPDFAnnotAdd(nPDFCurPageHandler,
					EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_PENCIL,
					nPencilInfoItem);
			arrIndexList.add(nIndex);

			EMBJavaSupport.FPDFLineInfoRelease(nLineInfo);
			EMBJavaSupport.FPDFPencilInfoRelease(nPencilInfoItem);

			break;
		}

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_STAMP: {
			RectangleF rect = new EMBJavaSupport().new RectangleF();
			rect.left = 137.97f;
			rect.top = 542.73f;
			rect.right = 417.97f;
			rect.bottom = 322.73f;
			String path = "/mnt/sdcard/FoxitLog.jpg";
			int nStampInfo = EMBJavaSupport.FPDFStampInfoAlloc("James",
					0xffff00, 80, rect, "Stamp_Test", path);
			int nIndex = EMBJavaSupport.FPDFAnnotAdd(nPDFCurPageHandler,
					EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_STAMP, nStampInfo);
			arrIndexList.add(nIndex);

			EMBJavaSupport.FPDFStampInfoRelease(nStampInfo);
			break;
		}

		case EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_FILEATTACHMENT: {
			RectangleF rect = new EMBJavaSupport().new RectangleF();
			rect.left = 24.31f;
			rect.top = 623.53f;
			rect.right = 246.97f;
			rect.bottom = 522.76f;
			String filepath = "/mnt/sdcard/FoxitForm.pdf";
			int nFileAttachmentInfo = EMBJavaSupport
					.FPDFFileAttachmentInfoAlloc("James", 0x0000ff, 80, rect,
							filepath);
			int nIndex = EMBJavaSupport.FPDFAnnotAdd(nPDFCurPageHandler,
					EMBJavaSupport.EMBJavaSupport_ANNOTTYPE_FILEATTACHMENT,
					nFileAttachmentInfo);
			arrIndexList.add(nIndex);

			EMBJavaSupport.FPDFFileAttachmentInfoRelease(nFileAttachmentInfo);
			break;
		}

		default:
			break;
		}
		int filewrite = EMBJavaSupport
				.FSFileWriteAlloc("/data/data/com.foxitsample.annotations/FoxitSaveAnnotation.pdf");
		EMBJavaSupport.FPDFDocSaveAs(nPDFDocHandler,
				EMBJavaSupport.EMBJavaSupport_SAVEFLAG_INCREMENTAL, 0,
				filewrite);
		EMBJavaSupport.FSFileWriteRelease(filewrite);
		return EMBJavaSupport.EMBJavaSupport_RESULT_SUCCESS;
	}

	public int deleteFileAttachment(int x, int y) {
		int annot_index = EMBJavaSupport.FPDFAnnotGetIndexAtPos(
				nPDFCurPageHandler, x, y);
		int nRet = EMBJavaSupport.FPDFAnnotDelete(nPDFCurPageHandler,
				annot_index);

		return nRet;
	}

	public int deleteAnnot() {
		int nSize = arrIndexList.size();
		if (nSize == 0) {
			return 0;
		}
		int annot_index = arrIndexList.get(nSize - 1);
		int nRet = EMBJavaSupport.FPDFAnnotDelete(nPDFCurPageHandler,
				annot_index);
		arrIndexList.remove(nSize - 1);
		return nRet;
	}

}