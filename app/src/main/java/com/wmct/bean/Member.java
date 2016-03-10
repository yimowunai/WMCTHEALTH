package com.wmct.bean;

import java.io.Serializable;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/4 18:36
 * <p>
 * --------------------------------------
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 140219911113L;
    private long id;


    private long memberid;
    private String familyPhone;
    private String membername;
    private int age;
    private int gender;
    private String image;

    public Member() {

    }

    public Member(long id, long memberid, String familyPhone, String membername, int age, int gender, String image) {
        this.id = id;
        this.memberid = memberid;
        this.familyPhone = familyPhone;
        this.membername = membername;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }


    @Override
    public String toString() {
        return "Member{" +
                "age=" + age +
                ", memberid=" + memberid +
                ", familyPhone='" + familyPhone + '\'' +
                ", membername='" + membername + '\'' +
                ", gender=" + gender +
                ", image='" + image + '\'' +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getFamilyPhone() {
        return familyPhone;

    }

    public int getAge() {
        return age;
    }


    public int getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public long getMemberid() {
        return memberid;
    }

    public String getMembername() {
        return membername;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }
}
