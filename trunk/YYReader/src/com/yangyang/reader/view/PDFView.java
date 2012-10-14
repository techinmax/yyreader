package com.yangyang.reader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PDFView extends ImageView{

	private Bitmap m_map = null;	
	public PDFView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PDFView(Context context) {
		super(context);
	}
	
	public void setBitmap(Bitmap map)
	{
		m_map = map;
	}	
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		if(m_map != null)
			canvas.drawBitmap(m_map, 0, 0, null);
	
	}
	public Bitmap GetCurBitmap()
	{
		return m_map;
	}
}
