package io.vtown.WeiTangApp.comment.multiphoto.model;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * 图片对象
 *
 */
public class ImageItem1 implements Serializable
{
	private static final long serialVersionUID = -7188270558443739436L;
	public String imageId;
	public String thumbnailPath;
	public String sourcePath;
	public Bitmap mBitmap;
	public boolean isSelected = false;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ImageItem1() {
		super();
		
	}
	public ImageItem1(String thumbnailPath) {
		super();
		this.sourcePath = thumbnailPath;
	}
	public ImageItem1(String sourcePath, Bitmap mBitmap) {
		super();
		this.sourcePath = sourcePath;
		this.mBitmap = mBitmap;
	}
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	 
	
	
}
