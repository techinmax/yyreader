package FoxitEMBSDK;

import com.foxitsdk.exception.errorException;
import com.foxitsdk.exception.fileAccessException;
import com.foxitsdk.exception.formatException;
import com.foxitsdk.exception.invalidLicenseException;
import com.foxitsdk.exception.memoryException;
import com.foxitsdk.exception.parameterException;
import com.foxitsdk.exception.passwordException;
import com.foxitsdk.exception.statusException;
import com.foxitsdk.exception.toBeContinuedException;

public class EMBJavaSupport {

	public final static int EMBJavaSupport_RESULT_SUCCESS = 0;
	public final static int EMBJavaSupport_RESULT_MEMORY = 1;
	public final static int EMBJavaSupport_RESULT_ERROR = -1;

	public final static int EMBJavaSupport_ANNOTTYPE_UNKNOWN = 0;
	public final static int EMBJavaSupport_ANNOTTYPE_NOTE = 1;
	public final static int EMBJavaSupport_ANNOTTYPE_HIGHLIGHT = 2;
	public final static int EMBJavaSupport_ANNOTTYPE_PENCIL = 3;
	public final static int EMBJavaSupport_ANNOTTYPE_STAMP = 4;
	public final static int EMBJavaSupport_ANNOTTYPE_FILEATTACHMENT = 5;

	public final static int EMBJavaSupport_SAVEFLAG_INCREMENTAL = 1;
	public final static int EMBJavaSupport_SVAEFLAG_NOORIGINAL = 2;

	// Define interfaces of EMB SDK. Users can define them according to their
	// own requirement.
	// Users can get descriptions of these interfaces from FoxitEMBSDK Help
	// Document.
	// /////////////////
	static {
		System.loadLibrary("fpdfembedsdk");
	}

	// //////////////////
	// Document Module
	public static native int FPDFDocLoad(int fileRead, String password)
			throws parameterException, invalidLicenseException,
			fileAccessException, formatException, memoryException,
			passwordException, errorException;

	public static native void FPDFDocClose(int document)
			throws parameterException;

	public static native int FPDFDocSaveAs(int document, int flag, int pause,
			int filewrite);

	public static native int FPDFDocGetPageCount(int document)
			throws parameterException, memoryException;

	public static native int FPDFPageLoad(int document, int index)
			throws parameterException;

	public static native void FPDFPageClose(int page) throws parameterException;

	public static native int FPDFPageStartParse(int page, int text_only,
			int pauseHandler) throws toBeContinuedException,
			parameterException, statusException, memoryException;

	public static native int FPDFPageContinueParse(int page, int pauseHandler)
			throws toBeContinuedException, parameterException, statusException,
			memoryException;

	public class Rectangle {
		public int left;
		public int top;
		public int right;
		public int bottom;

	}

	public class RectangleF {
		public float left;
		public float top;
		public float right;
		public float bottom;
	}

	public class PointF {
		public float x, y;
	}

	// //////////////////
	// View Module
	public static native int FPDFRenderPageStart(int dib, int page,
			int start_x, int start_y, int size_x, int size_y, int rotate,
			int flags, Rectangle clip, int pauseHandler)
			throws toBeContinuedException, parameterException, memoryException,
			statusException, errorException;

	public static native int FPDFRenderPageContinue(int page, int pauseHandler)
			throws toBeContinuedException, parameterException, memoryException,
			statusException, errorException;

	public static native int FPDFPageDeviceToPageRectF(int page, int start_x,
			int start_y, int size_x, int size_y, int rotate, RectangleF rect);

	public static native void FPDFPagePageToDeviceRectF(int page, int start_x,
			int start_y, int size_x, int size_y, int rotate, RectangleF rect);

	// //////////////////
	// Base data Module
	public static native void FSUnlock(String sn, String code);

	public static native void FSSetFileFontmap(String filepath)
			throws parameterException;

	public static native void FSInitLibrary(int hInstance);

	public static native void FSDestroyLibrary();

	public static native void FSMemInitFixedMemory(int size)
			throws parameterException, invalidLicenseException;

