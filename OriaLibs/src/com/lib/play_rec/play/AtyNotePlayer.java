package com.lib.play_rec.play;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.play_rec.BaseActivity;
import com.lib.play_rec.R;
import com.lib.play_rec.entity.Constant;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.WorksBean;
import com.lib.play_rec.utils.ObjectRWUtil;
import com.lib.play_rec.utils.ScreenManager;

public class AtyNotePlayer extends BaseActivity implements OnClickListener {

	private ImageView backIm;
	private TextView nameTv;
	private ImageView noteMoreIm;
	private ViewPager noteViewPager;
	private TextView noteNumTv;// 显示当前页面
	private TextView noteTitleTv;// 显示单曲笔记的标签
	private int pages;// 笔记的页数
	// private int pageNum;// 当前页
	// private NoteShareListener noteShare = null;
	private ArrayList<String> noteIamgeList;// 保存图片笔记集合
	private WorksBean noteBean;// 当前笔记对象
	private ImageView cursor;
	private LinearLayout llCursors;
	protected int startPage;
	private int tabWidth;
	private int currPager = 0;
	private Handler handler;

//	private NoteMoreListener noteShare;
	private int screenWidth;
	private int pageNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_player);
//		Constant.comeback4play = true;

		screenWidth = GlobalInit.getInstance().getScreenHeight();
		handler = new Handler();
		noteBean = (WorksBean) getIntent().getSerializableExtra("NOTE");

		backIm = (ImageView) findViewById(R.id.note_back);// 返回按钮
		nameTv = (TextView) findViewById(R.id.note_name_tv);// 笔记名字
		noteMoreIm = (ImageView) findViewById(R.id.note_more);// 更多操作
		noteNumTv = (TextView) findViewById(R.id.note_num);// 笔记分享
		noteTitleTv = (TextView) findViewById(R.id.note_marks);// 笔记标签
		noteViewPager = (ViewPager) findViewById(R.id.note_viewPager);
		backIm.setOnClickListener(this);
		noteMoreIm.setOnClickListener(this);

		noteIamgeList = getNoteIamge(noteBean);// 得到笔记的所有图片
		// 显示页码
//		noteNumTv.setText(resources.getString(R.string.note_play_pages) + 1 + "/" + pages);
		// 显示笔记名字
		if ("".equals(noteBean.getVideoName())) {
//			nameTv.setText(R.string.works_default_name);
		} else {
			nameTv.setText(noteBean.getVideoName());
		}
		// 显示笔记标签
//		noteTitleTv.setText(resources.getString(R.string.note_play_marks)
//				+ (noteBean.getVideoTags().equals("") ? resources.getString(R.string.note_play_nomark) : noteBean
//						.getVideoTags()));// 显示笔记标签

		initImageView();
//		NotePagerAdapter adapter = new NotePagerAdapter(this, noteViewPager);
//		adapter.change(noteIamgeList);
//		noteViewPager.setAdapter(adapter);
		noteViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pageNum) {
				llCursors.setVisibility(View.VISIBLE);
				AtyNotePlayer.this.pageNum = pageNum;
//				noteNumTv.setText(resources.getString(R.string.note_play_pages) + (pageNum + 1) + "/" + pages);
				Animation animation = null;

				if (pageNum >= currPager) {
					animation = new TranslateAnimation(tabWidth * currPager,
							tabWidth * (currPager + 1), 0, 0);
				} else {
					animation = new TranslateAnimation(
							tabWidth * (pageNum + 1), tabWidth * pageNum, 0, 0);
				}

				currPager = pageNum;
				animation.setFillAfter(true); // True:图片停在动画结束位置
				animation.setDuration(300); // 设置动画持续时间
				cursor.startAnimation(animation);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				llCursors.clearAnimation();
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_IDLE:
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							Animation animation = new AlphaAnimation(1, 0);
							animation.setDuration(200);
							llCursors.setAnimation(animation);
							llCursors.setVisibility(View.INVISIBLE);
						}
					}, 1000);
					break;
				}
			}
		});
	}

	/**
	 * 初始化动画
	 */
	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor_img);
		llCursors = (LinearLayout) findViewById(R.id.ll_scroll_line);
		llCursors.setVisibility(View.INVISIBLE);
		tabWidth = GlobalInit.getInstance().getScreenHeight() / pages;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = tabWidth;
		params.height = GlobalInit.getInstance().dip2px(4);
		cursor.setLayoutParams(params);
	}

	// 把笔记图片放入ArrayList集合
	private ArrayList<String> getNoteIamge(WorksBean bean) {
		String noteImage = bean.getNoteImage();
		if (noteImage == null || "".equals(noteImage)) {
			return null;
		}
		ArrayList<Integer> imagePagerList = new ArrayList<Integer>();
		ArrayList<String> arrayList = new ArrayList<String>();

		String[] split = noteImage.split(",");
		pages = split.length;
		if (pages <= 0) {
			return null;
		}
		for (int i = 0; i < split.length; i++) {
			String subSplit = (String) split[i].subSequence(0,
					split[i].indexOf("."));
			imagePagerList.add(Integer.parseInt(subSplit));
		}
		// 从新排序，以防显示混乱
		imagePagerList = ObjectRWUtil.getInstance().getSortList(imagePagerList);// 按大小重新给集合排序
		for (int j = 0; j < imagePagerList.size(); j++) {
			arrayList.add(GlobalInit.getInstance().getPersonalPath() // 分享成功后，这里有时会抛空指针
					+ bean.getVideoPath()
					+ File.separator
					+ imagePagerList.get(j) + ".pdt");
		}
		return arrayList;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id ==R.id.note_back) {
			thisOut();
		}else if (id ==R.id.note_more) {// 讲义的更多操作
			
//			if (noteShare == null) {
//				noteShare = new NoteMoreListener(this, screenWidth);
//			}
//			noteShare.showPopupWindow(v, pageNum, noteBean);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: // 回退键
			thisOut();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void thisOut() {
		Intent data = new Intent();
		data.putExtra("isNoteRecorde", true);
		AtyNotePlayer.this.setResult(RESULT_OK, data);
		ScreenManager.getInstance().popActivity(AtyNotePlayer.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
