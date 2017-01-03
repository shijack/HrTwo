package com.zerostudio.hrtwo.db.dao.bean;

import android.content.Context;

import com.zerostudio.hrtwo.db.dao.ormlite.OrmLiteDao;

/**
 * Created by shijack on 2017/1/3.
 */
public class QuesstionOptionDao extends OrmLiteDao<QuesstionOption> {

    public QuesstionOptionDao(Context context, Class<QuesstionOption> cls) {
        super(context, cls);
    }
}
