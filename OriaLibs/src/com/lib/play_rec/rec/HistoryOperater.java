package com.lib.play_rec.rec;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史操作类
 * @author Administrator
 */
public class HistoryOperater {

	public ArrayList<HistoryMemory> repeatList; // 被回退的操作集合
	private ArrayList<HistoryMemory> historyList; // 保留的操作集合
	private ArrayList<HistoryMemory> resetHistoryList; // 保留的操作集合
	private ArrayList<HistoryMemory> memories;// 存放未录制之前的操作集合
	private RecordViewGroup viewGroup;
	private float[] move; // 移动的坐标点的数据
	private JsonOperater jsonOperater; // json数据存储对象

	public HistoryOperater(RecordViewGroup viewGroup) {
		this.viewGroup = viewGroup;
		resetHistoryList = new ArrayList<HistoryMemory>();
		historyList = new ArrayList<HistoryMemory>();
		repeatList = new ArrayList<HistoryMemory>();
		memories = new ArrayList<HistoryMemory>();
		jsonOperater = JsonOperater.getInstance();
	}

	public List<HistoryMemory> getHistoryList() {
		return historyList;
	}

	public List<HistoryMemory> getResetHistoryList() {
		return resetHistoryList;
	}

	// 把所有的画线重新绘制
	public void drawPath(RecordView wacomView) {
		// 清空画布
		wacomView.clearCanvas();
		// 循环绘制线条
		for (int i = 0, j = historyList.size(); i < j; i++) {
			if (historyList.get(i).getOperaterType() == Config.OPER_PEN
					|| historyList.get(i).getOperaterType() == Config.OPER_RUBBER) {
				wacomView.drawPath(historyList.get(i));
			}
			if (historyList.get(i).getOperaterType() == Config.OPER_ARROW) {
				wacomView.drawAL(historyList.get(i));
			}
			if (historyList.get(i).getOperaterType() == Config.OPER_ELLIPSE) {
				wacomView.drawOval(historyList.get(i));
			}
			if (historyList.get(i).getOperaterType() == Config.OPER_RECT) {
				wacomView.drawRect(historyList.get(i));
			}
			if (historyList.get(i).getOperaterType() == Config.OPER_LINE) {
				wacomView.drawLine(historyList.get(i));
			}
		}
		// 刷新画布
		wacomView.refreshCanvas();
	}

	// 添加操作
	public synchronized void create(HistoryMemory memory) {
		if (memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
			// } else {
			/** 移除回退过的操作 */
			clearRepeal();
		}
		// 如果还没有录制，则把所有的操作轨迹全部记录下来
		if (!AtyRecord.recorded
				&& memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
			memories.add(memory);
		}
		switch (memory.getOperaterType()) {
		case Config.OPER_PEN: // 画线操作
			// 保存轨迹
			jsonOperater.penData(memory);
			break;
		case Config.OPER_RUBBER: // 橡皮擦操作
			jsonOperater.rubberData(memory);
			break;
		case Config.OPER_TEXT_ADD: // 创建文本
			jsonOperater.textAddd(memory.getRecordText());
			break;
		case Config.OPER_IMG_ADD: // 创建图片
			jsonOperater.imgAdd(memory.getMimg());
			break;
		case Config.OPER_ARROW:// 箭头
		case Config.OPER_ELLIPSE:// 椭圆
		case Config.OPER_RECT: // 矩形
		case Config.OPER_LINE: // 直线
//			jsonOperater.pathData(memory);
			break;
		case Config.OPER_CURSOR: // 光标
			jsonOperater.imgCursor(memory);
			break;
		}
		if (memory.getOperaterType() == Config.OPER_CLEAN_RECT) {
			return;
		} else {
			if (memory.getOperaterType() == Config.OPER_CURSOR) {// 光标也不添加到操作集合
			} else {
				viewGroup.addPencilNum();// 添加到操作集合
				historyList.add(memory);
			}
		}
	}

