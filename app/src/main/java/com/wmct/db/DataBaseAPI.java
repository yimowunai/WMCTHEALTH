package com.wmct.db;

import android.content.Context;

import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;

import java.util.List;

/**
 * Created by little grey on 2016/1/15.
 */
public class DataBaseAPI {

    private static final String TAG = "DataBaseAPI";

    private static Context mcontext;

    private DataBaseHelper dataBaseHelper;

    public DataBaseAPI(Context context) {
//        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        mcontext = context;
    }

    public void insertFamily(Family family) {
        FamilyDao familyDao = new FamilyDao(mcontext);
        familyDao.insert(family);
    }

    public void deleteFamily(Family family) {
        FamilyDao familyDao = new FamilyDao(mcontext);
        familyDao.delete(family);
    }

    public void updateFamily(Family family) {
        FamilyDao familyDao = new FamilyDao(mcontext);
        familyDao.update(family);
    }

    public Family queryFamily(Family family) {
        FamilyDao familyDao = new FamilyDao(mcontext);
        Family qfamily = familyDao.query(family);
        return qfamily;
    }

    public List<Family> queryAllFamily() {
        FamilyDao familyDao = new FamilyDao(mcontext);
        List<Family> qallfamily = familyDao.queryAll();
        return qallfamily;
    }

    public void insertMember(Member member) {
        MemberDao memberDao = new MemberDao(mcontext);
        memberDao.insert(member);
    }

    public void deleteMember(Member member) {
        MemberDao memberDao = new MemberDao(mcontext);
        memberDao.delete(member);
    }

    public void updateMember(Member member) {
        MemberDao memberDao = new MemberDao(mcontext);
        memberDao.update(member);
    }

    public Member queryMember(long memberid) {
        MemberDao memberDao = new MemberDao(mcontext);
        Member qmember = memberDao.query(memberid);
        return qmember;
    }

    public List<Member> queryAllMember(Family family) {
        MemberDao memberDao = new MemberDao(mcontext);
        List<Member> qallmember = memberDao.queryAll(family);
        return qallmember;
    }

    public void insertMeasureData(MeasureData measureData) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        measureDataDao.insert(measureData);
    }

    public void batchInsertMesureData(List<MeasureData> list) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        measureDataDao.batchInsert(list);
    }

    public void deleteMeasureData(MeasureData measureData) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        measureDataDao.delete(measureData);
    }

    public List<MeasureData> queryAllMeasureData(Member member) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        List<MeasureData> qallmeasuredata = measureDataDao.queryAll(member);
        return qallmeasuredata;
    }

    public List<MeasureData> query10MeasureData(Member member) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        List<MeasureData> q10measuredata = measureDataDao.query10(member);
        return q10measuredata;
    }

    public MeasureData querylastMeasureData(Member member) {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        MeasureData qlastmeasuredata = measureDataDao.querylast(member);
        return qlastmeasuredata;
    }

    public void insertVisitordata(MeasureData visitordata) {
        VisitorDataDao visitorDataDao = new VisitorDataDao(mcontext);
        visitorDataDao.insert(visitordata);
    }

    public void deleteVisitordata(MeasureData visitordata) {
        VisitorDataDao visitorDataDao = new VisitorDataDao(mcontext);
        visitorDataDao.delete(visitordata);
    }

    public List<MeasureData> queryAllVistordata() {
        VisitorDataDao visitorDataDao = new VisitorDataDao(mcontext);
        List<MeasureData> qallvisitordata = visitorDataDao.queryAll();
        return qallvisitordata;
    }

    public void insertNot_uploaded(MeasureData not_uploaded) {
        NotUploadMeasureDataDao notUploadMeasureDataDao = new NotUploadMeasureDataDao(mcontext);
        notUploadMeasureDataDao.insert(not_uploaded);
    }

    public void deleteNot_uploaded(MeasureData not_uploaded) {
        NotUploadMeasureDataDao notUploadMeasureDataDao = new NotUploadMeasureDataDao(mcontext);
        notUploadMeasureDataDao.delete(not_uploaded);
    }

    public List<MeasureData> queryAllNot_uploaded() {
        NotUploadMeasureDataDao notUploadMeasureDataDao = new NotUploadMeasureDataDao(mcontext);
        List<MeasureData> qallnot_uploaded = notUploadMeasureDataDao.queryAll();
        return qallnot_uploaded;
    }

    public void deleteMeasureDataAll() {
        MeasureDataDao measureDataDao = new MeasureDataDao(mcontext);
        measureDataDao.deleteAll();
    }

    public void deleteVisitordataAll() {
        VisitorDataDao visitorDataDao = new VisitorDataDao(mcontext);
        visitorDataDao.deleteAll();
    }

    public void deleteNot_uploadedAll() {
        NotUploadMeasureDataDao notUploadMeasureDataDao = new NotUploadMeasureDataDao(mcontext);
        notUploadMeasureDataDao.deleteAll();
    }
}
