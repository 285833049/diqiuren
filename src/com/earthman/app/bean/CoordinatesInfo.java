package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午8:12:37
 * @Decription 圈子：头像线条坐标信息
 */
public class CoordinatesInfo {

    private int startX, startY, endX, endY;//开始结束坐标
    private int lengthX, lengthY;//需要移动的坐标
    private int addX, addY;//x、y增量
    private int currentX, currentY;//存储当前XY

    private final int DURECTION = 600;//持续时间ms
    private final int SLEEP_TIME = 30;//刷新时间ms

    public CoordinatesInfo(int startX, int startY) {
        this.currentX = this.startX = startX;
        this.currentY = this.startY = startY;
    }

    public void setEndXY(int endX, int endY) {
        this.endX = endX;
        this.endY = endY;

        this.lengthX = endX - startX;
        this.lengthY = endY - startY;

        this.addX = lengthX / (DURECTION / SLEEP_TIME);
        this.addY = lengthY / (DURECTION / SLEEP_TIME);

    }

    //--------------------------当前坐标
    public int getCurrentY() {
        return currentY += this.addY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentX() {
        return currentX += this.addX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getLengthY() {
        return lengthY;
    }

    public int getAddX() {
        return addX;
    }

    public int getAddY() {
        return addY;
    }


}
