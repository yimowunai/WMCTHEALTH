package com.wmct.bean;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/5 15:34
 * <p/>
 * --------------------------------------
 */
public class MeasureData {

    private long id;     //测量数据的id
    private long memberid; //测量数据的外键
    private int diastolicpressure; //低压
    private int systolicpressure;  //高压
    private int heartrate;  //心率
    private int heartstate; //心率状况
    private int measuretime; //测量时间
    private int uploadtime;  //上传时间
    private String terminalid; //设备号


    public MeasureData(long id, long memberid, int diastolicpressure, int systolicpressure, int heartrate, int heartstate, int measuretime) {
        this.id = id;
        this.memberid = memberid;
        this.diastolicpressure = diastolicpressure;
        this.systolicpressure = systolicpressure;
        this.heartrate = heartrate;
        this.heartstate = heartstate;
        this.measuretime = measuretime;
    }

    public MeasureData(long id, String terminalid, int diastolicpressure, int systolicpressure, int heartrate, int heartstate, int measuretime) {
        this.id = id;
        this.terminalid = terminalid;
        this.diastolicpressure = diastolicpressure;
        this.systolicpressure = systolicpressure;
        this.heartrate = heartrate;
        this.heartstate = heartstate;
        this.measuretime = measuretime;
    }

    public MeasureData() {

    }

    @Override
    public String toString() {
        return "MeasureData{" +
                "diastolicpressure=" + diastolicpressure +
                ", id=" + id +
                ", memberid=" + memberid +
                ", systolicpressure=" + systolicpressure +
                ", heartrate=" + heartrate +
                ", heartstate=" + heartstate +
                ", measuretime=" + measuretime +
                ", uploadtime=" + uploadtime +
                '}';
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public void setMeasuretime(int measuretime) {
        this.measuretime = measuretime;
    }

    public void setUploadtime(int uploadtime) {
        this.uploadtime = uploadtime;
    }

    public int getUploadtime() {
        return uploadtime;
    }

    public int getMeasuretime() {
        return measuretime;
    }

    public int getDiastolicpressure() {
        return diastolicpressure;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public int getHeartstate() {
        return heartstate;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberid;
    }

    public int getSystolicpressure() {
        return systolicpressure;
    }


    public void setDiastolicpressure(int diastolicpressure) {
        this.diastolicpressure = diastolicpressure;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public void setHeartstate(int heartstate) {
        this.heartstate = heartstate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMemberId(long memberid) {
        this.memberid = memberid;
    }

    public void setSystolicpressure(int systolicpressure) {
        this.systolicpressure = systolicpressure;
    }
}