	// 文本和图片要先行删除
	public void delAllMemory() {
		for (int i = 0; i < historyList.size(); i++) {
			HistoryMemory memory = historyList.get(i);
			switch (memory.getOperaterType()) {
			// 文本操作相关
			case Config.OPER_TEXT_ADD:
				viewGroup.delEditText(memory.getRecordText(), false);
				break;
			// 图片操作相关
			case Config.OPER_IMG_ADD:
				viewGroup.delImageView(memory.getMimg(), false);
				break;
			}
		}
		historyList.clear();// 清除所有操作集合
		repeatList.clear();
	}

	// 清除屏幕 再重新画
	public void delMemories(RecordView wacomView) {
		try {
			// viewGroup.resetPencilNum(memories);
			wacomView.clearCanvas();// 清除屏幕
			wacomView.refreshCanvas();
			viewGroup.setDelPencilNum(memories.size());
			delAllMemory();
			for (int k = 0; k < memories.size(); k++) {
				HistoryMemory memory = memories.get(k);
				switch (memory.getOperaterType()) {
				case Config.OPER_PEN: // 画线操作
				case Config.OPER_RUBBER: // 橡皮擦操作
					wacomView.drawPath(memory, wacomView.map);// wacomView.map
					break;
				case Config.OPER_ARROW: // 箭头操作
					wacomView.drawAL(memory, wacomView.map);
					break;
				case Config.OPER_ELLIPSE: // 椭圆操作
					wacomView.drawOval(memory, wacomView.map);
					break;
				case Config.OPER_RECT: // 矩形操作
					wacomView.drawRect(memory, wacomView.map);
					break;
				case Config.OPER_LINE: // 直线操作
					wacomView.drawLine(memory, wacomView.map);
					break;
				case Config.OPER_TEXT_ADD: // 添加文本
					viewGroup.addEditText(memory.getRecordText());
					break;
				case Config.OPER_IMG_ADD: // 添加图片
					viewGroup.addImageView(memory.getMimg());
					break;
				}
				// 刷新画布
				wacomView.refreshCanvas();
				historyList.add(memory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 重新渲染画操作指令 */
	public void resetMemorier(RecordView wacomView,
			ArrayList<HistoryMemory> historyMemories) {
		try {
			// wacomView.clearCanvas();
			wacomView.refreshCanvas();
			for (int j = 0; j < historyMemories.size(); j++) {
				HistoryMemory memory = historyMemories.get(j);
				switch (memory.getOperaterType()) {
				case Config.OPER_PEN: // 画线操作
				case Config.OPER_RUBBER: // 橡皮擦操作
					wacomView.drawPath(memory);// wacomView.map
					break;
				case Config.OPER_ARROW: // 箭头操作
					wacomView.drawAL(memory);
					break;
				case Config.OPER_ELLIPSE: // 椭圆操作
					wacomView.drawOval(memory);
					break;
				case Config.OPER_RECT: // 矩形操作
					wacomView.drawRect(memory);
					break;
				case Config.OPER_LINE: // 直线操作
					wacomView.drawLine(memory);
					break;
				case Config.OPER_TEXT_ADD: // 添加文本
					viewGroup.addEditText(memory);
					break;
				case Config.OPER_IMG_ADD: // 添加图片
					viewGroup.addImageView(memory);
					break;
				}
				// 刷新画布
				wacomView.refreshCanvas();
				if (memory.getOperaterType() == Config.OPER_IMG_ADD
						|| memory.getOperaterType() == Config.OPER_TEXT_ADD) {
				} else {
					addToJson(memory);// 添加到json数据中
					resetHistoryList.add(memory);
				}
			}
		} catch (Exception e) {
		}
	}

	/** 添加到json */
	private void addToJson(HistoryMemory memory) {
		if (viewGroup.memoryMids.contains(memory.getMid())) {
			return;
		}
		viewGroup.memoryMids.add(memory.getMid());
		memory.setStartTime(DateUtil.getTimeMillisLong());
		memory.setEndTime(DateUtil.getTimeMillisLong());
		switch (memory.getOperaterType()) {
		case Config.OPER_PEN: // 画线操作
		case Config.OPER_RUBBER: // 橡皮擦操作
			jsonOperater.penData(memory);
			break;
		case Config.OPER_ARROW: // 箭头操作
		case Config.OPER_ELLIPSE: // 椭圆操作
		case Config.OPER_RECT: // 矩形操作
		case Config.OPER_LINE: // 直线操作
//			jsonOperater.pathData(memory);
			break;
		}
	}

	// 增加到回退的操作集合
	public void delPath(HistoryMemory memory) {
		repeatList.add(memory); // 将回退操作添加到回退集合中
		historyList.remove(memory); // 将被回退的操作从集合中删除
		// 如果还没有录制，则把所有的操作轨迹全部记录下来
		if (!AtyRecord.recorded
				&& memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
			memories.remove(memory);
		}
	}

	// 回退操作
	public void undo(RecordView wacomView) {
		if (historyList.size() >= 1) {
			// 　从最后一个操作记录开始回退
			HistoryMemory memory = historyList.get(historyList.size() - 1);
			wacomView.isWriting = true;
			// 获取操作类型
			int operaterType = memory.getOperaterType();
			if (operaterType == Config.OPER_PEN
			// || operaterType == Config.OPER_RUBBER
			// || operaterType == Config.OPER_ARROW
			// || operaterType == Config.OPER_ELLIPSE
			// || operaterType == Config.OPER_LINE
			// || operaterType == Config.OPER_RECT
			) {
				// 清空画布
				wacomView.clearCanvas();
				// 循环绘制线条
				int j = resetHistoryList.size();
				int order = 0;
				HistoryMemory histMemory = null;
				for (int i = 0; i < j; i++) {
					histMemory = resetHistoryList.get(i);
					order = histMemory.getOperaterType();
					// 画笔
					if (order == Config.OPER_PEN) {
						wacomView.drawPath(histMemory, wacomView.map);
					}
					// else if (order == Config.OPER_ARROW) {
					// wacomView.drawAL(histMemory, wacomView.map);
					// // 椭圆
					// } else if (order == Config.OPER_ELLIPSE) {
					// wacomView.drawOval(histMemory, wacomView.map);
					// // 直线
					// } else if (order == Config.OPER_LINE) {
					// wacomView.drawLine(histMemory, wacomView.map);
					// // 矩形
					// } else if (order == Config.OPER_RECT) {
					// wacomView.drawRect(histMemory, wacomView.map);
					// //
					// }
				}
				// 循环绘制线条
				int k = historyList.size() - 1;
				for (int i = 0; i < k; i++) {
					histMemory = historyList.get(i);
					order = histMemory.getOperaterType();
					// 画笔
					if (order == Config.OPER_PEN) {
						wacomView.drawPath(histMemory, wacomView.map);
					}
					// else if (order == Config.OPER_ARROW) {
					// wacomView.drawAL(histMemory, wacomView.map);
					// // 椭圆
					// } else if (order == Config.OPER_ELLIPSE) {
					// wacomView.drawOval(histMemory, wacomView.map);
					// // 直线
					// } else if (order == Config.OPER_LINE) {
					// wacomView.drawLine(histMemory, wacomView.map);
					// // 矩形
					// } else if (order == Config.OPER_RECT) {
					// wacomView.drawRect(histMemory, wacomView.map);
					// //
					// }
				}
				// 刷新画布
				wacomView.refreshCanvas();
			}
			switch (operaterType) {
			// ----------------以下是文本操作相关---------------------------------
			case Config.OPER_RUBBER:
				wacomView.undoRubber(historyList, memory.getIds());
				break;
			case Config.OPER_TEXT_ADD:
				viewGroup.delEditText(memory.getRecordText(), false);
				break;
			case Config.OPER_TEXT_EDIT:
				RecordEditText recordText = memory.getRecordText();
				recordText.setTextStr(memory.getOldText());
				recordText.setSize(memory.getOldSize());
				recordText.setColor(memory.getOldColor());
				break;
			case Config.OPER_TEXT_LOCK:
				memory.getRecordText().setLock(!memory.isLock());
				break;
			case Config.OPER_TEXT_DELETE:
				viewGroup.addEditText(memory.getRecordText());
				break;
			case Config.OPER_TEXT_MOVE:
				move = memory.getMoveList().get(0);
				memory.getRecordText().setLayoutByMid(move[0], move[1]);
				break;

			// ---------------以下是图片操作相关---------------------------------------
			case Config.OPER_IMG_ADD:
				viewGroup.delImageView(memory.getMimg(), false);
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.addImageView(memory.getMimg());
				break;
			case Config.OPER_IMG_MOVE:
				move = memory.getMoveList().get(0);
				memory.getMimg().setLayoutByMid(move[0], move[1]);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale / scaleList.get(i);
				}
				memory.getMimg().scaleImage(scale, false);
				break;
			case Config.OPER_IMG_ROTATE:
				memory.getMimg().rotate_90(false);
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(!memory.isLock());
				return;
			}
			repeatList.add(memory);
			historyList.remove(memory);
			// 如果还没有录制，则把所有的操作轨迹全部记录下来
			if (!AtyRecord.recorded
					&& memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
				memories.remove(memory);
			}
			// 保存轨迹
			jsonOperater.putUndoData(memory);
		}
	}

	// 前进操作
	public void redo(RecordView wacomView) {
		if (repeatList.size() >= 1) {
			HistoryMemory memory = repeatList.get(repeatList.size() - 1);
			wacomView.isWriting = true;
			switch (memory.getOperaterType()) {
			case Config.OPER_PEN: // 回退画线操作
				wacomView.drawPath(memory, wacomView.map);// 绘制线条
				wacomView.refreshCanvas();// 刷新画布
				break;
			// case Config.OPER_ELLIPSE: // 回退椭圆操作
			// wacomView.drawOval(memory, wacomView.map);
			// wacomView.refreshCanvas();
			// break;
			// case Config.OPER_ARROW: // 回退箭头操作
			// wacomView.drawAL(memory, wacomView.map);
			// wacomView.refreshCanvas();
			// break;
			// case Config.OPER_LINE: // 回退直线操作
			// wacomView.drawLine(memory, wacomView.map);
			// wacomView.refreshCanvas();
			// break;
			// case Config.OPER_RECT: // 回退矩形操作
			// wacomView.drawRect(memory, wacomView.map);
			// wacomView.refreshCanvas();
			// break;
			case Config.OPER_RUBBER: // 回退橡皮擦操作
				wacomView.redoRubber(historyList, memory.getIds());
				break;
			// ----------------以下是文本操作相关------------------------------------
			case Config.OPER_TEXT_ADD:
				viewGroup.addEditText(memory.getRecordText());
				break;
			case Config.OPER_TEXT_EDIT:
				RecordEditText recordText = memory.getRecordText();
				recordText.setTextStr(memory.getNewText());
				recordText.setSize(memory.getNewSize());
				recordText.setColor(memory.getNewColor());
				break;
			case Config.OPER_TEXT_LOCK:
				memory.getRecordText().setLock(memory.isLock());
				return;
			case Config.OPER_TEXT_DELETE:
				viewGroup.delEditText(memory.getRecordText(), false);
				break;
			case Config.OPER_TEXT_MOVE:
				float[] move = memory.getMoveList().get(
						memory.getMoveList().size() - 1);
				memory.getRecordText().setLayoutByMid(move[0], move[1]);
				break;

			// ---------------以下是图片操作相关----------------------------------------
			case Config.OPER_IMG_ADD:
				viewGroup.addImageView(memory.getMimg());
				break;
			case Config.OPER_IMG_DELETE:
				viewGroup.delImageView(memory.getMimg(), false);
				break;
			case Config.OPER_IMG_MOVE:
				List<float[]> l = memory.getMoveList();
				int ss = l.size();
				System.out.println(ss);
				move = memory.getMoveList()
						.get(memory.getMoveList().size() - 1);
				memory.getMimg().setLayoutByMid(move[0], move[1]);
				break;
			case Config.OPER_IMG_SCALE:
				List<Float> scaleList = memory.getScaleList();
				float scale = 1;
				for (int i = 0, j = scaleList.size(); i < j; i++) {
					scale = scale * scaleList.get(i);
				}
				memory.getMimg().scaleImage(scale, false);
				break;
			case Config.OPER_IMG_ROTATE:
				memory.getMimg().rotate90(false);
				break;
			case Config.OPER_IMG_LOCK:
				memory.getMimg().setLock(memory.isLock());
				return;
			}
			historyList.add(memory);
		
			// 如果还没有录制，则把所有的操作轨迹全部记录下来
			if (!AtyRecord.recorded
					&& memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
				memories.add(memory);
			}
			repeatList.remove(memory);
			// 保存轨迹
			jsonOperater.putRedoData(memory);
		}
	}

	/** 删除对象操作 */
	public void remove(HistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_DELETE) {
			historyList.add(memory);
			jsonOperater.textDelete(memory);// 保存轨迹
		} else if (memory.getOperaterType() == Config.OPER_IMG_DELETE) {
			historyList.add(memory);
			jsonOperater.imgDelete(memory);// 保存轨迹
		}
		// 如果还没有录制，同时也删除相应的操作轨迹
		if (!AtyRecord.recorded
				&& memory.getOperaterType() != Config.OPER_CLEAN_RECT) {
			for (int i = 0; i < memories.size(); i++) {
				if (memories.size() > 0 && memory.getMimg() != null
						&& memory.getMimg() == memories.get(i).getMimg()) {
					memories.remove(i);
				}
				if (memories.size() > 0
						&& memory.getRecordText() != null
						&& memory.getRecordText() == memories.get(i)
								.getRecordText()) {
					memories.remove(i);
				}
			}
		}
	}

	/** 修改文本操作 */
	public void modifyText(HistoryMemory memory) {
		historyList.add(memory);
		viewGroup.addPencilNum();
		// 保存轨迹
		jsonOperater.editText(memory);
	}

	/** 设置锁定于解锁操作 */
	public void setLock(HistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_LOCK) {
			memory.setOperaterType(Config.OPER_TEXT_LOCK);
			// historyList.add(memory);
		} else if (memory.getOperaterType() == Config.OPER_TEXT_LOCK) {
			memory.setOperaterType(Config.OPER_TEXT_LOCK);
			// historyList.add(memory);
		}
	}

	/** 移动操作 */
	public void setMove(HistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		if (memory.getOperaterType() == Config.OPER_TEXT_MOVE) {
			// 保存轨迹
			jsonOperater.textMove(memory);
		} else if (memory.getOperaterType() == Config.OPER_IMG_MOVE) {
			// 保存轨迹
			jsonOperater.imgMove(memory);
		}
		historyList.add(memory);
		viewGroup.addPencilNum();// 添加到操作集合
	}

	/** 缩放图片 */
	public void scaleImage(HistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		// 添加到操作集合
		viewGroup.addPencilNum();
		historyList.add(memory);
		// 保存轨迹
		jsonOperater.imgScale(memory);
	}

	/** 旋转图片 */
	public void rotateImage(HistoryMemory memory) {
		/** 移除回退过的操作 */
		clearRepeal();
		// 添加到操作集合
		viewGroup.addPencilNum();
		historyList.add(memory);
		// 保存轨迹
		jsonOperater.imgRotate(memory);
	}

	/** 在回退操作后，如果有新的操作，而非前进操作，则清空撤销的操作记录 */
	public void clearRepeal() {
		repeatList.clear();
	}

	/** 清空所以操作集合 */
	public void destroy() {
		historyList.clear();
	}

}
