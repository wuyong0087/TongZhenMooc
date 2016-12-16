package com.lib.play_rec.listener;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.play_rec.ProgressDialogView;
import com.lib.play_rec.R;
import com.lib.play_rec.adapter.CameraImageAdapter.ViewHolder;
import com.lib.play_rec.adapter.CustomPagerAdapter;
import com.lib.play_rec.adapter.PPTImageAdapter;
import com.lib.play_rec.adapter.RecommendAdapter;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.RecommendBean;
import com.lib.play_rec.net.HttpRequest;
import com.lib.play_rec.rec.AtyRecord;
import com.lib.play_rec.utils.CommUtil;
import com.lib.play_rec.view.CantScrollPager;

public class MaterialPPTDialog extends Dialog implements OnClickListener {

	private AtyRecord activity;
	private String materialType;
	private GridView imgpptGrid1, imgpptGrid2;
	private TextView personTv;

	private float density = 1f;
	private RecommendAdapter allImgAdapter; // 图片展示适配器
	private PPTImageAdapter pptImgAdapter; // 图片展示适配器
	private List<RecommendBean> allImgList, imgList; // 图片集合
	private int imgItemW = 0, imgItemH = 0; // 图片GridView每项的宽高
	private int IMGNUM = 4; // 图片每行显示的条数

	private int imagePosition = 0;

	private HttpRequest httpRequest;

	/** Handler操作标示 */
	private final int UPDATEEMPTY = 1;
	private final int UPDATEIMG = 2;
	private final int UPDATEFAIL = 3;
	private final int UPDATEEMPTY2 = 4;
	private final int UPDATEIMG2 = 5;
	private final int UPDATEIMG3 = 6;
	private final int UPDATEIMG4 = 7;

	private LinearLayout cancel, finish;
	private ProgressDialogView progress;

	protected RecommendBean bean, bean2;

	private CantScrollPager viewPager;
	private CustomPagerAdapter pagerAdapter;
	private List<View> viewList;
	private GridView personalRefreshView, personalRefreshView2;
	private List<RecommendBean> imaglist, imaglist2;
	private boolean isClick = false;

//	需要用户ID
	private String uid = "";
	public MaterialPPTDialog(AtyRecord activity, String materialType,String uid) {
		super(activity, R.style.RecommendDialogTheme);
		this.activity = activity;
		this.materialType = materialType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.materialppt_dialog);
		this.setCanceledOnTouchOutside(false);

		density = GlobalInit.getInstance().getScreenDensity();
		int w = GlobalInit.getInstance().getScreenWidth();
		int h = GlobalInit.getInstance().getScreenHeight();
		imgItemW = (int) ((w - (IMGNUM - 1) * 10 * density) / IMGNUM);
		imgItemH = (int) (h / (float) w * imgItemW);

		httpRequest = HttpRequest.getInstance();

		// 检查是否有网络
		if (!CommUtil.isNetworkAvailable(activity)) {
			Toast.makeText(activity, R.string.not_network, Toast.LENGTH_SHORT)
					.show();
		} else {
			progress = new ProgressDialogView(activity, R.string.image_load);
			progress.show();
			downpptThread.start();
		}

		personTv = (TextView) findViewById(R.id.textView1_person);
		initPersonTv();

		cancel = (LinearLayout) findViewById(R.id.mat_ppt_cancel);
		cancel.setOnClickListener(this);
		finish = (LinearLayout) findViewById(R.id.mat_ppt_finish);
		finish.setOnClickListener(this);

		viewPager = (CantScrollPager) findViewById(R.id.view_pager_ppt);
		initPPTImage();
		pagerAdapter = new CustomPagerAdapter();
		pagerAdapter.setViewList(viewList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	private void initPersonTv() {
		if ("PPT".equals(materialType)) {
			personTv.setText("PPT资源");
		} else if ("doc".equals(materialType)) {
			personTv.setText("PDF资源");
		}
	}

	private void initPPTImage() {
		setScrollListner();
		viewList = new ArrayList<View>();
		final LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.pptimage1, null);
		final View v2 = inflater.inflate(R.layout.pptimage2, null);
//		personalRefreshView = (PullToRefreshGridView) v
//				.findViewById(R.id.ppt_image1);
//		imgpptGrid1 = personalRefreshView.getRefreshableView();
		allImgList = new ArrayList<RecommendBean>();
		allImgAdapter = new RecommendAdapter(activity, allImgList, imgpptGrid1,
				imgItemW, imgItemH);
		imgpptGrid1.setNumColumns(IMGNUM);
		imgpptGrid1.setAdapter(allImgAdapter);
		imgpptGrid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				isClick = true;
				RecommendBean bean = imaglist.get(position);
				getPPTTmage(inflater, v2, bean.getId());
			}
		});
		viewList.add(v);
		viewList.add(v2);
	}

	private void setScrollListner() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_IDLE:
					viewPager.setCanScroll(true);
					break;
				case ViewPager.SCROLL_STATE_DRAGGING:
					viewPager.setCanScroll(false);
					break;
				}
			}
		});
	}

	private void getPPTTmage(LayoutInflater inflater, View v, int id) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();
			}
		});
		initPPTImage_Two(id);
		viewPager.setCurrentItem(1);
