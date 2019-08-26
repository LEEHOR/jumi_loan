package com.cashloan.jumidai.ui.homeMine.bean.recive;

import java.util.List;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/20 下午4:32
 * <p/>
 * Description: 所有字典
 */
public class DicRec {
    /** 联系人关系，type=BANK_TYPE */
    private List<KeyValueRec> bankTypeList;
    /** 联系人关系，type=CONTACT_RELATION */
    private List<KeyValueRec> contactRelationList;
    /** 紧急联系人关系，type=KINSFOLK_RELATION */
    private List<KeyValueRec> kinsfolkRelationList;
    /** 教育程度，type=EDUCATIONAL_STATE */
    private List<KeyValueRec> educationalStateList;
    /** 居住时长，type=LIVE_TIME */
    private List<KeyValueRec> liveTimeList;
    /** 婚姻状况，type=MARITAL_STATE */
    private List<KeyValueRec> maritalList;
    /** 月薪范围，type=SALARY_RANGE */
    private List<KeyValueRec> salaryRangeList;
    /** 工作时长，type=WORK_TIME */
    private List<KeyValueRec> workTimeList;

    public List<KeyValueRec> getContactRelationList() {
        return contactRelationList;
    }

    public List<KeyValueRec> getEducationalStateList() {
        return educationalStateList;
    }

    public List<KeyValueRec> getLiveTimeList() {
        return liveTimeList;
    }

    public List<KeyValueRec> getMaritalList() {
        return maritalList;
    }

    public List<KeyValueRec> getSalaryRangeList() {
        return salaryRangeList;
    }

    public List<KeyValueRec> getWorkTimeList() {
        return workTimeList;
    }

    public List<KeyValueRec> getBankTypeList() {
        return bankTypeList;
    }

    public List<KeyValueRec> getKinsfolkRelationList() {
        return kinsfolkRelationList;
    }
}
