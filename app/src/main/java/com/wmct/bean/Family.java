package com.wmct.bean;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/3 19:39
 * <p/>
 * --------------------------------------
 */
public class Family {
    private long id;
    private String name;
    private String pwd;
    private String phone;
    private String address;
    private String terminalid;
    private boolean flag = false;

    public Family() {
    }

    public Family(long id, String name, String pwd, String phone, String address, String terminalid) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.phone = phone;
        this.address = address;
        this.terminalid = terminalid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPwd() {
        return pwd;
    }

    public String getTerminalid() {
        return terminalid;
    }

    @Override
    public String toString() {
        return "Family{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", phone='" + phone + '\'' +
                ", terminalid='" + terminalid + '\'' +
                '}';
    }
}
