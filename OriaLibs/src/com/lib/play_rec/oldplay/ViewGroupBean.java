package com.lib.play_rec.oldplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.oldplay.HistoryMemoryPlayer;
import com.lib.play_rec.oldplay.PlayView;
import com.lib.play_rec.oldplay.PlayViewGroup;

public class ViewGroupBean {

	// protected int backgroundOrder = 1; // 背景的序号，默认第一张
	public List<HistoryMemoryPlayer> historyList; // 保留的操作集合
	private List<HistoryMemoryPlayer> repealList; // 被回退的操作集合
	private PlayViewGroup viewGroup;
	private float[] move;
	public Map<Integer, View> viewMap;
	public List<View> viewOrderList;

	public ViewGroupBean(PlayViewGroup viewGroup) {
		this.viewGroup = viewGroup;
		viewMap = Collections.synchronizedMap(new HashMap<Integer, View>());
		viewOrderList = new ArrayList<View>();
		historyList = new ArrayList<HistoryMemoryPlayer>();
		repealList = new ArrayList<HistoryMemoryPlayer>();
	}

	public List<HistoryMemoryPlayer> getHistoryList() {
		return historyList;
	}

	public void drawPath(PlayView wacomView) {
		int type;
		for (int i = 0, j = historyList.size(); i < j; i++) {
			type = historyList.get(i).getOperaterType();
			if (type == Config.OPER_PEN || type == Config.OPER_RUBBER) {
				wacomView.drawPath(historyList.get(i));
			}
			if (type == Config.OPER_ARROW) {
				wacomView.drawAL(historyList.get(i));
			}
			if (type == Config.OPER_ELLIPSE) {
				wacomView.drawOval(historyList.get(i));
			}
			if (type == Config.OPER_RECT) {
				wacomView.drawRect(historyList.get(i));
			}
			if (type == Config.OPER_LINE) {
				wacomView.drawLine(historyList.get(i));
			}
		}
		wacomView.refreshCanvas();
	}

	// 添加操作
	public void create(HistoryMemoryPlayer memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
//		Log.e("historyList", "=======" + historyList.size());
	}

	// 增加到回退的操作集合
	public void addRepeal(HistoryMemoryPlayer memory) {
		repealList.add(memory);
	}

	// 回退操作
	public void undo(PlayView wacomView, boolean refresh) {
		if (historyList.size() >= 1) {
			HistoryMemoryPlayer memory = historyList
					.get(historyList.size() - 1);
//			Log.e("historyList", "=======" + historyList.size());
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
				for (int i = 0, j = historyList.size() - 1; i < j; i++) {
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
				memory.getMet().setTextStr(memory.getOldText(), refresh);
				memory.getMet().setSize(memory.getOldSize(), refresh);
				memory.getMet().setColor(memory.getOldColor(), refresh);
				memory.getMet().setViewWidthHeight(memory.getOldWidth(),
						memory.getOldHeight(), refresh);
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
			addRepeal(memory);
			// if (historyList.contains(memory)) {
			historyList.remove(memory);
			// }
		}
	}

	// 前进操作
	public void redo(PlayView wacomView, boolean refresh) {
		if (repealList.size() >= 1) {
			HistoryMemoryPlayer memory = repealList.get(repealList.size() - 1);
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
	public void remove(HistoryMemoryPlayer memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_DELETE) {
			historyList.add(memory);
		} else if (memory.getOperaterType() == Config.OPER_IMG_DELETE) {
			historyList.add(memory);
		}
	}

	/** 修改文本操作 */
	public void modifyText(HistoryMemoryPlayer memory) {
		historyList.add(memory);
	}

	/** 移动操作 */
	public void setMove(HistoryMemoryPlayer memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_MOVE) {
			historyList.add(memory);
		} else if (memory.getOperaterType() == Config.OPER_IMG_MOVE) {
			historyList.add(memory);
		}
	}

	/** 缩放图片 */
	public void scaleImage(HistoryMemoryPlayer memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
	}

	/** 旋转图片 */
	public void rotateImage(HistoryMemoryPlayer memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		historyList.add(memory);
	}

	/** 在回退操作后，如果有新的操作，而非前进操作，则清空撤销的操作记录 */
	public void clearRepeal() {
		repealList.clear();
	}

}
