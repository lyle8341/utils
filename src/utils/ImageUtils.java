package utils;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * http://www.jb51.net/article/87382.htm
 * http://www.cnblogs.com/sunxucool/archive/2013/05/29/3106240.html
 * @author Lyle
 *
 */
public class ImageUtils {

	/**
	 * 斜水印,重复水印,文字
	 * @param params 水印参数封装对象
	 * @param path 图片输出路径
	 */
	public static void pressTest(WaterMarkParams params,String path){
		try {
			bufferedImageToImage(params, path, pressText(params));
			System.out.println("水印生成成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 /**
	  * 斜水印,重复水印,文字
	  * @param pressText  文字
	  * @param targetImg  目标图片
	  * @param fontName 字体名称
	  * @param colorStr 字体颜色字符串，格式如：#29944f
	  * @param fontSize  字体大小
	  * @param alpha 透明度(0.1-0.9)
	  * @param carelessness true为字体实心,false为字体空心
	  * @return
	  */
	 private static BufferedImage pressText(WaterMarkParams params) {
	  try {
	   File file = new File(params.getTargetImg());
	   Image src = ImageIO.read(file);
	   //图片宽度
	   int width = src.getWidth(null);
	   //图片高度
	   int height = src.getHeight(null);
	   BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	   Graphics2D g2d  = image.createGraphics();
	   //绘原图
	   g2d.drawImage(src, 0, 0, width, height, null);
	   //比例
	   g2d.scale(1, 1);
	   g2d.addRenderingHints(new RenderingHints(
	     RenderingHints.KEY_ANTIALIASING,
	     RenderingHints.VALUE_ANTIALIAS_ON));
	   g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	     RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	   //颜色
	   Color color = Color.decode(params.getColorStr());
	   //字体
	   Font font = new Font(params.getFontName(), params.getFontStyle(), params.getFontSize());
	   GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(),
	     params.getPressText());
	   Rectangle size = fontGV
	     .getPixelBounds(g2d.getFontRenderContext(), 0, 0);
	   Shape textShape = fontGV.getOutline();
	   double textWidth = size.getWidth();
	   double textHeight = size.getHeight();
	   //旋转角度
	   AffineTransform rotate45 = AffineTransform.getRotateInstance(params.getDegree());//d是double的意思
	   Shape rotatedText = rotate45.createTransformedShape(textShape);
	   // use a gradient that repeats 4 times 
	   g2d.setPaint(new GradientPaint(0, 0, color,
	   image.getWidth() / 2, image.getHeight() / 2,color));
	   //透明度
	   g2d.setStroke(new BasicStroke(params.getAlpha()));
	   // step in y direction is calc'ed using pythagoras + 5 pixel padding  
	   double yStep = Math.sqrt(textWidth * textWidth / 2) + 5;//一行每次错开
	   // step over image rendering watermark text      
	   //=======================
	   for (double x = -textHeight * params.getColSpacing(); x < image.getWidth(); x += (textHeight * params.getColSpacing())) {
	    double y = -yStep;
	    for (; y < image.getHeight(); y += yStep) {
	     g2d.draw(rotatedText);
	     if(params.isCarelessness())//字体实心
	     {
	      g2d.fill(rotatedText);
	     }
	     g2d.translate(0, yStep);
	    }
	    //g2d.translate(textHeight * params.getColSpacing(), -(y + yStep));
	    g2d.translate(textHeight * params.getColSpacing(), -(y + yStep));
	   }
	   //==========================
	   g2d.dispose();
	   return image;
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  return null;
	 }

	    /** 
	     * 给图片添加水印图片、可设置水印图片旋转角度 
	     *  
	     * @param waterImgPath 
	     *            水印图片路径 
	     * @param srcImgPath 
	     *            源图片路径 
	     * @param targerPath 
	     *            目标图片路径 
	     * @param degree 
	     *            水印图片旋转角度 
	     */  
	public static void pressImg(WaterMarkParams params,String outpath) throws Exception{
        try {  
            Image srcImg = ImageIO.read(new File(params.getTargetImg()));  
  
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),  
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);  
            // 1、得到画笔对象  
            Graphics2D g = buffImg.createGraphics();  
            // 2、设置对线段的锯齿状边缘处理  
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg  
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);  
            // 3、设置水印旋转  
            if (0 != params.getDegree()) {  
                g.rotate(Math.toRadians(params.getDegree()),  
                        (double) buffImg.getWidth() / 2, (double) buffImg  
                                .getHeight() / 2);  
            }  
            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度  
            ImageIcon imgIcon = new ImageIcon(params.getWaterImgPath());  
            // 5、得到Image对象。  
            Image img = imgIcon.getImage();  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,  
                    params.getAlpha()));  
            // 6、水印图片的位置  
            for (int height = params.getInterval() + imgIcon.getIconHeight(); height < buffImg  
                    .getHeight(); height = height + params.getInterval() + imgIcon.getIconHeight()) {  
                for (int weight = params.getInterval() + imgIcon.getIconWidth(); weight < buffImg  
                        .getWidth(); weight = weight + params.getInterval() + imgIcon.getIconWidth()) {  
                    g.drawImage(img, weight - imgIcon.getIconWidth(), height  
                            - imgIcon.getIconHeight(), null);  
                }
            }  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));  
            // 7、释放资源  
            g.dispose();  
            // 8、生成图片
            bufferedImageToImage(params, outpath, buffImg);  
            System.out.println("图片完成添加水印图片");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
    }

		/**
		 * 
		 * @param params
		 * @param outpath
		 * @param buffImg
		 * @throws IOException
		 */
		private static void bufferedImageToImage(WaterMarkParams params,
				String outpath, BufferedImage buffImg) throws IOException {
			String targetImg = params.getTargetImg();
			String formatName = targetImg.substring(targetImg.lastIndexOf(".")+1);
            ImageIO.write(buffImg, formatName, new File(outpath));
		}  
}