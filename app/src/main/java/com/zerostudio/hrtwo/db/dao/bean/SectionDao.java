package com.zerostudio.hrtwo.db.dao.bean;

import android.content.Context;

import com.zerostudio.hrtwo.db.dao.Section;
import com.zerostudio.hrtwo.db.dao.ormlite.OrmLiteDao;

/**
 * Created by shijack on 2017/1/3.
 */
public class SectionDao extends OrmLiteDao<Section> {

    public SectionDao(Context context, Class<Section> cls) {
        super(context, cls);
    }
}