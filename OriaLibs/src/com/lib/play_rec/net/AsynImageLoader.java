package com.lib.play_rec.net;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.lib.play_rec.utils.BitmapUtil;

public class AsynImageLoader {
	
	// 缓存下载过的图片Map
	private Map<String,SoftReference<Bitmap>> caches;
	// 任务队列
	private List<Task> taskQueue;
	private boolean isRunning = false;
	private PicUrlUtil picUrlUtil;
	
	public AsynImageLoader(){
		// 初始化变量
		caches = new HashMap<String,SoftReference<Bitmap>>();
		taskQueue = new ArrayList<AsynImageLoader.Task>();
		// 启动图片下载线程
		isRunning = true;
		picUrlUtil = new PicUrlUtil();
		new Thread(runnable).start();
	}
	
	/**
	 * @param imageView  需要延迟加载图片的对象
	 * @param url  图片的URL地址
	 * @param resId  图片加载过程中显示的图片资源
	 */
	public void showImageAsyn(ImageView imageView,String url,int resId){
		imageView.setTag(url);
		Bitmap bitmap = loadImageAsyn(url,null,getImageCallback(imageView,resId));
		if(bitmap==null){
			imageView.setImageResource(resId);
		}else{
			imageView.setImageBitmap(bitmap);
		}
	}
	
	public Bitmap loadImageAsyn(String url,String path,ImageCallback callback){
		// 判断缓存中是否存在该图片
		if(caches.containsKey("".equals(url)?path:url)){
			// 取出软引用
			SoftReference<Bitmap> rf = caches.get("".equals(url)?path:url);
			// 通过软引用获取图片
			Bitmap bitmap = rf.get();
			// 如果该图片已经被释放，则将该path对应的键从Map中移除掉
			if(bitmap==null){
				caches.remove("".equals(url)?path:url);
			}else{
				// 如果图片未被释放，直接返回该图片
				return bitmap;
			}
		}
		
		if(url==null){
			// 本地录制的视频
			Bitmap bitmap = BitmapUtil.getDatBmpWithPngPathForWORH(path,false);
			caches.put(path, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		}else{
			if(path!=null){
				Bitmap bitmap = BitmapUtil.getDatBmpWithPngPathForWORH(path,true);
				if(bitmap!=null){
					caches.put(url, new SoftReference<Bitmap>(bitmap));
					return bitmap;
				}
			}
		}
		
		// 如果缓存中不存在该图片，则创建图片下载任务
		Task task = new Task();
		task.url = url;
		task.callback = callback;
		task.path = path;
		if(!taskQueue.contains(task)){
			taskQueue.add(task);
			// 唤醒任务下载队列
			synchronized(runnable){
				runnable.notify();
			}
		}
		
		// 缓存中没有图片则返回null
		return null;
	}
	
	private ImageCallback getImageCallback(final ImageView imageView,final int resId){
		return new ImageCallback(){
			@Override
			public void loadImage(String path,Bitmap bitmap){
				if(path.equals(imageView.getTag().toString())){
					imageView.setImageBitmap(bitmap);
				}else{
					imageView.setImageResource(resId);
				}
			}
		};
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			// 子线程中返回的下载完成的任务
			Task task = (Task)msg.obj;
			// 调用callback对象的loadImage方法，并将图片路径和图片回传给adapter
			task.callback.loadImage(task.url, task.bitmap);
		}
	};
	
	private Runnable runnable = new Runnable(){
		@Override
		public void run(){
			while(isRunning){
				// 当队列中还有未处理的任务时，执行下载任务
				while(taskQueue.size()>0){
					// 获取第一个任务，并将之从任务队列中删除
					Task task = taskQueue.remove(0);
					// 将下载的图片添加到缓存
					task.bitmap = picUrlUtil.getBitmapPicByPath(task.url,task.path);
					caches.put(task.url, new SoftReference<Bitmap>(task.bitmap));
					if(handler!=null){
						// 创建消息对象，并将完成的任务添加到消息对象中
						Message msg = handler.obtainMessage();
						msg.obj = task;
						handler.sendMessage(msg);
					}
				}
				
				// 如果队列为空，则令线程等待
				synchronized(this){
					try{
						this.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	// 回调接口
	public interface ImageCallback{
		void loadImage(String path,Bitmap bitmap);
	}
	
	class Task{
		String url;   // 下载任务的下载路径
		String path=null;  // 图片存放路径
		Bitmap bitmap;  // 下载的图片
		ImageCallback callback;  // 回调对象
		
		public boolean equals(Object o){
			Task task = (Task)o;
			return task.url.equals(url);
		}
	}

}
