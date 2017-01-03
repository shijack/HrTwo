package com.zerostudio.hrtwo.db.dao.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 题目信息
 * Created by shijack on 2017/1/3.
 */
@DatabaseTable(tableName = "QUESTION_INFO_BEAN")
public class QuesstionInfo {
    @DatabaseField(generatedId = true)
    private int QUESTION_ID;//INTEGER PRIMARY KEY
    @DatabaseField
    private String CHAPTER_PARENT_ID;// ;// TEXT
    @DatabaseField
    private String CHAPTER_ID;// ;// TEXT
    @DatabaseField
    private String TITLE;// TEXT
    @DatabaseField
    private String TITLE_IMG;// TEXT
    @DatabaseField
    private int S_NUM; //INTEGER
    @DatabaseField
    private String UNIT;// TEXT
    @DatabaseField
    private String NUMBER;// TEXT
    @DatabaseField
    private String YEAR;// TEXT
    @DatabaseField
    private int NUMBER_NUMBER;// INTEGER
    @DatabaseField
    private String NUMBER_TYPE;// TEXT
    @DatabaseField
    private String RESTORE;// TEXT
    @DatabaseField
    private String EXPLAIN;// TEXT
    @DatabaseField
    private String TYPE;// TEXT
    @DatabaseField
    private String ANSWER;// TEXT
    @DatabaseField
    private String SUBJECT_NAME;// TEXT
    @DatabaseField
    private String OPTION;// TEXT

    public int getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(int QUESTION_ID) {
        this.QUESTION_ID = QUESTION_ID;
    }

    public String getCHAPTER_PARENT_ID() {
        return CHAPTER_PARENT_ID;
    }

    public void setCHAPTER_PARENT_ID(String CHAPTER_PARENT_ID) {
        this.CHAPTER_PARENT_ID = CHAPTER_PARENT_ID;
    }

    public String getCHAPTER_ID() {
        return CHAPTER_ID;
    }

    public void setCHAPTER_ID(String CHAPTER_ID) {
        this.CHAPTER_ID = CHAPTER_ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getTITLE_IMG() {
        return TITLE_IMG;
    }

    public void setTITLE_IMG(String TITLE_IMG) {
        this.TITLE_IMG = TITLE_IMG;
    }

    public int getS_NUM() {
        return S_NUM;
    }

    public void setS_NUM(int s_NUM) {
        S_NUM = s_NUM;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public int getNUMBER_NUMBER() {
        return NUMBER_NUMBER;
    }

    public void setNUMBER_NUMBER(int NUMBER_NUMBER) {
        this.NUMBER_NUMBER = NUMBER_NUMBER;
    }

    public String getNUMBER_TYPE() {
        return NUMBER_TYPE;
    }

    public void setNUMBER_TYPE(String NUMBER_TYPE) {
        this.NUMBER_TYPE = NUMBER_TYPE;
    }

    public String getRESTORE() {
        return RESTORE;
    }

    public void setRESTORE(String RESTORE) {
        this.RESTORE = RESTORE;
    }

    public String getEXPLAIN() {
        return EXPLAIN;
    }

    public void setEXPLAIN(String EXPLAIN) {
        this.EXPLAIN = EXPLAIN;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }

    public String getSUBJECT_NAME() {
        return SUBJECT_NAME;
    }

    public void setSUBJECT_NAME(String SUBJECT_NAME) {
        this.SUBJECT_NAME = SUBJECT_NAME;
    }

    public String getOPTION() {
        return OPTION;
    }

    public void setOPTION(String OPTION) {
        this.OPTION = OPTION;
    }

    @Override
    public String toString() {
        return "QuesstionInfo{" +
                "QUESTION_ID=" + QUESTION_ID +
                ", CHAPTER_PARENT_ID='" + CHAPTER_PARENT_ID + '\'' +
                ", CHAPTER_ID='" + CHAPTER_ID + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", TITLE_IMG='" + TITLE_IMG + '\'' +
                ", S_NUM=" + S_NUM +
                ", UNIT='" + UNIT + '\'' +
                ", NUMBER='" + NUMBER + '\'' +
                ", YEAR='" + YEAR + '\'' +
                ", NUMBER_NUMBER=" + NUMBER_NUMBER +
                ", NUMBER_TYPE='" + NUMBER_TYPE + '\'' +
                ", RESTORE='" + RESTORE + '\'' +
                ", EXPLAIN='" + EXPLAIN + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", ANSWER='" + ANSWER + '\'' +
                ", SUBJECT_NAME='" + SUBJECT_NAME + '\'' +
                ", OPTION='" + OPTION + '\'' +
                '}';
    }
}
