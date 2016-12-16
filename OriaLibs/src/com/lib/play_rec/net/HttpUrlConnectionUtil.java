package com.lib.play_rec.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUrlConnectionUtil {

	private HttpURLConnection conn = null;

	private String end = "\r\n";
	private String twoHyphens = "--"; // 两个连字符
	private String boundary = "******"; // 分界符的字符串

	/** post方式请求 */
	public synchronized String post(String url, String post_data) {
		String result = null;
		try {
			URL localUrl = new URL(url);
			conn = (HttpURLConnection) localUrl.openConnection();
			conn.setConnectTimeout(15 * 1000); // 设置超时
			conn.setUseCaches(false); // 设置不使用缓存
			conn.setDoInput(true);
			conn.setDoOutput(true); // 设置输出流
			conn.setRequestProperty("Charset", "UTF-8"); // 设置文件字符集
			conn.setRequestMethod("POST");
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(post_data.getBytes());
			// 刷新，关闭
			outputStream.flush();
			outputStream.close();
			result = readData(conn.getInputStream(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				conn.disconnect(); // 关闭http连接
				conn = null;
			} catch (Exception e) {
			}
		}
		return result;
	}

	/** get方式请求 */
	public synchronized String get(String url) {
		String result = null;
		try {
			URL localUrl = new URL(url);
			conn = (HttpURLConnection) localUrl.openConnection();
			conn.setConnectTimeout(15 * 1000); // 设置超时
			conn.setUseCaches(false); // 设置不使用缓存
			conn.setDoInput(true);
			conn.setDoOutput(true); // 设置输出流
			conn.setRequestProperty("Charset", "UTF-8"); // 设置文件字符集

			conn.setRequestMethod("GET");
			// 对响应码进行判断
			if (conn.getResponseCode() != 200) {
				throw new Exception("请求url失败");
			}
			result = readData(conn.getInputStream(), "UTF-8");
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				conn.disconnect(); // 关闭http连接
				conn = null;
			} catch (Exception e) {
			}
		}
		return result;
	}

	// 第一个参数为输入流,第二个参数为字符集编码
	private synchronized String readData(InputStream inSream, String charsetName)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4 * 1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}

	/**
	 * 文件下载 ，并存入指定文件
	 * @throws Exception
	 */
	public synchronized boolean downFile(String url, File file) {
		InputStream is = null;
		OutputStream os = null;
		try {
			URL localUrl = new URL(url);
			conn = (HttpURLConnection) localUrl.openConnection();
			conn.setConnectTimeout(30 * 1000); // 设置超时
			conn.setUseCaches(false); // 设置不使用缓存
			conn.setDoInput(true);
			// 设置Http请求头
			is = conn.getInputStream();
			os = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} catch (Exception e) {
			file.delete();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
			}
			try {
				conn.disconnect(); // 关闭http连接
				conn = null;
			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 * 文件下载 ,返回一个字节数组
	 * 
	 * @throws Exception
	 */
	/*
	 * public byte[] downFile(String url) throws Exception{ InputStream is =
	 * null; ByteArrayOutputStream bos = null; try{ URL localUrl = new URL(url);
	 * conn = (HttpURLConnection) localUrl.openConnection();
	 * conn.setConnectTimeout(15*1000); // 设置超时 conn.setUseCaches(false);
	 * //设置不使用缓存 conn.setDoInput(true); is = conn.getInputStream(); bos = new
	 * ByteArrayOutputStream(); byte buffer[] = new byte[8*1024]; int len=0;
	 * while((len = is.read(buffer))!=-1){ bos.write(buffer,0,len); } byte[]
	 * data = bos.toByteArray(); return data; }catch(Exception e){ throw new
	 * Exception("请求url失败"); }finally{ try{ if(bos!=null){ bos.close(); }
	 * if(is!=null){ is.close(); } }catch(Exception e){ e.printStackTrace(); }
	 * try{ conn.disconnect(); //关闭http连接 conn = null; }catch(Exception e){
	 * 
	 * } } }
	 */

	/** 文件上传 */
	public String uploadFile(String url, Map<String, String> textParams,
			Map<String, String> fileParams) {
		String result = null;
		try {
			URL localUrl = new URL(url);
			conn = (HttpURLConnection) localUrl.openConnection();
			conn.setConnectTimeout(15 * 1000); // 设置超时
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setChunkedStreamingMode(0);
			// 设置Http请求头
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			// 必须在Content-Type 请求头中指定分界符中的任意字符串
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			// 定义数据写入流，准备上传文件
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			/** 普通字符串数据上传 */
			if (textParams != null && textParams.size() > 0) {
				for (Entry<String, String> entry : textParams.entrySet()) {
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + end);
					dos.writeBytes(end);
					dos.write(entry.getValue().getBytes());
					dos.writeBytes(end);
				}
			}
			/** 文件上传 */
			if (fileParams != null && fileParams.size() > 0) {
				for (Entry<String, String> fileEntry : fileParams.entrySet()) {
					String value = fileEntry.getValue();
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ fileEntry.getKey()
							+ "\"; filename=\""
							+ value.substring(fileEntry.getValue().lastIndexOf(
									"/") + 1) + "\"" + end);
					dos.writeBytes(end);
					FileInputStream fis = new FileInputStream(
							fileEntry.getValue());
					byte[] buffer = new byte[8 * 1024];
					int count = 0;
					// 读取文件内容，并写入OutputStream对象
					while ((count = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, count);
					}
					fis.close();
					dos.writeBytes(end);
				}
			}
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.writeBytes(end);
			// 刷新，关闭
			dos.flush();
			dos.close();
			result = readData(conn.getInputStream(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				conn.disconnect(); // 关闭http连接
			} catch (Exception e) {
			}
		}
		return result;
	}

}
