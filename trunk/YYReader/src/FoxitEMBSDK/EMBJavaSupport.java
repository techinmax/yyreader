package FoxitEMBSDK;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.foxitsdk.exception.errorException;
import com.foxitsdk.exception.fileAccessException;
import com.foxitsdk.exception.formatException;
import com.foxitsdk.exception.invalidLicenseException;
import com.foxitsdk.exception.memoryException;
import com.foxitsdk.exception.parameterException;
import com.foxitsdk.exception.passwordException;
import com.foxitsdk.exception.statusException;
import com.foxitsdk.exception.toBeContinuedException;
import com.yangyang.reader.view.MainActivity;

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
	
	public static final int FXG_PT_LINETO = 2;
	public static final int FXG_PT_MOVETO = 6;
    public static final int FXG_PT_ENDPATH = 8;
    
    public static final	int PSI_ACTION_DOWN = 1;
    public static final int PSI_ACTION_UP = 2;
    public static final int PSI_ACTION_MOVE = 3;

	private final static int TIMER_EVENT_ID = 10;



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

	public class CPDFSystemTime {
		int nYear;
		int nMonth;
		int nDayofWeek;
		int nDay;
		int nHour;
		int nMinute;
		int nSecond;
		int nMilliseconds;
	}

	public class CPDFFormFillerInfo {

		public int nCallBackAddr = 0;
		public int nTimeElapse = 0;
		private MainActivity mainView = null;
		private Timer time = null;

		public CPDFFormFillerInfo(MainActivity view) {
			mainView = view;
		}

		public void Release() {

		}

		public void FFI_Invalidate(int page, float left, float top,
				float right, float bottom) {
			if (mainView != null) {
				int nCurPage = mainView.func.getCurPDFPageHandler();
				if (nCurPage != page)
					return;
				// mainView.dis
				mainView.invalidate(left, bottom, right, top);
			}
		}

		public void FFI_OutputSelectedRect(int page, double left, double top,
				double right, double bottom) {

		}

		public void FFI_SetCursor(int nCursorType) {

		}

		public int FFI_SetTimer(int uElapse, int callbackaddr) {
			nCallBackAddr = callbackaddr;
			nTimeElapse = uElapse;
			time = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					EMBJavaSupport.FPDFExecCallBack(nCallBackAddr, true);
				}
			};
			time.schedule(task, uElapse, uElapse);
			return TIMER_EVENT_ID;
		}

		public void FFI_KillTimer(int nTimerID) {
			time.cancel();
		}

		public CPDFSystemTime FFI_GetLocalTime() {
			return null;
		}

		public void FFI_OnChange() {

		}

		public int FFI_GetPage(int document, int page_index) {
			if (mainView != null) {
				int nPageHandler = mainView.func.getPageHandler(page_index);
				return nPageHandler;
			}
			return 0;
		}

		public int FFI_GetCurrentPage(int document) {
			if (mainView != null) {
				return mainView.func.getCurPDFPageHandler();
			}
			return 0;
		}

		public int FFI_GetRotation(int page) {
			return 0;
		}

		public void FFI_ExecuteNamedAction(String nameAction) {

		}

		public void FFI_OnSetFieldInputFocus(int field, String focustext,
				int nTextLen) {
			mainView.createAndroidTextField(focustext);
		}
	}

	public class CPDFJsPlatform {

		public int app_alert(String msg, String title, int type, int icon) {
			return 0;
		}

		public int app_beep(int type) {
			return 0;
		}

		public String app_response(String question, String title,
				String defval, String label, boolean password) {
			return "";
		}

		public String Doc_getFilePath() {
			return "";
		}

		public void Doc_mail(byte[] maildata, int length, boolean ui,
				String to, String subject, String cc, String bcc, String msg) {

		}

		public void Doc_print(boolean ui, int start, int end, boolean slient,
				boolean shrinktofit, boolean printasimage, boolean reverse,
				boolean annotations) {

		}

		public void Doc_submitForm(byte[] formdata, int size, String url) {

		}

		public String Field_browse(String filepath) {
			return "";
		}
	}

	public static native int FPDFFormFillerInfoAlloc(CPDFFormFillerInfo info);

	public static native void FPDFFormFillerInfoRelease(int nFormFillerInfo);

	public static native int FPDFJsPlatformAlloc(CPDFJsPlatform jsPlatform);

	public static native void FPDFJsPlatformRelease(int nJsPlatform);

	public static native void FPDFFormFillerInfoSetJsPlatform(
			int nFormFillerInfo, int nJsPlatform);

	public static native void FPDFJsPlatformSetFormFillerInfo(int nJsPlatform,
			int nFormFillerInfo);

	public static native int FPDFDocInitFormFillEnviroument(int document,
			int nFormFillerInfo);

	public static native void FPDFDocExitFormFillEnviroument(int nFormHandler);

	public static native void FPDFFormFillOnAfterLoadPage(int nFormHandler,
			int page);
	
	public static native void FPDFFormFillOnBeforeClosePage(int nFormHandler,
			int page);

	public static native void FPDFFormFillDraw(int nFormHandler, int dib,
			int page, int startx, int starty, int sizex, int sizey, int rotate,
			int flags);

	public static native void FPDFFormFillOnKillFocus(int nFormHandler);

	public static native int FPDFFormCreateInterForm(int document,
			boolean updateap);

	public static native void FPDFFormReleaseInterForm(int nInterForm);

	public static native boolean FPDFFormExportToXML(int nInterForm,
			int nFileWriter);

	public static native boolean FPDFFormImportFromXML(int nInterForm,
			int nFileReader);

	public static native void FPDFFormFillUpdatForm(int nFormHandler);

	public static native boolean FPDFFormFillOnMouseMove(int nFormHandler,
			int page, int evenflag, double pagex, double pagey);

	public static native boolean FPDFFormFillOnLButtonUp(int nFormHandler,
			int page, int evenflag, double pagex, double pagey);

	public static native boolean FPDFFormFillOnLButtonDown(int nFormHandler,
			int page, int evenflag, double pagex, double pagey);

	public static native boolean FPDFFormFillOnSetText(int nFormHandler,
			int page, String text, int evenflag);

	public static native void FPDFExecCallBack(int callbackaddr,
			boolean newThread);
	
