package com.zerostudio.hrtwo.db.dao.ormlite;


public enum DaoThreadMode {

    /**
     * 同一个线程
     */
    PostThread,

    /**
     * 主线程
     */
    MainThread,

    /**
     * 后台线程
     */
    BackgroundThread,

    /**
     * 单独开一个线程
     */
    Async
}