	public static native void FSMemDestroyMemory();

	public static native int FSBitmapCreate(int width, int height, int format,
			byte[] buffer, int stride) throws parameterException,
			memoryException, errorException;

	public static native void FSBitmapDestroy(int dib)
			throws parameterException;

	public static native byte[] FSBitmapGetBuffer(int dib)
			throws parameterException;

	public static native void FSBitmapFillColor(int dib, int argb)
			throws parameterException;

	public static native void FSFontLoadGBCMap();

	public static native void FSFontLoadGBExtCMap();

	public static native void FSFontLoadCNSCMap();

	public static native void FSFontLoadKoreaCMap();

	public static native void FSFontLoadJapanCMap();

	public static native void FSFontLoadJapanExtCMap();

	public static native void FSLoadJbig2Decoder();

	public static native void FSLoadJpeg2000Decoder();

	public static native int FSFileReadAlloc(String filePath)
			throws memoryException;

	public static native void FSFileReadRelease(int fileRead);

	public static native int FSPauseHandlerAlloc() throws memoryException;

	public static native void FSPauseHandlerRelease(int pauseHandler);

	// //////////Annotation
	public static native int FPDFNoteInfoAlloc(String author, long color,
			int opacity, RectangleF rect, String content, int page);

	public static native void FPDFNoteInfoRelease(int nNoteInfoHandler);

	public static native int FPDFPencilInfoAlloc(String author, long color,
			int opacity, boolean busebezier, boolean boptimize, int line_width,
			int line_count);

	public static native void FPDFPencilInfoSetLineInfo(int nPencilInfo,
			int nLineInfo);

	public static native void FPDFPencilInfoRelease(int nPencilInfoHandler);

	public static native int FPDFLineInfoAlloc(int line_count);

	public static native void FPDFLineInfoSetPointInfo(int nLineInfo,
			int line_index, int nPointCounts, float[] points);

	public static native void FPDFLineInfoRelease(int nLineInfo);

	public static native int FPDFHighlightInfoAlloc(String author, long color,
			int opacity, int quad_count);

	public static native void FPDFHighlightSetQuads(int nHighlightInfo,
			int nQuads);

	public static native void FPDFHighlightInfoRelease(int nHighlightInfo);

	public static native int FPDFQuadsInfoAlloc(int quad_count, float[] quads);

	public static native void FPDFQuadsInfoRelease(int nQuads);

	public static native int FPDFStampInfoAlloc(String author, long color,
			int opacity, RectangleF pdfRect, String subject, String image_path);

	public static native void FPDFStampInfoRelease(int nStampInfo);

	public static native int FPDFFileAttachmentInfoAlloc(String author,
			long color, int opacity, RectangleF pdfRect, String filepath);

	public static native void FPDFFileAttachmentInfoRelease(
			int nFileAttachmentInfo);

	public static native int FPDFAnnotAdd(int page, int annot_type, int data);

	public static native int FPDFAnnotDelete(int page, int index);

	public static native int FPDFAnnotGetCount(int page);

	public static native int FPDFAnnotGetIndexAtPos(int page, int x, int y);

	public static native int FSFileWriteAlloc(String filePath)
			throws memoryException;

	public static native void FSFileWriteRelease(int filewrite);

	public static native int FPDFTextLoadPage(int page);

	public static native void FPDFTextCloseTextPage(int textPage);

	public static native int FPDFTextCountRects(int textPage, int start,
			int count);

	public static native RectangleF FPDFTextGetRect(int textpage, int index);

	public static native float FPDFPageGetSizeX(int pageHandle)
			throws parameterException;

	public static native float FPDFPageGetSizeY(int pageHandle)
			throws parameterException;

	public static native String FPDFLinkOpenOuterLink(int textpage, int x, int y);

	public static native int FPDFLinkOpenInnerLink(int documentHandle,
			int pageHandle, int x, int y);

	public static native void FPDFPageDeviceToPagePointF(int pageHandler,
			int x, int y, int width, int hegith, int rotate, PointF point);

}