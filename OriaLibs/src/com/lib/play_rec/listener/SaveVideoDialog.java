package com.lib.play_rec.listener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.WorksBean;
import com.lib.play_rec.rec.AtyRecord;
import com.lib.play_rec.rec.JsonOperater;

public class SaveVideoDialog extends Dialog implements OnClickListener {

	private Activity context;
	private TextView cancelTv, confirmTv; // 确认/取消按钮
	// private SelectBoxView firstSelLayout, secondSelLayout; // 知识点选择框
	private EditText titleEt, tagEt; // 标题输入框
	// private List<KnowledgeBean> gradeList, secondList;
	// private TextView giveupBtn; // 放弃按钮

	// private KnowSetListener knowSetListener;
	private int gradePos = -1
//			, subjectPos = -1
			; // 知识点选择对应集合的序号
	private String permission = "1"; // 默认权限公开(0-公开，1-私有)
	private InputMethodManager manager;
	private boolean worksDraft;// true录制视频 false 录制笔记
	private TextView saveTitleTv;
	// private SelectBoxView yearSelLayout;
	private WorksBean worksBean;
	private GlobalInit globalInit;

	/**
	 * 保存界面构造函数
	 * 
	 * @param context
	 *            是否是保存讲义
	 * @param worksBean
	 *            即将要被保存的坐标的数据封装对象
	 */
	public SaveVideoDialog(Activity context, boolean videoDraft,
			WorksBean worksBean) {
		super(context, R.style.EditVideoDialogTheme);// GradeSetDialogTheme
		this.context = context;
		this.worksDraft = videoDraft;
		this.worksBean = worksBean;
		globalInit = GlobalInit.getInstance(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int w = globalInit.getScreenWidth();
		int h = globalInit.getScreenHeight();
		View view = View.inflate(context, R.layout.savevideo_dialog, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				w * 4 / 7, h * 85 / 100);
		this.setContentView(view, params);
		this.setCanceledOnTouchOutside(false);
		this.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
		manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		cancelTv = (TextView) findViewById(R.id.save_cancel_btn);
		confirmTv = (TextView) findViewById(R.id.save_confirm_btn);
		// yearSelLayout = (SelectBoxView)
		// findViewById(R.id.year_period_layout);
		// firstSelLayout = (SelectBoxView) findViewById(R.id.first_sel_layout);
		// secondSelLayout = (SelectBoxView)
		// findViewById(R.id.second_sel_layout);
		// gradeSelLayout = (SelectBoxView) findViewById(R.id.grade_sel_layout);
		// giveupBtn = (TextView) findViewById(R.id.giveup_btn);
		titleEt = (EditText) findViewById(R.id.save_title_et);
		tagEt = (EditText) findViewById(R.id.save_tag_et);

		if (worksBean != null) {
			titleEt.setText(worksBean.getVideoName());
			tagEt.setText(worksBean.getVideoTags());
		}

		saveTitleTv = (TextView) findViewById(R.id.save_title);
		if (worksDraft) {
			saveTitleTv.setText(R.string.save_drafts);
		}

		// firstSelLayout.setText(getContext().getString(R.string.topic_1));
		// firstSelLayout.setImgResouce(R.drawable.select_down);
		// secondSelLayout.setText(getContext().getString(R.string.topic_2));
		// secondSelLayout.setImgResouce(R.drawable.select_down);

		// firstList = ObjectRWUtil.getInstance().getKnowledgeMap()
		// .get(globalInit.getVideoBean().getSubjectId());

		// gradeList = ObjectRWUtil.getInstance().getGradeList();
		// knowSetListener = new KnowSetListener(context);

		// yearSelLayout.setImgResouce(R.drawable.select_down);
		// gradeSelLayout.setImgResouce(R.drawable.select_down);
		// yearSelLayout.setText(getContext().getString(R.string.topic_1));
		// gradeSelLayout.setText(getContext().getString(R.string.topic_2));

		cancelTv.setOnClickListener(this);
		confirmTv.setOnClickListener(this);
		// yearSelLayout.setOnClickListener(this);
		// gradeSelLayout.setOnClickListener(this);
		// firstSelLayout.setOnClickListener(this);
		// secondSelLayout.setOnClickListener(this);
		// giveupBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.save_cancel_btn) {
			this.dismiss();
		} else if (id == R.id.save_confirm_btn) {
			// 判断用户是否已经点击过录制
			if (JsonOperater.getInstance().getSumTime() <= 0
					&& worksDraft == false) {
				Toast.makeText(context, R.string.give_up, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// 进行视频保存
			String title = titleEt.getText().toString().trim();
			String tag = tagEt.getText().toString().trim();
			WorksBean vb = globalInit.getVideoBean();
			vb.setGradeId("118");
			if ("".equals(title)) {
				title = getContext().getString(R.string.works_default_name);
			}
			vb.setVideoName(title);
			vb.setVideoTags(tag);
			vb.setVideoDesc("");
			vb.setPermission(permission);
			
			this.dismiss();
			((AtyRecord)context).saveRecord(worksDraft);
		} else if (id == R.id.year_period_layout) {
			// 隐藏键盘
			manager.hideSoftInputFromWindow(titleEt.getWindowToken(), 0);
			// if (gradeList != null) {
			// yearSelLayout.setImgResouce(R.drawable.select_up);
			// knowSetListener.showPopupWindow(yearSelLayout, this, gradeList,
			// 1);
			// } else {
			// Toast.makeText(context, R.string.no_topic, Toast.LENGTH_SHORT)
			// .show();
			// }
		}
		// case R.id.grade_sel_layout:
		// manager.hideSoftInputFromWindow(titleEt.getWindowToken(), 0);
		// if (gradePos != -1) {
		// secondList = gradeList.get(gradePos).getList();
		// gradeSelLayout.setImgResouce(R.drawable.select_up);
		// knowSetListener.showPopupWindow(gradeSelLayout, this,
		// secondList, 2);
		// }
		// break;
		// case R.id.first_sel_layout:
		// // 隐藏键盘
		// manager.hideSoftInputFromWindow(titleEt.getWindowToken(), 0);
		// if (firstList != null) {
		// firstSelLayout.setImgResouce(R.drawable.select_up);
		// knowSetListener.showPopupWindow(firstSelLayout, this,
		// firstList, 1);
		// } else {
		// Toast.makeText(context, R.string.no_topic, Toast.LENGTH_SHORT)
		// .show();
		// }
		// break;
		// case R.id.second_sel_layout:
		// manager.hideSoftInputFromWindow(titleEt.getWindowToken(), 0);
		// if (firstSelPosition != -1) {
		// secondList = firstList.get(firstSelPosition).getList();
		// secondSelLayout.setImgResouce(R.drawable.select_up);
		// knowSetListener.showPopupWindow(secondSelLayout, this,
		// secondList, 2);
		// }
		// break;
		// case R.id.giveup_btn: // 放弃视频
		// CommUtil.delFile(JsonOperater.getInstance().getVideoFullPath(),
		// true);
		// context.finish();
		// break;
	}

	public void setFirstSelPosition(int gradePos) {
		if (this.gradePos != gradePos) {
			this.gradePos = gradePos;
			// yearSelLayout.setText(gradeList.get(gradePos).getName());
//			subjectPos = -1;
			// gradeSelLayout.setText(getContext().getString(R.string.topic_2));
		}
	}

	public void setSecondSelPosition(int subjectPos) {
//		this.subjectPos = subjectPos;
		// gradeSelLayout.setText(secondList.get(subjectPos).getName());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

}
