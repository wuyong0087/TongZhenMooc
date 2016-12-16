package com.lib.play_rec.rec;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.Constant;
import com.lib.play_rec.entity.WorksBean;

import com.lib.play_rec.utils.CommUtil;
import com.lib.play_rec.utils.DateUtil;
import com.lib.play_rec.utils.HexUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonOperater {

    private static JsonOperater instance = null;
    private JSONObject videoJson;
    private JSONArray commandArray;
    private int viewId = 0; // 控件的id，自增
    private long sumTime = 0; // 总录制时间
    private long startTime = 0; // 开始录制时间
    private float thousand = 1000; // 基数1000
    private boolean isRecording = false; // 视频录制状态 // 是否正在录制
    private String videoFolder;
    private String videoFullPath;
    private String voiceName;
    private List<String> fileList; // 文件名称列表
    private String tempImgName; // 中间插入的图片名称
    private boolean shotScreen = false;
    // 当前时间保留的精确度
    private int subStart = 0, subEnd = 3;

    private float time;

    public synchronized static JsonOperater getInstance() {
        if (instance == null) {
            instance = new JsonOperater();
        }
        return instance;
    }

    private JsonOperater() {
        videoJson = new JSONObject();
        commandArray = new JSONArray();
        videoFolder = DateUtil.getTimeMillisStr();
        videoFullPath = Constant.worksPath
                + videoFolder + File.separator;
        File f = new File(videoFullPath);
        if (!f.exists()) {
            f.mkdir();
        }
        voiceName = videoFolder + Config.LOCAL_AUDIO_SUFFIX;
        fileList = new ArrayList<String>();
    }

    public JSONObject getEduJson() {
        return videoJson;
    }

    public void setEduJson(JSONObject eduJson) {
        this.videoJson = eduJson;
    }

    public JSONArray getCommandArray() {
        return commandArray;
    }

    public void setCommandArray(JSONArray commandArray) {
        this.commandArray = commandArray;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getViewId() {
        return ++viewId;
    }

    public long getSumTime() {
        return sumTime;
    }

    public void setSumTime() {
        this.sumTime += DateUtil.getTimeMillisLong() - this.startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public String getVideoFullPath() {
        return videoFullPath;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public String getTempImgName() {
        return tempImgName;
    }

    public void setTempImgName(String tempImgName) {
        this.tempImgName = tempImgName;
    }

    public boolean isShotScreen() {
        return shotScreen;
    }

    // public void putResetJsonObj(JSONObject jsonObject){
    // commandArray.put(jsonObject);
    // }

    /**
     * 添加文本对象
     *
     * @param pointY
     * @param pointX
     */
    public void textAdd(float pointX, float pointY, HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            detailObj.put("x", pointX);
            detailObj.put("y", pointY);
            detailObj.put("width", memory.getTextWidth());
            detailObj.put("height", memory.getTextHeight());
            detailObj.put("fontSize", memory.getOldSize());
            detailObj.put("fontColor",
                    HexUtil.colorToHexString(memory.getOldColor()));
            detailObj.put("words", memory.getOldText());

            commandObj.put("order", "AddText");
            commandObj.put("mid", memory.getMid());
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
            commandObj.put("detail", detailObj);

            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文本对象
     */
    public void textAddd(RecordEditText edit) {
        JSONObject commandObj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            detailObj.put("x", edit.getX());
            detailObj.put("y", edit.getY());
            detailObj.put("width", edit.getWidth());
            detailObj.put("height", edit.getHeight());
            detailObj.put("fontSize", edit.getSize());
            detailObj.put("fontColor",
                    HexUtil.colorToHexString(edit.getColor()));
            detailObj.put("words", edit.getTextStr());

            commandObj.put("order", "AddText");
            commandObj.put("mid", edit.getMid());
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
            commandObj.put("detail", detailObj);

            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改文本对象
     */
    public void editText(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            RecordEditText txt = memory.getRecordText();
            detailObj.put("x", txt.getX());
            detailObj.put("y", txt.getY());
            detailObj.put("width", txt.getWidth());
            detailObj.put("height", txt.getHeight());
            detailObj.put("fontSize", memory.getNewSize());
            detailObj.put("fontColor",
                    HexUtil.colorToHexString(memory.getNewColor()));
            detailObj.put("words", txt.getText().toString().trim());

            commandObj.put("order", "EditText");
            commandObj.put("mid", txt.getMid());

            getMemoryTime(memory, commandObj);
            commandObj.put("detail", detailObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文本对象
     */
    public void textDelete(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        try {
            commandObj.put("order", "DeleteText");
            commandObj.put("mid", memory.getRecordText().getMid());
            getMemoryTime(memory, commandObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文本
     */
    public void textMove(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        float[] data;
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_MOVE_TEXT);
            commandObj.put("mid", memory.getRecordText().getMid());
            getOrderTime(memory, commandObj);
            List<Long> timeList = memory.getTimeList();
            for (int i = 0, j = memory.getMoveList().size(); i < j; i++) {
                data = memory.getMoveList().get(i);
                dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                if (isRecording) {
                    dataObj.put("t", (timeList.get(i) - startTime + sumTime)
                            / thousand);
                } else {
                    dataObj.put("time", sumTime / thousand);
                }
                dataArray.put(dataObj);
            }
            commandObj.put("detail", dataArray);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回退操作
     */
    public void undo() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("order", "Undo");
            if (isRecording) {
                obj.put("startTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
                obj.put("endTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
            } else {
                obj.put("startTime", sumTime / thousand);
                obj.put("endTime", sumTime / thousand);
            }
            commandArray.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 前进操作
     */
    public void redo() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("order", "Redo");
            if (isRecording) {
                obj.put("startTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
                obj.put("endTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
            } else {
                obj.put("startTime", sumTime / thousand);
                obj.put("endTime", sumTime / thousand);
            }
            commandArray.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建空白页
     */
    public void newBlankPage(int oldPid) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("order", "NewPage");
            if (isRecording) {
                obj.put("startTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
                obj.put("endTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
            } else {
                obj.put("startTime", sumTime / thousand);
                obj.put("endTime", sumTime / thousand);
            }
            commandArray.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制当前页
     */
    public void newCopyPage(int oldPid) {
        JSONObject obj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            detailObj.put("old_pid", oldPid);

            obj.put("order", "CopyPage");
            obj.put("mid", getViewId());
            if (isRecording) {
                obj.put("startTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
                obj.put("endTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
            } else {
                obj.put("startTime", sumTime / thousand);
                obj.put("endTime", sumTime / thousand);
            }
            obj.put("detail", detailObj);

            commandArray.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 翻页操作
     */
    public void flipPage(int action, int currentPage) {
        JSONObject commandObj = new JSONObject();
        try {
            commandObj.put("action", action);
            commandObj.put("order", "FlipPage");
            commandObj.put("currentPage", currentPage);
            if (isRecording) {
                commandObj.put("startTime", (System.currentTimeMillis()
                        - startTime + sumTime)
                        / thousand);
                commandObj.put("endTime", (System.currentTimeMillis()
                        - startTime + sumTime)
                        / thousand);
            } else {
                commandObj.put("startTime", sumTime / thousand);
                commandObj.put("endTime", sumTime / thousand);
            }
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更换背景操作
     */
    public void changeBackground(int currbk) {
        JSONObject obj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            detailObj.put("cBackground", currbk);

            obj.put("order", "Cbackground");
            obj.put("mid", getViewId());
            if (isRecording) {
                obj.put("startTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
                obj.put("endTime",
                        (System.currentTimeMillis() - startTime + sumTime)
                                / thousand);
            } else {
                obj.put("startTime", sumTime / thousand);
                obj.put("endTime", sumTime / thousand);
            }
            obj.put("detail", detailObj);
            commandArray.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加图片对象
     */
    public synchronized void imgAdd(RecordImageView img) {
        JSONObject commandObj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        try {
            detailObj.put("x", img.posLeft);
            detailObj.put("y", img.posTop);
            detailObj.put("width", img.posRight - img.posLeft);
            detailObj.put("height", img.posBottom - img.posTop);
            detailObj.put("source", img.getImgName());

            commandObj.put("order", "AddImage");
            commandObj.put("mid", img.getMid());
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
            commandObj.put("detail", detailObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除图片对象
     */
    public void imgDelete(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_DELETE_IMAGE);
            commandObj.put("mid", memory.getMimg().getMid());
            getMemoryTime(memory, commandObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指令时间
     *
     * @param memory
     * @param commandObj
     * @throws JSONException
     */
    private void getMemoryTime(HistoryMemory memory, JSONObject commandObj)
            throws JSONException {
        if (isRecording) {
            commandObj.put("startTime",
                    (System.currentTimeMillis() - startTime + sumTime)
                            / thousand);
            commandObj.put("endTime",
                    (System.currentTimeMillis() - startTime + sumTime)
                            / thousand);
        } else {
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
        }
    }

    /**
     * 移动图片
     */
    public void imgMove(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        float[] data;
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_MOVE_IMAGE);
            commandObj.put("mid", memory.getMimg().getMid());
            List<Long> timeList = memory.getTimeList();
            getOrderTime(memory, commandObj);
            for (int i = 0, j = memory.getMoveList().size(); i < j; i++) {
                data = memory.getMoveList().get(i);
                dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                getDataTime(dataObj, timeList, i);
                dataArray.put(dataObj);
            }

            commandObj.put("detail", dataArray);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旋转图片
     */
    public void imgRotate(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_ROTATION_IMAGE);
            commandObj.put("mid", memory.getMimg().getMid());
            getOrderTime(memory, commandObj);
            List<Long> timeList = memory.getTimeList();
            for (int i = 0, j = memory.getRotateList().size(); i < j; i++) {
                dataObj = new JSONObject();
                dataObj.put("angle", memory.getRotateList().get(i));
                getDataTime(dataObj, timeList, i);
                dataArray.put(dataObj);
            }
            commandObj.put("detail", dataArray);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataTime(JSONObject dataObj, List<Long> timeList, int i)
            throws JSONException {
        if (isRecording) {
            time = (timeList.get(i) - startTime + sumTime) / thousand;
            time = (float) (Math.round(time * 100)) / 100;
            dataObj.put("t", time);
        } else {
            time = sumTime / thousand;
            time = (float) (Math.round(time * 100)) / 100;
            dataObj.put("t", time);
        }
    }

    private void getOrderTime(HistoryMemory memory, JSONObject commandObj)
            throws JSONException {
        if (isRecording) {
            time = (memory.getStartTime() - startTime + sumTime) / thousand;
            time = (float) (Math.round(time * 100)) / 100;
            commandObj.put("startTime", time);

            time = (memory.getEndTime() - startTime + sumTime) / thousand;
            time = (float) (Math.round(time * 100)) / 100;
            commandObj.put("endTime", time);
        } else {
            time = sumTime / thousand;
            time = (float) (Math.round(time * 100)) / 100;
            commandObj.put("startTime", time);
            commandObj.put("endTime", time);
        }
    }

    /**
     * 缩放图片
     */
    public void imgScale(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        String timeStemp = "";
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_SCALE_IMAGE);
            commandObj.put("mid", memory.getMimg().getMid());
            getOrderTime(memory, commandObj);
            List<Long> timeList = memory.getTimeList();
            for (int i = 0, j = memory.getScaleList().size(); i < j; i++) {
                dataObj = new JSONObject();
                dataObj.put("scale", memory.getScaleList().get(i));
                getDataTime(dataObj, timeList, i);
                dataArray.put(dataObj);
            }

            commandObj.put("detail", dataArray);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 截屏
     */
    public void screenShot(View view) {
        OutputStream os = null;
        try {
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            os = new FileOutputStream(videoFullPath + "screenshot"
                    + Config.LOCAL_IMAGE_SUFFIX);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, os);
//            os.write(bmpData);
            os.flush();
            shotScreen = true;
        } catch (Exception e) {
            shotScreen = false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 完成操作，保存操作轨迹到文件
     *
     * @param viewGroupList
     * @param isNote
     * @throws JSONException
     * @throws IOException
     */
    public WorksBean done(int w, int h, View view,
                          List<RecordViewGroup> viewGroupList, boolean isNote)
            throws IOException, JSONException {
        videoJson.put("screenWidth", w);
        videoJson.put("screenHeight", h);
        videoJson.put("version", 2.0);
        videoJson.put("Commands", commandArray);
        OutputStream os = null;
        WorksBean vb = new WorksBean();
        vb.setVideoPath(videoFolder);
        vb.setVideoText(videoFolder + Config.LOCAL_TEXT_SUFFIX);
        vb.setVideoVoice(voiceName);
        vb.setSumTime(String.valueOf(sumTime));
        vb.setCreateTime(videoFolder);
        vb.setVideoType("video");
//            保存指令文本
        os = new FileOutputStream(videoFullPath + vb.getVideoText());

        os.write(videoJson.toString().getBytes());
        os.flush();
        os.close();
//        生成截图
        if (!shotScreen) {
            screenShot(view);
        }

        String noteImage = "";
        int pages = 0;// 页数
        if (isNote) {// 保存笔记
            while (pages < viewGroupList.size()) {
                noteImage = noteImage + pages + Config.LOCAL_IMAGE_SUFFIX + ",";
                pages++;
            }
            noteImage = noteImage.subSequence(0, noteImage.length() - 1)
                    .toString().trim();

            String[] split = noteImage.split(",");
            for (int i = 0; i < split.length; i++) {
                fileList.add(split[i]);
            }
            vb.setVideoType("note");
        } else {// 删除截屏
            for (int i = 0; i < viewGroupList.size(); i++) {
                CommUtil.delFile(JsonOperater.getInstance().getVideoFullPath()
                        + i + Config.LOCAL_IMAGE_SUFFIX, true);
            }
        }
        vb.setNoteImage(noteImage);

        fileList.add(vb.getVideoText());
        fileList.add(vb.getVideoVoice());
        fileList.add("screenshot" + Config.LOCAL_IMAGE_SUFFIX);

        vb.setPages(pages);
        vb.setFiles(CommUtil.listToString(fileList, ","));
        vb.setIsDownLoad(1);
        // 生成压缩包
        try {
            boolean b = CommUtil.zipFolder(videoFullPath, videoFullPath + videoFolder + ".zip", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vb;
    }

    /**
     * 把startTime，endTime时间重置为0.0
     */
    public void delCommandArray() {
        for (int i = 0, j = commandArray.length(); i < j; i++) {
            JSONObject commandObj;
            try {
                commandObj = commandArray.getJSONObject(i);
                String order = commandObj.getString("order");
                double startTime = commandObj.getDouble("startTime");
                double endTime = commandObj.getDouble("endTime");
                if ((startTime != 0.0 || endTime != 0.0)
                        && (order.equals("Pencil") || order.equals("Arrow") || order
                        .equals("Ellipse"))) {
                    commandObj.remove("order");
                    commandObj.put("order", "");
                }
                commandObj.remove("startTime");
                commandObj.remove("endTime");
                commandObj.put("startTime", 0);
                commandObj.put("endTime", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把startTime，endTime时间重置为0.0
     */
    public JSONArray delCommandArray(JSONArray commandArray) {
        for (int i = 0, j = commandArray.length(); i < j; i++) {
            JSONObject commandObj;
            try {
                commandObj = commandArray.getJSONObject(i);
                commandObj.remove("startTime");
                commandObj.remove("endTime");
                commandObj.put("startTime", 0);
                commandObj.put("endTime", 0);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return commandArray;
    }

    /**
     * 对象销毁
     */
    public void destroy() {
        instance = null;
    }

    /** 新增指令 */
    /**
     * 开始指令
     */
    public void startOrder() {
        try {
            // long time = DateUtil.getTimeMillisLong();
            JSONObject commandObj = new JSONObject();
            commandObj.put(Config.ORDER_NAME, Config.ORDER_START);
            // if (recording) {
            // commandObj.put("startTime", (time - startTime + sumTime)
            // / thousand);
            // commandObj.put("endTime", (time - startTime + sumTime)
            // / thousand);
            // } else {
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
            // }
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指令
     */
    public void endOrder() {
        try {
            JSONObject commandObj = new JSONObject();
            commandObj.put(Config.ORDER_NAME, Config.ORDER_END);
            commandObj.put("startTime", sumTime / thousand);
            commandObj.put("endTime", sumTime / thousand);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加Undo操作的数据
     *
     * @param memory
     */
    public void putUndoData(HistoryMemory memory) {
        try {
            int operaterType = memory.getOperaterType();
            JSONObject undoCommand = new JSONObject();
            JSONArray undoArray = null;
            String orderValue = null;
            // 画笔相关
            if (Config.OPER_PEN == operaterType) {
                orderValue = Config.ORDER_UNDO_PENCIL;
                undoCommand.put("mid", memory.getMid());
                getMemoryTime(memory, undoCommand);

            } else if (Config.OPER_RUBBER == operaterType) {
                orderValue = Config.ORDER_UNDO_RUBBER;
                String ids = memory.getIds();
                String[] split = ids.split(",");
                JSONArray array = new JSONArray();
                for (String id : split) {
                    array.put(id);
                }
                undoCommand.put("ids", array);
                getMemoryTime(memory, undoCommand);

                // 文本相关
            } else if (Config.OPER_TEXT_ADD == operaterType) {
                orderValue = Config.ORDER_UNDO_ADDTEXT;
                RecordEditText recordText = memory.getRecordText();
                undoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, undoCommand);
                JSONObject obj = new JSONObject();
                obj.put("x", recordText.getX());
                obj.put("y", recordText.getY());
                obj.put("width", recordText.getWidth());
                obj.put("height", recordText.getHeight());
                obj.put("fontSize", recordText.getSize());
                obj.put("fontColor",
                        HexUtil.colorToHexString(recordText.getColor()));
                obj.put("words", recordText.getOldText());
                undoCommand.put("detail", obj);

            } else if (Config.OPER_TEXT_DELETE == operaterType) {
                orderValue = Config.ORDER_UNDO_DELETE_TEXT;
                RecordEditText recordText = memory.getRecordText();
                undoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, undoCommand);

            } else if (Config.OPER_TEXT_EDIT == operaterType) {
                orderValue = Config.ORDER_UNDO_EDITTEXT;
                RecordEditText recordText = memory.getRecordText();
                undoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, undoCommand);
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("x", recordText.getX());
                jsonObj.put("y", recordText.getY());
                jsonObj.put("width", recordText.getEtWidth());
                jsonObj.put("height", recordText.getEtHeight());
                jsonObj.put("fontSize", recordText.getSize());
                jsonObj.put("fontColor",
                        HexUtil.colorToHexString(recordText.getColor()));
                jsonObj.put("words", recordText.getTextStr());
                undoCommand.put("detail", jsonObj);

            } else if (Config.OPER_TEXT_MOVE == operaterType) {
                orderValue = Config.ORDER_UNDO_MOVETEXT;
                RecordEditText recordText = memory.getRecordText();
                undoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, undoCommand);
                List<float[]> moveList = memory.getMoveList();
                float[] move = moveList.get(0);
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("x", move[0]);
                jsonObj.put("y", move[1]);
                jsonObj.put("time", undoCommand.getDouble("startTime"));
                undoArray = new JSONArray();
                undoArray.put(jsonObj);
                undoCommand.put("detail", undoArray);
                // 图片相关
            } else if (Config.OPER_IMG_ADD == operaterType) {
                orderValue = Config.ORDER_UNDO_ADDIMAGE;
                RecordImageView recordImg = memory.getMimg();
                undoCommand.put("mid", recordImg.getMid());
                getMemoryTime(memory, undoCommand);
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("x", recordImg.posLeft);
                jsonObj.put("y", recordImg.posTop);
                jsonObj.put("width",
                        Math.abs(recordImg.posLeft - recordImg.posRight));
                jsonObj.put("height",
                        Math.abs(recordImg.posBottom - recordImg.posTop));
                jsonObj.put("source", recordImg.getImgName());
                undoCommand.put("detail", jsonObj);

            } else if (Config.OPER_CURSOR == operaterType) {

            } else if (Config.OPER_IMG_DELETE == operaterType) {
                orderValue = Config.ORDER_UNDO_DELETE_IMAGE;
                undoCommand.put("mid", memory.getMimg().getMid());
                getMemoryTime(memory, undoCommand);

            } else if (Config.OPER_IMG_MOVE == operaterType) {
                orderValue = Config.ORDER_UNDO_MOVEIMAGE;
                RecordImageView mimg = memory.getMimg();
                undoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, undoCommand);
                float[] move = memory.getMoveList().get(0);
                JSONObject dataObj = new JSONObject();
                dataObj.put("x", move[0]);
                dataObj.put("y", move[1]);
                dataObj.put("time", undoCommand.getDouble("startTime"));
                undoArray = new JSONArray();
                undoArray.put(dataObj);
                undoCommand.put("detail", undoArray);

            } else if (Config.OPER_IMG_ROTATE == operaterType) {
                orderValue = Config.ORDER_UNDO_ROTATION_IMAGE;
                RecordImageView mimg = memory.getMimg();
                undoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, undoCommand);
                List<Float> rotateList = memory.getRotateList();
                JSONObject dataObj = new JSONObject();
                dataObj.put("angle", rotateList.get(0));
                dataObj.put("time", undoCommand.getDouble("startTime"));
                undoArray = new JSONArray();
                undoArray.put(dataObj);
                undoCommand.put("detail", undoArray);

            } else if (Config.OPER_IMG_SCALE == operaterType) {
                orderValue = Config.ORDER_UNDO_SCALEIMAGE;
                RecordImageView mimg = memory.getMimg();
                undoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, undoCommand);
                JSONObject dataObj = new JSONObject();
                List<Float> scaleList = memory.getScaleList();
                float scale = 1;
                for (int i = 0, j = scaleList.size(); i < j; i++) {
                    scale = scale / scaleList.get(i);
                }
                dataObj.put("scale", scale);
                dataObj.put("time", undoCommand.getDouble("startTime"));
                undoArray = new JSONArray();
                undoArray.put(dataObj);
                undoCommand.put("detail", undoArray);
            }
            undoCommand.put(Config.ORDER_NAME, orderValue);
            commandArray.put(undoCommand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * redo的数据
     *
     * @param memory
     */
    public void putRedoData(HistoryMemory memory) {
        try {
            int operaterType = memory.getOperaterType();
            JSONObject redoCommand = new JSONObject();
            JSONArray redoArray = null;
            String orderValue = null;
            // 画笔相关
            if (Config.OPER_PEN == operaterType) {
                orderValue = Config.ORDER_REDO_PENCIL;
                redoCommand.put("mid", memory.getMid());
                getMemoryTime(memory, redoCommand);

            } else if (Config.OPER_RUBBER == operaterType) {
                orderValue = Config.ORDER_REDO_RUBBER;
                String ids = memory.getIds();
                String[] split = ids.split(",");
                JSONArray array = new JSONArray();
                for (String id : split) {
                    array.put(id);
                }
                redoCommand.put("ids", array);
                getMemoryTime(memory, redoCommand);

                // 文本相关
            } else if (Config.OPER_TEXT_ADD == operaterType) {
                orderValue = Config.ORDER_REDO_ADDTEXT;
                RecordEditText recordText = memory.getRecordText();
                redoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, redoCommand);
                JSONObject obj = new JSONObject();
                obj.put("x", recordText.getX());
                obj.put("y", recordText.getY());
                obj.put("width", recordText.getWidth());
                obj.put("height", recordText.getHeight());
                obj.put("fontSize", recordText.getSize());
                obj.put("fontColor",
                        HexUtil.colorToHexString(recordText.getColor()));
                obj.put("words", recordText.getText().toString());
                redoCommand.put("detail", obj);

            } else if (Config.OPER_TEXT_DELETE == operaterType) {
                orderValue = Config.ORDER_REDO_DELETE_TEXT;
                RecordEditText recordText = memory.getRecordText();
                redoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, redoCommand);

            } else if (Config.OPER_TEXT_EDIT == operaterType) {
                orderValue = Config.ORDER_REDO_EDITTEXT;
                RecordEditText recordText = memory.getRecordText();
                redoCommand.put("mid", recordText.getMid());
                getMemoryTime(memory, redoCommand);
                JSONObject dataObj = new JSONObject();
                dataObj.put("x", recordText.getX());
                dataObj.put("y", recordText.getY());
                dataObj.put("width", recordText.getOldWidth());
                dataObj.put("height", recordText.getOldHeight());
                dataObj.put("fontSize", memory.getNewSize());
                dataObj.put("fontColor",
                        HexUtil.colorToHexString(memory.getNewColor()));
                dataObj.put("words", memory.getNewText());
                redoCommand.put("detail", dataObj);

            } else if (Config.OPER_TEXT_MOVE == operaterType) {
                orderValue = Config.ORDER_REDO_MOVETEXT;
                RecordEditText recordText = memory.getRecordText();
                getMemoryTime(memory, redoCommand);
                redoCommand.put("mid", recordText.getMid());
                List<float[]> moveList = memory.getMoveList();
                float[] data = moveList.get(moveList.size() - 1);
                JSONObject dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                dataObj.put("time", redoCommand.getDouble("startTime"));
                redoArray = new JSONArray();
                redoArray.put(dataObj);
                redoCommand.put("detail", redoArray);

                // 图片相关
            } else if (Config.OPER_IMG_ADD == operaterType) {
                orderValue = Config.ORDER_REDO_ADDIMAGE;
                RecordImageView mimg = memory.getMimg();
                redoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, redoCommand);
                JSONObject dataObj = new JSONObject();
                dataObj.put("x", mimg.getX());
                dataObj.put("y", mimg.getY());
                dataObj.put("width", Math.abs(mimg.posLeft - mimg.posRight));
                dataObj.put("height", Math.abs(mimg.posTop - mimg.posBottom));
                dataObj.put("source", mimg.getImgName());
                redoCommand.put("detail", dataObj);

            } else if (Config.OPER_IMG_MOVE == operaterType) {
                orderValue = Config.ORDER_REDO_MOVEIMAGE;
                RecordImageView mimg = memory.getMimg();
                redoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, redoCommand);
                List<float[]> moveList = memory.getMoveList();
                int index = moveList.size() - 1;
                float[] data = moveList.get(index);
                JSONObject dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                dataObj.put("time", redoCommand.getDouble("startTime"));
                redoArray = new JSONArray();
                redoArray.put(dataObj);
                redoCommand.put("detail", redoArray);

            } else if (Config.OPER_IMG_DELETE == operaterType) {
                orderValue = Config.ORDER_REDO_DELETE_IMAGE;
                getMemoryTime(memory, redoCommand);
                redoCommand.put("mid", memory.getMimg().getMid());

            } else if (Config.OPER_IMG_ROTATE == operaterType) {
                orderValue = Config.ORDER_REDO_ROTATION_IMAGE;
                getMemoryTime(memory, redoCommand);
                RecordImageView mimg = memory.getMimg();
                redoCommand.put("mid", mimg.getMid());
                getMemoryTime(memory, redoCommand);
                List<Float> rotateList = memory.getRotateList();
                int index = rotateList.size() - 1;
                JSONObject dataObj = new JSONObject();
                dataObj.put("angle", rotateList.get(index));
                dataObj.put("time", redoCommand.getDouble("startTime"));
                redoArray = new JSONArray();
                redoArray.put(dataObj);
                redoCommand.put("detail", redoArray);

            } else if (Config.OPER_IMG_SCALE == operaterType) {
                orderValue = Config.ORDER_REDO_SCALEIMAGE;
                getMemoryTime(memory, redoCommand);
                RecordImageView mimg = memory.getMimg();
                redoCommand.put("mid", mimg.getMid());
                JSONObject dataObj = new JSONObject();
                List<Float> scaleList = memory.getScaleList();
                float scale = 1;
                for (int i = 0, j = scaleList.size(); i < j; i++) {
                    scale = scale * scaleList.get(i);
                }
                dataObj.put("scale", scale);
                dataObj.put("time", redoCommand.getDouble("startTime"));
                redoArray = new JSONArray();
                redoArray.put(dataObj);
                redoCommand.put("detail", redoArray);
            }
            redoCommand.put(Config.ORDER_NAME, orderValue);
            commandArray.put(redoCommand);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * 添加画线对象
     */
    public void penData(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        float[] data;
        try {
            String order = "Pencil";
            float strok = memory.getPaint().getStrokeWidth();
            commandObj.put("thickness", strok);
            commandObj.put("color",
                    HexUtil.colorToHexString(memory.getPaint().getColor()));
            commandObj.put("mid", memory.getMid());
            String source = memory.getSource() == 1 ? "TOUCH" : "YFPEN";
            commandObj.put("source", source);
            commandObj.put("order", order);
            for (int i = 0, j = memory.getDataList().size(); i < j; i++) {
                data = memory.getDataList().get(i);
                dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                dataObj.put("dx", data[2]);
                dataObj.put("dy", data[3]);
                getDataTime(dataObj, memory.getTimeList(), i);
                dataArray.put(dataObj);
            }
            commandObj.put("data", dataArray);

            getOrderTime(memory, commandObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存rubber数据
     *
     * @param memory
     */
    public void rubberData(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        try {
            commandObj.put(Config.ORDER_NAME, Config.ORDER_RUBBER);
            String ids = memory.getIds();
            String[] split = ids.split(",");
            JSONArray array = new JSONArray();
            for (String id : split) {
                array.put(id);
            }
            commandObj.put("ids", array);
            getOrderTime(memory, commandObj);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加光标对象
     */
    public void imgCursor(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONArray detailArray = new JSONArray();
        JSONObject dataObj;
        float[] data;
        try {
            List<Long> timeList = memory.getTimeList();
            for (int i = 0, j = memory.getDataList().size(); i < j; i++) {
                data = memory.getDataList().get(i);
                dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                getDataTime(dataObj, timeList, i);
                detailArray.put(dataObj);
            }
            commandObj.put(Config.ORDER_NAME, Config.ORDER_CURSOR);
            getOrderTime(memory, commandObj);
            commandObj.put("center", detailArray);
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	/* 以下指令 为待优化 或者需删除指令 */

    /**
     * 添加箭头对象
     */
    public void penPaths(HistoryMemory memory) {
        JSONObject commandObj = new JSONObject();
        JSONObject detailObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONObject dataObj;
        float[] data;
        try {
            detailObj.put("mid", memory.getMid());
            detailObj.put("thickness", memory.getPaint().getStrokeWidth());
            detailObj.put("color",
                    HexUtil.colorToHexString(memory.getPaint().getColor()));
            for (int i = 0, j = memory.getDataList().size(); i < j; i++) {
                data = memory.getDataList().get(i);
                dataObj = new JSONObject();
                dataObj.put("x", data[0]);
                dataObj.put("y", data[1]);
                dataObj.put("dx", 0);
                dataObj.put("dy", 0);
                if (isRecording) {
                    dataObj.put("time", (memory.getTimeList().get(i)
                            - startTime + sumTime)
                            / thousand);
                } else {
                    dataObj.put("time", sumTime / thousand);
                }
                dataArray.put(dataObj);
            }
            detailObj.put("data", dataArray);
            commandObj.put("mid", memory.getMid());
            getMemoryTime(memory, commandObj);
            commandObj.put("detail", detailObj);
            int type = memory.getOperaterType();
            if (type == Config.OPER_ARROW) {
                commandObj.put("order", "Arrow");
            } else if (type == Config.OPER_ELLIPSE) {
                commandObj.put("order", "Ellipse");
            } else if (type == Config.OPER_LINE) {
                commandObj.put("order", "Line");
            } else if (type == Config.OPER_RECT) {
                commandObj.put("order", "Rect");
            }
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择线条ID进行删除
     *
     * @param time
     */
    public void penClearRect(ArrayList<Integer> list, long time) {
        JSONObject commandObj = new JSONObject();
        try {
            commandObj.put("order", "ClearRec");
            commandObj.put("mid", -1);
            String str = list.toString();
            String string = str.substring(str.indexOf("[") + 1,
                    str.lastIndexOf("]"));
            commandObj.put("mids", string.replaceAll(" ", ""));
            if (isRecording) {
                commandObj.put("startTime", (time - startTime) / thousand);
                commandObj.put("endTime", (time - startTime) / thousand);
            } else {
                commandObj.put("startTime", sumTime / thousand);
                commandObj.put("endTime", sumTime / thousand);
            }
            commandArray.put(commandObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
