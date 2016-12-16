package com.lib.play_rec.entity;

import android.view.View;

import com.lib.play_rec.play.PlayEditText;
import com.lib.play_rec.play.PlayHistoryMemory;
import com.lib.play_rec.play.PlayView;
import com.lib.play_rec.play.PlayViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGroupBean {

	// protected int backgroundOrder = 1; // 背景的序号，默认第一张
	private List<PlayHistoryMemory> historyList; // 保留的操作集合
	private List<PlayHistoryMemory> repealList; // 被回退的操作集合
	// 容器控件
	private PlayViewGroup viewGroup;
	// 移动的集合
	private float[] move;
	// 录制控件集合
	public Map<Integer, View> viewMap;
	// 录制控件集合
	public List<View> viewOrderList;

	// 操作的ID标识,用于保存那些没有MID的操作
	// private int negtiveMid;

	public ViewGroupBean(PlayViewGroup viewGroup) {
		this.viewGroup = viewGroup;
		viewMap = Collections.synchronizedMap(new HashMap<Integer, View>());
		viewOrderList = new ArrayList<View>();
		historyList = new ArrayList<PlayHistoryMemory>();
		repealList = new ArrayList<PlayHistoryMemory>();
	}

	public List<PlayHistoryMemory> getHistoryList() {
		return historyList;
	}

	public void drawPath(PlayView wacomView) {
		int order;
		for (int i = 0, j = historyList.size(); i < j; i++) {
			order = historyList.get(i).getOperaterType();
			if (order == Config.OPER_PEN
			// || order == Config.OPER_RUBBER
			) {
				wacomView.drawPath(historyList.get(i));
			}
			if (order == Config.OPER_ARROW) {
				wacomView.drawAL(historyList.get(i));
			}
			if (order == Config.OPER_ELLIPSE) {
				wacomView.drawOval(historyList.get(i));
			}
			if (order == Config.OPER_RECT) {
				wacomView.drawRect(historyList.get(i));
			}
			if (order == Config.OPER_LINE) {
				wacomView.drawLine(historyList.get(i));
			}
		}
		wacomView.refreshCanvas();
	}

	// 添加操作
	public void addMemory(PlayHistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
	}

	// 增加到回退的操作集合
	public void addRepeal(PlayHistoryMemory memory) {
		if (
//				TODO 注释掉之后，未录制时的redo操作无效
//				memory.getOperaterType() != Config.OPER_PEN&&
				memory.getOperaterType() != Config.OPER_RUBBER) {
			repealList.add(memory);
			historyList.remove(memory);
		}
	}

	// 增加到回退的操作集合
	public void removeRepeal(PlayHistoryMemory memory) {
		if (
//				TODO 注释掉之后，未录制时的undo操作无效
//				memory.getOperaterType() != Config.OPER_PEN&&
				memory.getOperaterType() != Config.OPER_RUBBER) {
			repealList.remove(memory);
			historyList.add(memory);
		}
	}

	/**
	 * 撤销画笔，橡皮擦
	 * 
	 * @param wacomView
	 * @param refresh
	 * @param mid
	 */
	public void undoPen_Rubber(PlayView wacomView, boolean refresh, int... mid) {
		if (mid.length > 0) {
			for (PlayHistoryMemory memory : historyList) {
				if (memory.getMid() == mid[0] && mid[0] > -2) {
					addRepeal(memory);
					break;
				}
			}
		}
		// 清空画布
		wacomView.clearCanvas();
		int order = 0;
		PlayHistoryMemory memory = null;
		// 循环绘制线条
		for (int i = 0, j = historyList.size(); i < j; i++) {
			memory = historyList.get(i);
			order = memory.getOperaterType();
			if (order == Config.OPER_PEN) {
				wacomView.drawPath(memory);
			} else if (order == Config.OPER_ARROW) {
				wacomView.drawAL(memory);
			} else if (order == Config.OPER_ELLIPSE) {
				wacomView.drawOval(memory);
			} else if (order == Config.OPER_RECT) {
				wacomView.drawRect(memory);
			} else if (order == Config.OPER_LINE) {
				wacomView.drawLine(memory);
			} else if (order == Config.OPER_RUBBER) {
				wacomView.rubberDraw(memory.getIds(), true);
			}
		}
		// 刷新画布
		if (refresh) {
			wacomView.refreshCanvas();
		}
	}

	/**
	 * 重置画笔相关
	 * 
	 * @param wacomView
	 *            执行渲染操作的控件
	 * @param refresh
	 *            是否刷新视图，暂停时，操作不进行刷新
	 * @param mid
	 *            执行指令的mid
	 */
	public void redoPen_Rubber(PlayView wacomView, boolean refresh, int... mid) {
		if (repealList.size() > 0) {
			PlayHistoryMemory memory = null;
			if (mid.length > 0) {
				for (PlayHistoryMemory hist : repealList) {
					if (hist.getMid() == mid[0] && mid[0] > -2) {
						historyList.add(memory);
						repealList.remove(memory);
						memory = hist;
						break;
					}
				}
			}
			switch (memory.getOperaterType()) {
			case Config.OPER_PEN: // 回退画线操作
				// 绘制线条
				wacomView.drawPath(memory);
				// 刷新画布
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_RUBBER: // 回退橡皮擦操作
				wacomView.rubberDraw(memory.getIds(), false);
				break;
			case Config.OPER_ARROW:// 绘制箭头
				wacomView.drawAL(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_ELLIPSE:// 绘制椭圆
				wacomView.drawOval(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_RECT:
				wacomView.drawRect(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_LINE:
				wacomView.drawLine(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			}
		}
	}

	public void undoTextAdd(boolean refresh, int mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : historyList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_TEXT_ADD) {
					memory = repeat;
				}
			}
			if (memory != null) {
				viewGroup.delEditText(memory.getMet(), refresh, false);
				addRepeal(memory);
			}
		}
	}

	public void redoTextAdd(boolean refresh, int mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_TEXT_ADD) {
					memory = repeat;
				}
			}
			if (memory != null) {
				viewGroup.addEditText(memory.getMet(), refresh);
				removeRepeal(memory);
			}
		}
	}

	public void undoEditText(PlayHistoryMemory memory, boolean refresh) {
		if (memory != null) {
			memory.getMet().setTextStr(memory.getOldText(), refresh);
			memory.getMet().setSize(memory.getOldSize(), refresh);
			memory.getMet().setColor(memory.getOldColor(), refresh);
			memory.getMet().setViewWidthHeight(memory.getOldWidth(),
					memory.getOldHeight(), refresh);
		}
	}

	public void redoTextEdit(PlayHistoryMemory memory, boolean refresh) {
		if (memory != null) {
			memory.getMet().setTextStr(memory.getOldText(), refresh);
			memory.getMet().setSize(memory.getOldSize(), refresh);
			memory.getMet().setColor(memory.getOldColor(), refresh);
			memory.getMet().setViewWidthHeight(memory.getOldWidth(),
					memory.getOldHeight(), refresh);
		}
	}

	public void undoTextMove(PlayHistoryMemory memory, boolean refresh) {
		move = memory.getMoveList().get(0);
		memory.getMet().setLayoutByMid(move[0], move[1], refresh);
	}

	public void redoTextMove(PlayHistoryMemory memory, boolean refresh) {
		move = memory.getMoveList().get(0);
		memory.getMet().setLayoutByMid(move[0], move[1], refresh);
	}

	public void undoTextDelete(boolean refresh, int mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : historyList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_TEXT_DELETE) {
					memory = repeat;
					break;
				}
			}
			if (memory != null) {
				viewGroup.addEditText(memory.getMet(), refresh);
				addRepeal(memory);
			}
		}
	}

	public void redoTextDelete(boolean refresh, int mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_TEXT_DELETE) {
					memory = repeat;
					break;
				}
			}
			if (memory != null) {
				viewGroup.addEditText(memory.getMet(), refresh);
				removeRepeal(memory);
			}
		}
	}

	/* 图片相关 */
	public void undoImgAdd(boolean refresh, int mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : historyList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_IMG_ADD) {
					memory = repeat;
				}
			}
			if (memory != null) {
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				addRepeal(memory);
			}
		}
	}

	public void redoImgAdd(boolean refresh, int mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_IMG_ADD) {
					memory = repeat;
				}
			}
			if (memory != null) {
				viewGroup.addImageView(memory.getMimg(), refresh);
				removeRepeal(memory);
			}
		}
	}

	public void undoImgDelete(boolean refresh, int mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : historyList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_IMG_DELETE) {
					memory = repeat;
					break;
				}
			}
			if (memory != null) {
				viewGroup.addImageView(memory.getMimg(), refresh);
				addRepeal(memory);
			}
		}
	}

	public void redoImgDelete(boolean refresh, int mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid
						&& repeat.getOperaterType() == Config.OPER_IMG_DELETE) {
					memory = repeat;
					break;
				}
			}
			if (memory != null) {
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				addRepeal(memory);
			}
		}
	}

	public void undoImgMove(PlayHistoryMemory memory, boolean refresh) {
		move = memory.getMoveList().get(0);
		memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
	}

	public void redoImgMove(PlayHistoryMemory memory, boolean refresh) {
		move = memory.getMoveList().get(0);
		memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
	}

	public void redoImgScale(PlayHistoryMemory memory, boolean refresh) {
		float scale = memory.getScaleList().get(0);
		memory.getMimg().scaleImageForUnReDo(scale, refresh);
	}

	public void undoImgScale(PlayHistoryMemory memory, boolean refresh) {
		float scale = memory.getScaleList().get(0);
		memory.getMimg().scaleImageForUnReDo(scale, refresh);
	}

	public void undoImgRotate(PlayHistoryMemory memory, boolean refresh) {
		float rotate = memory.getRotateList().get(0);
		memory.getMimg().rotateImage(-rotate, false);
	}

	public void redoImgRotate(PlayHistoryMemory memory, boolean refresh) {
		float rotate = memory.getRotateList().get(0);
		memory.getMimg().rotateImage(-rotate, false);
	}

	/**
	 * Undo图片
	 * 
	 * @param refresh
	 * @param mid
	 */
	public void undoImage(boolean refresh, int... mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid[0]) {
					memory = repeat;
				}
			}
			if (memory == null) {
				return;
			}
			switch (memory.getOperaterType()) {
			case Config.OPER_IMG_ADD:
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.addImageView(memory.getMimg(), refresh);
				break;
			case Config.OPER_IMG_MOVE:
				move = memory.getMoveList().get(0);
				memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale / scaleList.get(i);
				}
				memory.getMimg().scaleImageForUnReDo(scale, refresh);
				break;
			case Config.OPER_IMG_ROTATE:
				List<Float> rotateList = memory.getRotateList();
				float rotate = 1;
				for (int i = 0, j = rotateList.size(); i < j; i++) {
					rotate = rotateList.get(i);
					memory.getMimg().rotateImage(-rotate, false);
				}
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(!memory.isLock());
				return;
			}
		}
	}

	/**
	 * Redo图片
	 * 
	 * @param refresh
	 * @param mid
	 */
	public void redoImage(boolean refresh, int... mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			for (PlayHistoryMemory repeat : repealList) {
				if (repeat.getMid() == mid[0]) {
					memory = repeat;
				}
			}
			if (memory == null) {
				return;
			}
			switch (memory.getOperaterType()) {
			case Config.OPER_IMG_ADD:
				viewGroup.addImageView(memory.getMimg(), refresh);
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				break;
			case Config.OPER_IMG_MOVE:
				move = memory.getMoveList()
						.get(memory.getMoveList().size() - 1);
				memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale * scaleList.get(i);
				}
				memory.getMimg().scaleImageForUnReDo(scale, refresh);
				break;
			case Config.OPER_IMG_ROTATE:
				List<Float> rotateList = memory.getRotateList();
				float rotate = 1;
				for (int i = 0, j = rotateList.size(); i < j; i++) {
					rotate = rotateList.get(i);
				}
				memory.getMimg().rotateImage(rotate, false);
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(memory.isLock());
				return;
			}
		}
	}

	// 回退操作
	public void undo(PlayView wacomView, boolean refresh, int... mid) {
		if (historyList.size() >= 1) {
			PlayHistoryMemory memory = null;
			if (mid.length > 0) {
				for (PlayHistoryMemory hist : historyList) {
					if (hist.getMid() == mid[0] && mid[0] > -2) {
						memory = hist;
						addRepeal(memory);
						break;
					}
				}
			}
			if (memory == null) {
				return;
			}
			switch (memory.getOperaterType()) {
			case Config.OPER_PEN: // 回退画线操作
			case Config.OPER_RUBBER: // 回退橡皮擦操作
			case Config.OPER_ARROW: // 回退箭头操作
			case Config.OPER_ELLIPSE: // 回退椭圆操作
			case Config.OPER_RECT:
			case Config.OPER_LINE:
				// historyList.remove(memory);
				// 清空画布
				wacomView.clearCanvas();
				// 循环绘制线条
				for (int i = 0, j = historyList.size(); i < j; i++) {
					if (historyList.get(i).getOperaterType() == Config.OPER_PEN
							|| historyList.get(i).getOperaterType() == Config.OPER_RUBBER) {
						wacomView.drawPath(historyList.get(i));
					} else if (historyList.get(i).getOperaterType() == Config.OPER_ARROW) {
						wacomView.drawAL(historyList.get(i));
					} else if (historyList.get(i).getOperaterType() == Config.OPER_ELLIPSE) {
						wacomView.drawOval(historyList.get(i));
					} else if (historyList.get(i).getOperaterType() == Config.OPER_RECT) {
						wacomView.drawRect(historyList.get(i));
					} else if (historyList.get(i).getOperaterType() == Config.OPER_LINE) {
						wacomView.drawLine(historyList.get(i));
					}
				}
				// 刷新画布
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			// ----------------以下是文本操作相关---------------------------------
			case Config.OPER_TEXT_ADD:
				viewGroup.delEditText(memory.getMet(), refresh, false);
				break;
			case Config.OPER_TEXT_EDIT:
				PlayEditText met = memory.getMet();
				memory.getMet().setTextStr(met.getTextStr(), refresh);
				memory.getMet().setSize(met.getSize(), refresh);
				memory.getMet().setColor(met.getColor(), refresh);
				memory.getMet().setViewWidthHeight(met.getWidth(),
						met.getHeight(), refresh);
				break;
			case Config.OPER_TEXT_LOCK:
				memory.getMet().setLock(!memory.isLock());
				return;
			case Config.OPER_TEXT_DELETE:
				viewGroup.addEditText(memory.getMet(), refresh);
				break;
			case Config.OPER_TEXT_MOVE:
				move = memory.getMoveList().get(0);
				memory.getMet().setLayoutByMid((int) move[0], (int) move[1],
						refresh);
				break;

			// ---------------以下是图片操作相关---------------------------------------
			case Config.OPER_IMG_ADD:
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.addImageView(memory.getMimg(), refresh);
				break;
			case Config.OPER_IMG_MOVE:
				move = memory.getMoveList().get(0);
				memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale / scaleList.get(i);
				}
				memory.getMimg().scaleImageForUnReDo(scale, refresh);
				break;
			case Config.OPER_IMG_ROTATE:
				List<Float> rotateList = memory.getRotateList();
				float rotate = 1;
				for (int i = 0, j = rotateList.size(); i < j; i++) {
					rotate = rotateList.get(i);
					memory.getMimg().rotateImage(-rotate, false);
				}
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(!memory.isLock());
				return;
			}
		}
	}

	// 前进操作
	public void redo(PlayView wacomView, boolean refresh, int... mid) {
		if (repealList.size() >= 1) {
			PlayHistoryMemory memory = null;
			if (mid.length > 0) {
				for (PlayHistoryMemory hist : repealList) {
					if (hist.getMid() == mid[0] && mid[0] > -2) {
						memory = hist;
						break;
					}
				}
			}
			if (memory == null) {
				return;
			}
			switch (memory.getOperaterType()) {
			case Config.OPER_PEN: // 回退画线操作
			case Config.OPER_RUBBER: // 回退橡皮擦操作
				// 绘制线条
				wacomView.drawPath(memory);
				// 刷新画布
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_ARROW:// 绘制箭头
				wacomView.drawAL(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_ELLIPSE:// 绘制椭圆
				wacomView.drawOval(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_RECT:
				wacomView.drawRect(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			case Config.OPER_LINE:
				wacomView.drawLine(memory);
				if (refresh) {
					wacomView.refreshCanvas();
				}
				break;
			// ----------------以下是文本操作相关------------------------------------
			case Config.OPER_TEXT_ADD:
				viewGroup.addEditText(memory.getMet(), refresh);
				break;
			case Config.OPER_TEXT_EDIT:
				memory.getMet().setTextStr(memory.getNewText(), refresh);
				memory.getMet().setSize(memory.getNewSize(), refresh);
				memory.getMet().setColor(memory.getNewColor(), refresh);
				memory.getMet().setViewWidthHeight(memory.getNewWidth(),
						memory.getNewHeight(), refresh);
				break;
			case Config.OPER_TEXT_LOCK:
				memory.getMet().setLock(memory.isLock());
				return;
			case Config.OPER_TEXT_DELETE:
				viewGroup.delEditText(memory.getMet(), refresh, false);
				break;
			case Config.OPER_TEXT_MOVE:
				float[] move = memory.getMoveList().get(
						memory.getMoveList().size() - 1);
				memory.getMet().setLayoutByMid((int) move[0], (int) move[1],
						refresh);
				break;

			// ---------------以下是图片操作相关----------------------------------------
			case Config.OPER_IMG_ADD:
				viewGroup.addImageView(memory.getMimg(), refresh);
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.delImageView(memory.getMimg(), refresh, false);
				break;
			case Config.OPER_IMG_MOVE:
				List<float[]> l = memory.getMoveList();
				int ss = l.size();
				System.out.println(ss);
				move = memory.getMoveList()
						.get(memory.getMoveList().size() - 1);
				memory.getMimg().setLayoutByMid(move[0], move[1], refresh);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale * scaleList.get(i);
				}
				memory.getMimg().scaleImageForUnReDo(scale, refresh);
				break;
			case Config.OPER_IMG_ROTATE:
				List<Float> rotateList = memory.getRotateList();
				float rotate = 1;
				for (int i = 0, j = rotateList.size(); i < j; i++) {
					rotate = rotateList.get(i);
				}
				memory.getMimg().rotateImage(rotate, false);
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(memory.isLock());
				return;
			}
			historyList.add(memory);
			repealList.remove(memory);
		}
	}

	/** 删除对象操作 */
	public void remove(PlayHistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_DELETE) {
			historyList.add(memory);
		} else if (memory.getOperaterType() == Config.OPER_IMG_DELETE) {
			historyList.add(memory);
		}
	}

	/** 修改文本操作 */
	public void modifyText(PlayHistoryMemory memory) {
		historyList.add(memory);
	}

	/** 移动操作 */
	public void setMove(PlayHistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_MOVE) {
			historyList.add(memory);
		} else if (memory.getOperaterType() == Config.OPER_IMG_MOVE) {
			historyList.add(memory);
		}
	}

	/** 缩放图片 */
	public void scaleImage(PlayHistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
	}

	/** 旋转图片 */
	public void rotateImage(PlayHistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
	}

	/** 在回退操作后，如果有新的操作，而非前进操作，则清空撤销的操作记录 */
	public void clearRepeal() {
		repealList.clear();
	}

}