//		personalRefreshView2 = (PullToRefreshGridView) v
//				.findViewById(R.id.ppt_image2);
//		imgpptGrid2 = personalRefreshView2.getRefreshableView();
		imgList = new ArrayList<RecommendBean>();
		pptImgAdapter = new PPTImageAdapter(activity, imgList, imgpptGrid2,
				imgItemW, imgItemH);
		imgpptGrid2.setAdapter(pptImgAdapter);
		imgpptGrid2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				RecommendBean bean = imgList.get(position);
				CheckBox cb = ((ViewHolder) v.getTag()).delCB;
				if (cb.isChecked()) {
					pptImgAdapter.getSelMap().put(position, true);
					cb.setChecked(false);
					bean.setCheck(false);
				} else {
					pptImgAdapter.getSelMap().put(position, false);
					cb.setChecked(true);
					bean.setCheck(true);
				}
				return;
			}
		});
	}

	private void initPPTImage_Two(int id) {
		imagePosition = id;
		new Thread(new DownpptRunnable()).start();
	}

	@Override
	public void onClick(View v) {
		// if (!UIWorkUtil.isSingleClick(v)) {
		// return;
		// }
		int id = v.getId();
		if (id == R.id.mat_ppt_cancel) {
			isClick = false;
			if (viewPager.getCurrentItem() == 1) {
				viewPager.setCurrentItem(0);
			} else {
				MaterialPPTDialog.this.dismiss();
			}
		} else if (id == R.id.mat_ppt_finish) {
			if (isClick) {
				List<RecommendBean> selCBPosList = pptImgAdapter
						.getSelCBPosList();
				AtyRecord.isImports = true;
				activity.loadRecommends(selCBPosList);
				MaterialPPTDialog.this.dismiss();
			} else {
				Toast.makeText(activity, R.string.select_image,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 获取PPT资源
	 */
	Thread downpptThread = new Thread() {
		@Override
		public void run() {
			handler.sendEmptyMessage(UPDATEEMPTY); // 清空当前界面的图片
			try {
				String resultStr = httpRequest
						.getPPTMaterial(uid, materialType);
				if (resultStr != null) {
					JSONObject obj = new JSONObject(resultStr);
					String result = obj.getString("result");
					if ("1".equals(result)) {
						imaglist = new ArrayList<RecommendBean>();
						JSONArray dataArr = obj.getJSONArray("data");
						for (int i = 0, j = dataArr.length(); i < j; i++) {
							JSONObject recommendObj = dataArr.getJSONObject(i);
							bean = new RecommendBean();
							bean.setId(recommendObj.getInt("id"));
							bean.setSmallImgUrl(recommendObj.getString("simg"));
							bean.setCommImgUrl(recommendObj.getString("img"));
							imaglist.add(bean);
						}
					}
				}
				if (imaglist != null && imaglist.size() > 0) {
					allImgList = imaglist;
					handler.sendEmptyMessage(UPDATEIMG);
				} else {
					handler.sendEmptyMessage(UPDATEIMG4);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(UPDATEFAIL);
			}
		}
	};

	private class DownpptRunnable implements Runnable {
		@Override
		public void run() {
			handler.sendEmptyMessage(UPDATEEMPTY2); // 清空当前界面的图片
			try {
				int position = imagePosition;
				String resultStr = httpRequest.getPPTImageMaterial(position);
				if (resultStr != null) {
					JSONObject obj = new JSONObject(resultStr);
					String result = obj.getString("result");
					if ("1".equals(result)) {
						imaglist2 = new ArrayList<RecommendBean>();
						JSONArray dataArr = obj.getJSONArray("data");
						for (int i = 0, j = dataArr.length(); i < j; i++) {
							JSONObject recommendObj = dataArr.getJSONObject(i);
							bean2 = new RecommendBean();
							bean2.setSmallImgUrl(recommendObj.getString("simg"));
							bean2.setCommImgUrl(recommendObj.getString("img"));
							imaglist2.add(bean2);
						}
					}
				}
				if (imaglist2 != null && imaglist2.size() > 0) {
					imgList = imaglist2;
					handler.sendEmptyMessage(UPDATEIMG2);
				} else {
					handler.sendEmptyMessage(UPDATEIMG3);
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(UPDATEFAIL);
				e.printStackTrace();
			}
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEEMPTY:
				allImgAdapter.setList(new ArrayList<RecommendBean>());
				allImgAdapter.notifyDataSetInvalidated();
				break;
			case UPDATEEMPTY2:
				pptImgAdapter.setList(new ArrayList<RecommendBean>());
				pptImgAdapter.notifyDataSetInvalidated();
				break;
			case UPDATEIMG:
				progress.dismiss();
				allImgAdapter.setList(allImgList);
				allImgAdapter.notifyDataSetInvalidated();
				break;
			case UPDATEIMG2:
				progress.dismiss();
				pptImgAdapter.setList(imgList);
				pptImgAdapter.notifyDataSetInvalidated();
				break;
			case UPDATEIMG3:
				progress.dismiss();
				Toast.makeText(activity, R.string.no_material,
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATEIMG4:
				progress.dismiss();
				Toast.makeText(activity, R.string.no_material,
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATEFAIL:
				Toast.makeText(activity, R.string.Image_load_fail,
						Toast.LENGTH_SHORT).show();
				progress.dismiss();
				break;
			}
		}
	};

}
