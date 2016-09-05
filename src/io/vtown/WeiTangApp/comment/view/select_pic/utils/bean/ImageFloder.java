package io.vtown.WeiTangApp.comment.view.select_pic.utils.bean;

public class ImageFloder
{
	/**
	 * ͼƬ���ļ���·��
	 */
	private String dir;

	/**
	 * ��һ��ͼƬ��·��
	 */
	private String firstImagePath;

	/**
	 * �ļ��е�����
	 */
	private String name;

	/**
	 * ͼƬ������
	 */
	private int count;

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	

}
