package com.lib.play_rec.rec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.OutputFormat;

import com.lib.play_rec.utils.DateUtil;

public class MediaCapture {

	private List<String> recordFiles; // 音频集合
	private String voiceName;
	private MediaRecorder recorder = null;

	public MediaCapture() {
		try {
			recordFiles = new ArrayList<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startRecord() {
		try {
			voiceName = DateUtil.getTimeMillisStr() + ".amr";
			recorder = new MediaRecorder();
			// 设置录音为麦克风
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置输出格式
			recorder.setOutputFormat(OutputFormat.AMR_WB);
			recorder.setAudioEncoder(AudioEncoder.AMR_WB);
			// 录音文件保存目录
			recorder.setOutputFile(JsonOperater.getInstance()
					.getVideoFullPath() + voiceName);
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 重置时清空录音 */
	public void clearRecord() {
		recordFiles.clear();
	}

	public void pauseRecord() {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
			recordFiles.add(voiceName);
		}
	}

	public void stopRecord() throws IOException {
		OutputStream os = null;
		if (recordFiles.size() >= 1) {
			voiceName = JsonOperater.getInstance().getVideoFullPath()
					+ JsonOperater.getInstance().getVoiceName();
			File file = new File(voiceName);
			os = new FileOutputStream(file);
			for (int i = 0; i < recordFiles.size(); i++) {
				File f = new File(JsonOperater.getInstance().getVideoFullPath()
						+ recordFiles.get(i));
				FileInputStream fis = new FileInputStream(f);
				byte[] b = new byte[fis.available()];
				int length = b.length;
				// 头文件
				if (i == 0) {
					if (length > 0) {
						while (fis.read(b) != -1) {
							os.write(b, 0, length);
						}
					}
				} else {
					// 之后的文件，去掉头文件就可以了
					while (fis.read(b) != -1) {
						// AMR_RAW及AMR_NB格式的头文件长度
//						os.write(b, 6, length - 6);
						// AMR_WB的头文件长度
						os.write(b, 9, length - 9);
					}
				}
				fis.close();
				f.delete();
			}
			os.flush();
			os.close();

		}
	}

}
