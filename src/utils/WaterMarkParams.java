package utils;

import java.awt.Font;

public class WaterMarkParams{
	/**
	 * 水印文字
	 */
	private String pressText;
	/**
	 * 需要加水印的图片路径
	 */
	private String targetImg;
	/**
	 * 水印图片路径
	 */
	private String waterImgPath;
	/**
	 * 水印图片间隔
	 */
	private int interval = 20;
	/**
	 * 旋转角度
	 */
	private double degree = - Math.PI / 8d;
	/**
	 * 字体格式：常规，加粗。。。
	 */
	private int fontStyle = Font.PLAIN;
	/**
	 * 字体类型
	 */
	private String fontName = "宋体";
	/**
	 * 字体颜色
	 */
	private String colorStr = "#E90003";
	/**
	 * 字体大小
	 */
	private int fontSize = 90;
	/**
	 * 透明度0.1f-0.9f
	 */
	private float alpha = 0.1f;
	/**
	 * 空心还是实心
	 */
	private boolean carelessness = false;
	/**
	 * 行间距
	 */
	private int colSpacing = 5;
	
	public String getPressText() {
		return pressText;
	}
	public void setPressText(String pressText) {
		this.pressText = pressText;
	}
	public String getTargetImg() {
		return targetImg;
	}
	public void setTargetImg(String targetImg) {
		this.targetImg = targetImg;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public String getColorStr() {
		return colorStr;
	}
	public void setColorStr(String colorStr) {
		this.colorStr = colorStr;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public boolean isCarelessness() {
		return carelessness;
	}
	public void setCarelessness(boolean carelessness) {
		this.carelessness = carelessness;
	}
	public int getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	public double getDegree() {
		return degree;
	}
	public void setDegree(double degree) {
		this.degree = degree;
	}
	public int getColSpacing() {
		return colSpacing;
	}
	public void setColSpacing(int colSpacing) {
		this.colSpacing = colSpacing;
	}
	public String getWaterImgPath() {
		return waterImgPath;
	}
	public void setWaterImgPath(String waterImgPath) {
		this.waterImgPath = waterImgPath;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
}