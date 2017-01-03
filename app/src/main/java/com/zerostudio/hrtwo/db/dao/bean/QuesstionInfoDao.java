package com.zerostudio.hrtwo.db.dao.bean;

import android.content.Context;

import com.zerostudio.hrtwo.db.dao.ormlite.OrmLiteDao;

/**
 * Created by shijack on 2017/1/3.
 */
public class QuesstionInfoDao extends OrmLiteDao<QuesstionInfo> {
    public QuesstionInfoDao(Context context, Class<QuesstionInfo> cls) {
        super(context, cls);
    }
}
