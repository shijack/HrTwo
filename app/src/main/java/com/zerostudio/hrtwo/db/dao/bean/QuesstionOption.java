package com.zerostudio.hrtwo.db.dao.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 题目选项信息
 * Created by shijack on 2017/1/3.
 */
@DatabaseTable(tableName = "QUESTION_OPTION_BEAN")
public class QuesstionOption {

    @DatabaseField
    private int QUESTION_ID;// INTEGER
    @DatabaseField
    private String IMG;//Text
    @DatabaseField
    private String VALUE;//Text
    @DatabaseField
    private String KEY;//TEXT
    @DatabaseField
    private String TYPE;//Text
    @DatabaseField
    private String ISANSWER;//Text

    public int getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(int QUESTION_ID) {
        this.QUESTION_ID = QUESTION_ID;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getISANSWER() {
        return ISANSWER;
    }

    public void setISANSWER(String ISANSWER) {
        this.ISANSWER = ISANSWER;
    }

    @Override
    public String toString() {
        return "QuesstionOption{" +
                "QUESTION_ID=" + QUESTION_ID +
                ", IMG='" + IMG + '\'' +
                ", VALUE='" + VALUE + '\'' +
                ", KEY='" + KEY + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", ISANSWER='" + ISANSWER + '\'' +
                '}';
    }
}
