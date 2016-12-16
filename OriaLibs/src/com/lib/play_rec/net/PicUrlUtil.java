package com.lib.play_rec.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class PicUrlUtil {

	/**
	 * 根据一个网络连接（String）获取bitmapDrawable
	 * 
	 * @param path
	 * @return
	 */
	public BitmapDrawable getDrawablePicByPath(String path) {
		BitmapDrawable icon = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			icon = new BitmapDrawable(conn.getInputStream()); // 将输入流转成bitmap
		} catch (Exception e) {

		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
			}
		}
		return icon;
	}

	/**
	 * 根据一个网络连接（String）获取Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapPicByPath(String downUrl, String savePath) {
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			// 对响应码进行判断
			if (conn.getResponseCode() != 200) {
				throw new Exception("请求url失败");
			}
			InputStream is = conn.getInputStream();
			if (savePath == null) {
			}
			bitmap = BitmapFactory.decodeStream(is); // 将输入流转成bitmap
			if (savePath != null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(savePath);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
				} catch (IOException e1) {
				} finally {
					if (out != null) {
						out.close();
					}
				}
			}
			is.close();
		} catch (Exception e) {

		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

}
