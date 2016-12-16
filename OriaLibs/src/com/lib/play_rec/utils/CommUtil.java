package com.lib.play_rec.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;

public class CommUtil {

	/** 初始化基本信息 */
	public static void initialize(Activity activity) {
		/** 初始化屏幕信息 */
		GlobalInit.getInstance(activity);
	}

	/** 检测网络是否可用 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] ninfo = cm.getAllNetworkInfo();
		if (ninfo != null) {
			for (int i = 0; i < ninfo.length; i++) {
				if (ninfo[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/** 用于判断能否联网以及网络类型（0-无网络，1-WiFi，2-2G/3G网络） */
	public static int checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return 0;
		}
		NetworkInfo ntinfo = cm.getActiveNetworkInfo();
		if (ntinfo == null || !ntinfo.isConnected()) {
			return 0;
		} else {
			/** 检测网络类型 */
			int type = ntinfo.getType();
			if (type == ConnectivityManager.TYPE_WIFI) {
				return 1;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				return 2;
			}
			return 0;
		}
	}

	/** 检查URL地址是否可用 */
	public static boolean isHttpAvailable(String urlStr) {
		boolean flag = false;
		URL url = null;
		InputStream in = null;
		try {
			url = new URL(urlStr);
			in = url.openStream();
			flag = true;
		} catch (IOException e) {
			flag = false;
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/** 调用系统的ping命令判断能否链接对应的IP */
	public static boolean canPingIP(String ip) {
		Runtime run = Runtime.getRuntime();
		Process proc = null;
		try {
			/**
			 * -w timeout Timeout in milliseconds to wait for each reply.
			 * 指定超时间隔，单位为毫秒。
			 */
			String str = "ping -c 1 -w 3000" + ip;
			proc = run.exec(str);
			int result = proc.waitFor();
			if (result == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			proc.destroy();
		}
	}

	/**
	 * 删除该目录下的所有文件,如果路径为文件夹（self为true则删除该文件夹）
	 * @param f
	 * @param self
	 * @return
	 */
	public static boolean delFile(File f, boolean self) {
		boolean flag = false;
		try {
			if (!f.exists()) {
				return true;
			}
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				for (int i = 0; i < fs.length; i++) {
					flag = delFile(fs[i], self);
					if (!flag) {
						break;
					}
				}
				if (self) {
					f.delete();
				}
			} else {
				flag = f.delete();
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 删除该目录下的所有文件,如果路径为文件夹（self为true则删除该文件夹）
	 * 
	 * @param dir
	 * @param self
	 * @return
	 */
	public static boolean delFile(String dir, boolean self) {
		boolean flag = false;
		try {
			File f = new File(dir);
			if (!f.exists()) {
				return true;
			}
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				for (int i = 0; i < fs.length; i++) {
					flag = delFile(fs[i], self);
					if (!flag) {
						break;
					}
				}
				if (self) {
					f.delete();
				}
			} else {
				flag = f.delete();
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param scrFileStr
	 *            要压缩的文件/文件夹名字
	 * @param zipFileStr
	 *            指定压缩的目的和名字
	 * @param folder
	 *            如果传过来的路径是文件夹，是否需要把该文件夹当做zip包的根路径
	 * @throws Exception
	 */
	public static boolean zipFolder(String scrFileStr, String zipFileStr,
			boolean folder) throws Exception {
		boolean flag = false;
		// 创建Zip包
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
				zipFileStr));
		File file = new File(scrFileStr);
		if (file.isFile() || folder) {
			return zipFiles(file.getParent() + File.separator, file.getName(),
					outZip);
		} else {
			String fileList[] = file.list();
			if (fileList.length > 0) {
				for (int i = 0; i < fileList.length; i++) {
					flag = zipFiles(scrFileStr + File.separator, fileList[i],
							outZip);
					if (!flag) {
						break;
					}
				}
			}
		}
		// 完成,关闭
		outZip.finish();
		outZip.close();
		return flag;
	}

	/**
	 * 压缩文件
	 * 
	 * @param folderStr
	 * @param fileStr
	 * @param zipOutputStream
	 * @return
	 */
	public static boolean zipFiles(String folderString, String fileString,
			ZipOutputStream zipOutputStream) {
		if (zipOutputStream == null) {
			return false;
		}
		try {
			File file = new File(folderString + fileString);
			// 判断是不是文件
			if (file.isFile()) {
				String fileName = file.getName();
				ZipEntry zipEntry;
				if (fileName.endsWith(Config.LOCAL_AUDIO_SUFFIX)) {
					zipEntry = new ZipEntry(fileString.substring(0,
							fileString.lastIndexOf("."))
							+ Config.LOCAL_REAL_AUDIO_SUFFIX);
				} else if (fileName.endsWith(Config.LOCAL_TEXT_SUFFIX)) {
					zipEntry = new ZipEntry(fileString.substring(0,
							fileString.lastIndexOf("."))
							+ Config.LOCAL_REAL_TEXT_SUFFIX);
				} else if (fileName.endsWith(Config.LOCAL_IMAGE_SUFFIX)) {
					zipEntry = new ZipEntry(fileString.substring(0,
							fileString.lastIndexOf("."))
							+ Config.LOCAL_REAL_IMAGE_SUFFIX);
				} else {
					return true;
				}

				FileInputStream inputStream = new FileInputStream(file);
				zipOutputStream.putNextEntry(zipEntry);
				int len;
				byte[] buffer = new byte[4 * 1024];
				while ((len = inputStream.read(buffer)) != -1) {
					zipOutputStream.write(buffer, 0, len);
				}
				zipOutputStream.closeEntry();
				inputStream.close();
			} else {
				// 文件夹的方式，获取文件夹下的文件
				String fileList[] = file.list();
				// 如果没有子文件，则添加进去即可
				if (fileList.length <= 0) {
					ZipEntry zipEntry = new ZipEntry(fileString
							+ File.separator);
					zipOutputStream.putNextEntry(zipEntry);
					zipOutputStream.closeEntry();
				} else {
					// 如果有子文件，遍历子文件
					for (int i = 0; i < fileList.length; i++) {
						zipFiles(folderString, fileString + File.separator
								+ fileList[i], zipOutputStream);
					}
				}
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解压zip文件
	 * 
	 * @param zipFilePath
	 *            zip文件路径
	 * @param outFolderPath
	 *            解压后的存放路径
	 * @return
	 */
	public static boolean unZipFile(String zipFilePath, String outFolderPath) {
		try {
			ZipInputStream inZip = new ZipInputStream(new FileInputStream(
					zipFilePath));
			ZipEntry zipEntry;
			String szName = "";
			while ((zipEntry = inZip.getNextEntry()) != null) {
				szName = zipEntry.getName();
				if (zipEntry.isDirectory()) {
					szName = szName.substring(0, szName.length() - 1);
					File folder = new File(outFolderPath + szName);
					folder.mkdirs();
				} else {
					// TODO
//					if (szName.endsWith(Constant.AudioServSuffix)) {
//						szName = szName.substring(0, szName.lastIndexOf("."))
//								+ Constant.AudioLocalSuffix;
//					} else if (szName.endsWith(Constant.TextServSuffix)) {
//						szName = szName.substring(0, szName.lastIndexOf("."))
//								+ Constant.TextLocalSuffix;
//					} else {
//						szName = szName.substring(0, szName.lastIndexOf("."))
//								+ Constant.ImageLocalSuffix;
//					}
					File file = new File(outFolderPath + szName);
					FileOutputStream out = new FileOutputStream(file);
					int len;
					byte[] buffer = new byte[4 * 1024];
					while ((len = inZip.read(buffer)) != -1) {
						out.write(buffer, 0, len);
						out.flush();
					}
					out.close();
				}
			}
			inZip.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 把字符串数组转成按给定分隔符的字符串
	 * 
	 * @param arr
	 * @param split
	 * @return
	 */
	public static String arrToString(String[] arr, String split) {
		StringBuffer sb = new StringBuffer();
		if (arr != null && split != null) {
			for (int i = 0; i < arr.length; i++) {
				if (i == 0) {
					sb.append(arr[i]);
				} else {
					sb.append(split + arr[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 把List集合转成按给定分隔符的字符串
	 * 
	 * @param list
	 * @param split
	 * @return
	 */
	public static String listToString(List<String> list, String split) {
		StringBuffer sb = new StringBuffer();
		if (list != null && split != null) {
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					sb.append(list.get(i));
				} else {
					sb.append(split + list.get(i));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 判断是否是多次重复点击
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
//		long time = System.currentTimeMillis();
//		long timeD = time - Constant.lastClickTime;
//		if (0 < timeD && timeD < 1000) {
//			return true;
//		}
//		Constant.lastClickTime = time;
		return false;
	}

}
