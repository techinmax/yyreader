package com.foxitsdk.service;

import FoxitEMBSDK.EMBJavaSupport;

import com.foxitsdk.exception.invalidLicenseException;
import com.foxitsdk.exception.parameterException;

/**
 * defined for a wrap for All PDF implements��
 * @author Foxit
 *
 */

public class WrapPDFFunc
{
	
	/** The PDF Engine*/
	
	/** state variables*/
	private static final String TAG = "WrapPDFFunc";
	
	/** Init EMB SDK
	 * @throws invalidLicenseException 
	 * @throws parameterException */
	public boolean InitFoxitFixedMemory(int initMemSize) throws parameterException, invalidLicenseException{
		EMBJavaSupport.FSMemInitFixedMemory(initMemSize);		
		EMBJavaSupport.FSInitLibrary(0);
		EMBJavaSupport.FSUnlock("SDKEDTEMP", "3C86F25880658927118E766271BEB68454E49DFD");
		return true;
	}
	
	/** Destroy EMB SDK*/
	public boolean DestroyFoxitFixedMemory(){
		EMBJavaSupport.FSDestroyLibrary();	
		EMBJavaSupport.FSMemDestroyMemory();		
		return true;
	}
	
	public FoxitDoc createFoxitDoc(String fileName, String password){
		return new FoxitDoc(fileName,password);
	}
	
	void closeFoxitDoc(FoxitDoc doc){
		doc.close();
	}
	
	
	/** Load jbig2 decoder.*/
	public void LoadJbig2Decoder(){
		EMBJavaSupport.FSLoadJbig2Decoder();
	}
	
	/** Load jpeg2000 decoder. */
	public void LoadJpeg2000Decoder(){
		EMBJavaSupport.FSLoadJpeg2000Decoder();
	}
	
	/** */
	public void LoadJapanFontCMap(){
		EMBJavaSupport.FSFontLoadJapanCMap();
		EMBJavaSupport.FSFontLoadJapanExtCMap();
	}
	
	/** */
	public void LoadCNSFontCMap(){
		EMBJavaSupport.FSFontLoadGBCMap();
		EMBJavaSupport.FSFontLoadGBExtCMap();
		EMBJavaSupport.FSFontLoadCNSCMap();
	}
	
	public void LoadKoreaFontCMap(){
		EMBJavaSupport.FSFontLoadKoreaCMap();
	}
	
	public void SetFontFileMap(String strFontFilePath) throws parameterException
	{
		EMBJavaSupport.FSSetFileFontmap(strFontFilePath);
	}
}