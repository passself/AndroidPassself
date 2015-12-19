package com.pass.self.qrcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成工具类
 * @author shihx
 *
 */
public class QRCodeUtil {
	private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小
	/**
	 * 
	 * @param content 内容
	 * @param widthPix 图片宽度
	 * @param heightPix 图片高度
	 * @param logoBm 二维码中心的logo图标
	 * @param filePath 用于存储二维码图片的文件路径
	 * @return 返回生成是否成功的结果
	 */
	public static boolean createQRImage(String content,int widthPix,int heightPix,Bitmap logoBm,String filePath){
		boolean result = false;
		
		if(TextUtils.isEmpty(content)){
			result = false;
		}
		try {
			//配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			//设置白边距的宽度
			hints.put(EncodeHintType.MARGIN, 2);
			
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix,hints);
			int[] pixels = new int[widthPix * heightPix];
			//下面这里按照二维码算法，逐个生成二维码的图片
			// 两个for循环是图片横列扫描的结果
			for(int y = 0;y < heightPix;y++){
				for(int x = 0; x<widthPix; x++){
					if(bitMatrix.get(x, y)){
						pixels[y*widthPix + x] = 0xff000000;
					}else{
						pixels[y*widthPix + x] = 0xffffffff;
					}
				}
			}
			
			//生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
			
			if(logoBm != null){
				bitmap = addLogo(bitmap, logoBm);
			}
			
			result = bitmap !=null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
		} catch (WriterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	private static Bitmap addLogo(Bitmap src,Bitmap logo){
		if(src == null || logo == null){
			return null;
		}
		
		//获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		
		if(srcWidth == 0 || srcHeight == 0){
			return null;
		}
		
		if(logoWidth ==0 || logoHeight == 0){
			return src;
		}
		
		//logo 大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f /5 /logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth/2, srcHeight/2);
			canvas.drawBitmap(logo, (srcWidth-logoWidth)/2,(srcHeight - logoHeight)/2, null);
			
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			// TODO: handle exception
			bitmap = null;
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
     * 生成二维码
     * @param string 二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * @return Bitmap 位图
     * @throws WriterException
     */
    public static Bitmap createCode(String string,Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                	//该位置用于存放图片信息
                	//记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);                
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
