package com.zerostudio.hrtwo.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 章节信息，知识的目录
 * Created by shijack on 2017/1/3.
 */
@DatabaseTable(tableName = "SECTION_BEAN")
public class Section {

    @DatabaseField(generatedId = true)
    private int CHAPTER_ID;// INTEGER PRIMARY KEY
    @DatabaseField
    private String CHAPTER_PARENT_ID;// TEXT
    @DatabaseField
    private String TITLE;// TEXT
    @DatabaseField
    private int SORT;// INTEGER

    public int getCHAPTER_ID() {
        return CHAPTER_ID;
    }

    public void setCHAPTER_ID(int CHAPTER_ID) {
        this.CHAPTER_ID = CHAPTER_ID;
    }

    public String getCHAPTER_PARENT_ID() {
        return CHAPTER_PARENT_ID;
    }

    public void setCHAPTER_PARENT_ID(String CHAPTER_PARENT_ID) {
        this.CHAPTER_PARENT_ID = CHAPTER_PARENT_ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getSORT() {
        return SORT;
    }

    public void setSORT(int SORT) {
        this.SORT = SORT;
    }

    @Override
    public String toString() {
        return "Section{" +
                "CHAPTER_ID=" + CHAPTER_ID +
                ", CHAPTER_PARENT_ID='" + CHAPTER_PARENT_ID + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", SORT=" + SORT +
                '}';
    }
}
