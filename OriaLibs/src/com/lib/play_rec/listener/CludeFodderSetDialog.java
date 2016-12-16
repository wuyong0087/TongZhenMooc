package com.lib.play_rec.listener;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.play_rec.R;
import com.lib.play_rec.rec.AtyRecord;

/**
 * 云素材操作界面
 * 
 * @author Administrator
 * 
 */
public class CludeFodderSetDialog extends Dialog implements OnClickListener {

//	private MaterialPDFDialog materialPDFDialog = null;
//	private MaterialPPTDialog materialPPTDialog = null;

	private AtyRecord context;

	private RelativeLayout PicLayout;
	private RelativeLayout PDFLayout;
	private RelativeLayout PPTLayout;
	private TextView cancelTv;

	public CludeFodderSetDialog(AtyRecord context) {
		super(context, R.style.GradeSetDialogTheme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clude_fodder_activity);
		// 初始化布局对象
		cancelTv = (TextView) findViewById(R.id.clude_cancel_tv);
		cancelTv.setOnClickListener(this);
		PicLayout = (RelativeLayout) findViewById(R.id.rl_pic_fodder);
		PicLayout.setOnClickListener(this);
		PDFLayout = (RelativeLayout) findViewById(R.id.rl_pdf_fodder);
		PDFLayout.setOnClickListener(this);
		PPTLayout = (RelativeLayout) findViewById(R.id.rl_ppt_fodder);
		PPTLayout.setOnClickListener(this);

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
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.clude_cancel_tv) {
			this.dismiss();
		} else if (id == R.id.rl_pic_fodder) {// 图片素材

		} else if (id == R.id.rl_pdf_fodder) {// PDF素材
			new MaterialPPTDialog(context, "doc","1").show();
			this.dismiss();
		} else if (id == R.id.rl_ppt_fodder) {// PPT素材
			new MaterialPPTDialog(context, "PPT","1").show();
			this.dismiss();
		}
	}

}
