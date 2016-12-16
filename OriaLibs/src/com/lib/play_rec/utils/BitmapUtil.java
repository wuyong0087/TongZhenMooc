package com.lib.play_rec.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.lib.play_rec.entity.GlobalInit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {

	/** 获取圆角图片 */
	public static Bitmap getRoundedCornerBitmap(int width, int height,
			int color, float roundPx) {
		Bitmap bmp = getPureColorBitmap(width, height, color);
		Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(output, rect, rect, paint);
		if (!bmp.isRecycled()) {
			bmp.recycle();
		}
		return output;
	}

	/**
	 * 复制相册文件到指定文件
	 * 
	 * @param filePath
	 *            相册文件路径
	 * @param savePath
	 *            指定文件路径
	 * @return
	 */
	public static boolean saveImageFile(String filePath, String savePath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(filePath);
			if (oldfile.exists()) {
				inStream = new FileInputStream(filePath); // 读入原文件
				fs = new FileOutputStream(savePath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 获取纯色图片 */
	public static Bitmap getPureColorBitmap(int width, int height, int color) {
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawColor(color);
		return output;
	}

	/** 合并图片 */
	public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground) {
		int bgW = background.getWidth();
		int bgH = background.getHeight();
		int fgW = foreground.getWidth();
		int fgH = foreground.getHeight();

		Bitmap newBmp = Bitmap.createBitmap(bgW, bgH, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBmp);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(foreground, (bgW - fgW) / 2, (bgH - fgH) / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newBmp;
	}

	/**
	 * 获取笔宽的图片
	 * 
	 * @param width
	 *            整张图片的宽度
	 * @param height
	 *            整张图片的高度
	 * @param color
	 *            图片前景色
	 * @param h
	 *            前景图片高度
	 * @return
	 */
	public static Bitmap getStrokeBitmap(int width, int height, int color, int h) {
		Bitmap bmp = getPureColorBitmap(width, h, color);

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawBitmap(bmp, 0, (height - h) / 2, new Paint());

		return output;
	}

	/** 把图片按固定比例缩放 */
	public static Bitmap getScaleBitmap(Bitmap bmp, int width, int height) {
		Bitmap newBmp = null;
		int imageHeight = bmp.getHeight();
		int imageWidth = bmp.getWidth();
		float scaleWidth = (float) width / imageWidth;
		float scaleHeight = (float) height / imageHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
				matrix, true);
		/*
		 * if (!bmp.isRecycled()) { bmp.recycle(); }
		 */
		return newBmp;
	}

	/** 把图片按固定比例缩放,根据宽高比，来取最适合的压缩方式 */
	public static Bitmap getScaleBitmap2(Bitmap bmp, int width, int height) {
		if (bmp == null) { // 当给的图片路径的资源对象不是图片文件时。
			return null;
		}
		Bitmap newBmp = null;
		int imageHeight = bmp.getHeight();
		int imageWidth = bmp.getWidth();
		float scaleWidth = (float) width / imageWidth;
		float scaleHeight = (float) height / imageHeight;

		Matrix matrix = new Matrix();
		if (scaleWidth >= scaleHeight) {
			matrix.postScale(scaleHeight, scaleHeight);
		} else {
			matrix.postScale(scaleWidth, scaleWidth);
		}
		newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
				matrix, true);
		if (bmp != null) {
			bmp.recycle();
		}
		return newBmp;
	}

	// 图片缩放,按宽度比例
	public static Bitmap zoomBitmpWidth(Bitmap bmp, int width) {
		Bitmap newBmp = null;
		int imageHeight = bmp.getHeight();
		int imageWidth = bmp.getWidth();
		float scaleWidth = (float) width / imageWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
				matrix, true);
		if (!bmp.isRecycled()) {
			bmp.recycle();
		}
		return newBmp;
	}

	// 图片缩放，按高度比例
	public static Bitmap zoomBitmpHeight(Bitmap bmp, int height) {
		Bitmap newBmp = null;
		int imageHeight = bmp.getHeight();
		int imageWidth = bmp.getWidth();
		float scaleHeight = height / imageHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleHeight, scaleHeight);
		newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
				matrix, true);
		if (!bmp.isRecycled()) {
			bmp.recycle();
		}
		return newBmp;
	}

	// drawable转为bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/** 压缩图片质量 */
	public static Bitmap compressBitmap(Bitmap bmp, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		if (!bmp.isRecycled()) {
			bmp.recycle();
		}
		return bitmap;
	}

	/** 压缩图片质量,返回字节数组 */
	public static byte[] compressBitmap2(Bitmap bmp, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		if (!bmp.isRecycled()) {
			bmp.recycle();
		}
		return baos.toByteArray();
	}

	/** 压缩图片，以防OOM */
	public static Bitmap getBitmapFromPath2(String path) {

		Bitmap map = null;
		FileInputStream inputStream = null;
		FileDescriptor fd;
		try {
			inputStream = new FileInputStream(path);
			fd = inputStream.getFD();
			int reqHeight = (int) (GlobalInit.getInstance().getScreenWidth() * 0.54);
			int reqWidth = (int) (GlobalInit.getInstance().getScreenHeight() * 0.92);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			int height = options.outHeight;
			int width = options.outWidth;
			int inSampleSize = 1;
			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width
						/ (float) reqWidth);
				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
				inSampleSize = (int) (inSampleSize + 1);
			}
			if (inSampleSize <= 0) {
				inSampleSize = 1;
			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			map = BitmapFactory.decodeFile(path, options);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据图片路径处理图片
	 * @throws IOException
	 */
	public static Bitmap getBitmapFromPath(String filePath, String savePath,
			int reqWidth, int reqHeight, int size) throws IOException {
		int MINVALUE = 150;
		int width = 0, height = 0;

		// 获取原始图片的长和宽
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int h = options.outHeight;
		int w = options.outWidth;
		if (w <= 0 || h <= 0) {
			throw new IOException("图片已损坏");
		}
		width = w;
		height = h;
		// 图片宽高的最大值与定义的最小值判断
		if (Math.max(w, h) < MINVALUE) {
			if (w > h) {
				width = MINVALUE;
				height = (int) (h / (float) w * MINVALUE);
			} else {
				height = MINVALUE;
				width = (int) (w / (float) h * MINVALUE);
			}
		}
		// 计算压缩比例
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

		}

		if (inSampleSize <= 0) {
			inSampleSize = 1;
		}
		// 缩放并压缩图片
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		Bitmap newBmp = null;
		if (bitmap.getWidth() > reqWidth || bitmap.getHeight() > reqHeight) {
			newBmp = getScaleBitmap2(bitmap, reqWidth, reqHeight);
		}

		byte[] buffer = null;
		if (newBmp != null) {
			buffer = compressBitmap2(newBmp, size);
		} else {
			buffer = compressBitmap2(bitmap, size);
		}

		// 文件保存
		FileOutputStream fos = new FileOutputStream(savePath);
		fos.write(buffer);
		fos.flush();
		fos.close();

		ByteArrayInputStream isBm = new ByteArrayInputStream(buffer);
		return BitmapFactory.decodeStream(isBm, null, null);
	}

	public static Bitmap getBitmapFromPath1(String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		return bmp;
	}

	/**
	 * @param filePath
	 *            以.png为后缀的图片路径
	 * @param width
	 *            需要的宽度
	 * @param height
	 *            需要的高度
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getDatBmpWithPngPathForWORH(String filePath,
			int width, int height) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			if (bitmap.getWidth() != width) {
				return BitmapUtil.getScaleBitmap2(bitmap, width, height);
			} else {
				return getBitmapFromPath1(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Bitmap getDatBmpWithPngPathForWORH(String filePath,
			boolean exceptionDel) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(filePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[4 * 1024];
			int len = 0;
			while ((len = fis.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			baos.flush();
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;
		} catch (Exception e) {
			if (exceptionDel) {
				CommUtil.delFile(filePath, false);
			}
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

}