////PSI
	public class CPDFPSI{
		private MainActivity mainView = null;
		
		public CPDFPSI(MainActivity view){
			mainView = view;
		}
		
		public void FPSI_Invalidate(int left, int top, int right, int bottom){
			Log.e("xxxxxxxxxxjavasupport","FPSI_Invalidate");
			if (mainView != null){
				mainView.invalidate(left, top, right, bottom);
			}
		}
		
		public float FPSI_GetOpacity(){
			return 1.0f;
		}
		
	}
	
	public static native int FPSIInitAppCallback(CPDFPSI pCallBack);
	public static native void FPSIReleaseAppCallback(int nCallback);
	
	public static native int FPSIInitEnvironment(int nCallback, boolean bSimulate);
	public static native void FPSIDestroyEnvironment(int nPSIHandler);
	
	public static native int FPSIInitCanvas(int nPSIHandler, int width, int height);
	public static native int FPSISetInkColor(int nPSIHandler, long color);
	public static native int FPSISetInkDiameter(int nPSIHandler, int diameter);
	public static native int FPSIAddPoint(int nPSIHandler, float x, float y, float pressure, int flag);
	public static native int FPSIRender(int nPSIHandler, int bitmap, int left, int top, int right, int bottom, int src_left, int src_top);
//	public static native int FPSIRenderWithBitmap(int nPSIHandle, Bitmap bitmap, int left, int top, int right, int bottom);
	public static native int FPDFPage_GeneratePSIAnnot(int nPSIHandler, int page, Rectangle pageRect, float left, float top, float right, float bottom);


}