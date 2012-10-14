package com.yangyang.reader.util;

/**
 * 放大缩小的逻辑
 * 
 * @author yangyang
 * 
 */
public class ZoomStatus {

	private int zoomStatus;
	private int width, height, displayWidth, displayHeight;

	public int getDisplayWidth() {
		return displayWidth;
	}

	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private float scale = 1f;

	public final static int Status_ActiualSize = 0;// 实际大小，不做缩放
	public final static int Status_FitWidth = 1; // 适合的宽度
	public final static int Status_FitHeight = 2;// 适合的高度
	public final static int Status_AutoZoom = 3;// 手动缩放

	public final static float scaleMin = 0.5f;
	public final static float scaleMAX = 2f;
	public final static float scaleIncreament = 0.1f;

	public ZoomStatus(int width, int height, int displayWidth, int displayHeight) {
		this.width = width;
		this.height = height;
		this.displayHeight = displayHeight;
		this.displayWidth = displayWidth;
	}

	public int getWidth() {
		switch (zoomStatus) {
		case Status_ActiualSize:
			return this.width;
		case Status_FitWidth:
			return displayWidth;
		case Status_FitHeight:
			return this.width * displayHeight / this.height;
		case Status_AutoZoom:
			return (int) (this.width * this.scale);
		}
		return -1;
	}

	public int getHeight() {
		switch (zoomStatus) {
		case Status_ActiualSize:
			return this.height;
		case Status_FitWidth:
			return this.height * displayWidth / this.width;
		case Status_FitHeight:
			return displayHeight;
		case Status_AutoZoom:
			return (int) (this.height * this.scale);
		}
		return -1;
	}

	public void nextZoom(float scale) {
		if (scale > 0) {
			this.scale += scale;
			if (this.scale > scaleMAX)
				this.scale = scaleMAX;
			if (this.scale < scaleMin)
				this.scale = scaleMin;
			this.zoomStatus = Status_AutoZoom;
		} else {
			this.zoomStatus = (++this.zoomStatus) % 4;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
