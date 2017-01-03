package com.zerostudio.hrtwo.db.dao.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.zerostudio.hrtwo.db.DatabaseHelper;
import com.zerostudio.hrtwo.utils.LogUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一的Dao实现，封装了增删查改的统一操作
 * <p/>
 */
public class OrmLiteDao<T> {

    protected Dao<T, Integer> ormLiteDao;

    protected DatabaseHelper helper;

    public OrmLiteDao(Context context, Class<T> cls) {
        helper = DatabaseHelper.getInstance(context);
        ormLiteDao = helper.getDao(cls);
    }

    public DatabaseConnection beginTransaction() {
        DatabaseConnection connection = null;
        try {
            connection = ormLiteDao.getConnectionSource().getReadWriteConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void commit(DatabaseConnection connection) {
        try {
            connection.commit(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean doBatch(List<T> list, int batchType) {
        int result = 0;
        DatabaseConnection connection = null;
        try {
            connection = ormLiteDao.getConnectionSource().getReadWriteConnection();
            connection.setAutoCommit(false);    // 设置为不自动提交，因为自动提交效率很低，不自动提交可一定程度上提升写的效率（百度出来的）
            for (T t : list) {
                switch (batchType) {
                    case DaoOperation.INSERT:
                        result += ormLiteDao.create(t);
                        break;
                    case DaoOperation.DELETE:
                        result += ormLiteDao.delete(t);
                        break;
                    case DaoOperation.UPDATE:
                        result += updateIfValueNotNull(t);
                        break;
                    default:
                        LogUtil.d("no this type.");
                        break;
                }
            }
            connection.commit(null);
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return result == list.size();
    }

    /**
     * 增加一条记录
     *
     * @param t
     * @return
     */
    public boolean insert(T t) {
        int result = 0;
        try {
            result = ormLiteDao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result > 0) {
            DaoObserver.publish(DaoOperation.INSERT, t);
        } else {
            LogUtil.e("insert error,insert line:" + result);
        }
        return result > 0;
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean insertForBatch(List<T> list) {
        boolean result = doBatch(list, DaoOperation.INSERT);
        if (result) {
            DaoObserver.publish(DaoOperation.INSERT_BATCH, list);
        } else {
            LogUtil.e("insert error,result:" + result);
        }
        return result;
    }

    public boolean clearTableData() {
        long count = 0;
        try {
            count = ormLiteDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            return true;
        }
        int result = 0;
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        try {
            deleteBuilder.where().isNotNull("id");
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result > 0;
    }

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    public boolean deleteById(int id) {
        if (checkIdIsNull(id)) {
            return false;
        }
        int result = 0;
        T t = null;
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        try {
            t = ormLiteDao.queryForId(id);
            deleteBuilder.where().idEq(id);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (result > 0) {
            DaoObserver.publish(DaoOperation.DELETE, t);
        } else {
            LogUtil.i("delete error,delete line:" + result);
        }
        return true;
    }

    public boolean deleteByColumnName(String columnName, Object value) {
        int result = 0;
        List<T> list = null;
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        try {
            list = queryByColumnName(columnName, value);
            deleteBuilder.where().eq(columnName, value);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (result > 0) {
            DaoObserver.publish(DaoOperation.DELETE, list);
        } else {
            LogUtil.i("delete error, columnName: " + columnName + ", value: " + value + ", result: " + result);
        }
        return true;
    }

    /**
     * 通过表列名来删除
     *
     * @param map key是列名,value是列对应的值
     * @return
     */
    public boolean deleteByColumnName(Map<String, Object> map) {
        int result = 0;
        List<T> list = null;
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        Where where = deleteBuilder.where();
        try {
            list = queryByColumnName(map);
            where.isNotNull("id");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                where.and().eq(entry.getKey(), entry.getValue());
            }
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (result > 0) {
            DaoObserver.publish(DaoOperation.DELETE, list);
        } else {
            LogUtil.i("delete error,delete line:" + result);
        }
        return true;
    }

    /**
     * 批量删除，list中的item必须有id
     *
     * @param list
     */
    public boolean deleteForBatch(List<T> list) {
        boolean result = doBatch(list, DaoOperation.DELETE);
        if (result) {
            DaoObserver.publish(DaoOperation.DELETE_BATCH, list);
        } else {
            LogUtil.i("deleteForBatch error,result:" + result);
        }
        return result;
    }

    public long getCount(Map<String, Object> map) {
        long count = 0;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.setCountOf(true);
        Where where = queryBuilder.where();
        try {
            where.isNotNull("id");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                where.and().eq(entry.getKey(), entry.getValue());
            }
            PreparedQuery preparedQuery = queryBuilder.prepare();
            count = ormLiteDao.countOf(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 查询全部记录
     *
     * @return
     */
    public List<T> queryForAll() {
        List<T> list = null;
        try {
            list = ormLiteDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    public T queryById(Integer id) {
        if (checkIdIsNull(id)) {
            return null;
        }
        T t = null;
        try {
            t = ormLiteDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 根据表中任意字段进行查询
     *
     * @param map
     * @return
     */
    public List<T> queryByColumnName(Map<String, Object> map) {
        List<T> list = null;
        try {
            list = ormLiteDao.queryForFieldValuesArgs(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取指定类的数据访问对象
     *
     * @return
     */
    public Dao<T, Integer> getDao() {
        return ormLiteDao;
    }

    /**
     * 通过表列名查询
     *
     * @param columnName
     * @param value
     * @return
     */
    public List<T> queryByColumnName(String columnName, Object value) {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        try {
            queryBuilder.where().eq(columnName, value);
            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 排序查询指定条件下，大于指定值的所有记录
     *
     * @param orderColumn 大于的列
     * @param limitValue  大于的值
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param ascending   true为升序,false为降序
     * @return
     */
    public List<T> queryGeByOrder(String orderColumn, Object limitValue, String columnName, Object value, boolean ascending) {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        try {
            where.eq(columnName, value);
            where.and().ge(orderColumn, limitValue);
            queryBuilder.orderBy(orderColumn, ascending);
            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 分页排序查询
     *
     * @param columnName
     * @param value
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     * @return
     */
    public List<T> queryForPagesByOrder(String columnName, Object value, String orderColumn, boolean ascending, Long offset, Long count) {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        try {
            where.eq(columnName, value);
            queryBuilder.orderBy(orderColumn, ascending);
            queryBuilder.offset(offset);
            queryBuilder.limit(count);
            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 按列排序后分页查询
     *
     * @param map         查询的列条件
     * @param offset      查询的下标
     * @param count       查询的条数
     * @param orderColumn 排序的列
     * @param ascending   升序或降序,true为升序,false为降序
     * @return
     */
    public List<T> queryForPagesByOrder(Map<String, Object> map, String orderColumn, boolean ascending, Long offset, Long count) {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        try {
            queryBuilder.orderBy(orderColumn, ascending);
            queryBuilder.offset(offset);
            queryBuilder.limit(count);
            where.isNotNull("id");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                where.and().eq(entry.getKey(), entry.getValue());
            }
            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过表列名查询第一条记录
     *
     * @param map
     * @return
     */
    public T queryForFirst(Map<String, Object> map) {
        T t = null;
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        try {
            where.isNotNull("id");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                where.and().eq(entry.getKey(), entry.getValue());
            }
            t = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    public T queryForFirstByOrder(Map<String, Object> map, String orderColumn, boolean ascending) {
        T t = null;
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        try {
            queryBuilder.orderBy(orderColumn, ascending);
            where.isNotNull("id");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                where.and().eq(entry.getKey(), entry.getValue());
            }
            t = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    public T queryForFirstByOrder(String columnName, Object value, String orderColumn, boolean ascending) {
        T t = null;
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        try {
            queryBuilder.orderBy(orderColumn, ascending);
            queryBuilder.where().eq(columnName, value);
            t = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    public T queryForFirst(String columnName, Object value) {
        T t = null;
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        try {
            queryBuilder.where().eq(columnName, value);
            t = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 修改记录，ID不能为空
     *
     * @param t
     * @return
     */
    public boolean update(T t) {
        int result = updateIfValueNotNull(t);
        if (result > 0) {
            T t1 = null;
            try {
                t1 = ormLiteDao.queryForSameId(t);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (t1 != null) {
                DaoObserver.publish(DaoOperation.UPDATE, t1);
            } else {
                LogUtil.e("no find the same id of:" + t);
            }
        } else {
            LogUtil.i("update error,update line:" + result);
        }
        return result > 0;
    }

    public boolean updateBy(T t, String columnName, Object value) {
        int result = 0;
        T t1 = queryForFirst(columnName, value);
        if (t1 == null) {
            LogUtil.e("no find this data in database:" + t);
            return false;
        }
        try {
            setObjectValueIfNotNull(t1, t);
            result = ormLiteDao.update(t1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result > 0) {
            DaoObserver.publish(DaoOperation.UPDATE, t1); //发布
        } else {
            LogUtil.i("update error,update line:" + result);
        }
        return result > 0;
    }

    public boolean updateBy(T t, Map<String, Object> value) {
        int result = 0;
        T t1 = queryForFirst(value);
        if (t1 == null) {
            LogUtil.e("no find this data in database:" + t);
            return false;
        }
        try {
            setObjectValueIfNotNull(t1, t);
            result = ormLiteDao.update(t1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result > 0) {
            DaoObserver.publish(DaoOperation.UPDATE, t1); //发布
        } else {
            LogUtil.i("update error,update line:" + result);
        }
        return result > 0;
    }

    /**
     * 如果更新记录字段值为null则忽略不更新
     *
     * @param t
     * @return
     */
    private int updateIfValueNotNull(T t) {
        int result = 0;
        UpdateBuilder updateBuilder = ormLiteDao.updateBuilder();
        Map<String, Object> map = getFieldsIfValueNotNull(t);
        if (map.isEmpty()) {
            LogUtil.i("all field value is null.");
            return 0;
        }
        if (map.get("id") == null) {
            LogUtil.i("id is null.");
            return 0;
        }
        try {
            updateBuilder.where().idEq(map.get("id"));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("id")) {
                    continue;
                }
                updateBuilder.updateColumnValue(entry.getKey(), entry.getValue());
            }
            result = updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量修改
     *
     * @param list
     * @return
     */
    public boolean updateForBatch(List<T> list) {
        boolean result = doBatch(list, DaoOperation.UPDATE);
        if (result) {
            DaoObserver.publish(DaoOperation.UPDATE_BATCH, list); //发布
        } else {
            LogUtil.e("updateForBatch error,result:" + result);
        }
        return result;
    }

    private void setObjectValueIfNotNull(T t, Object obj) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Class<?> cls = t.getClass();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            Object valueObj = field.get(obj);
            if (valueObj != null) {
                Field f = cls.getDeclaredField(field.getName());
                if (f != null) {
                    f.setAccessible(true);
                    f.set(t, valueObj);
                } else {
                    LogUtil.e("no this field:" + field.getName());
                }
            } else {
                LogUtil.i(field.getName() + " is null.");
            }
        }
    }

    /**
     * 获取对象属性值不为空的属性名称和属性值
     *
     * @param obj
     * @return
     */
    private Map<String, Object> getFieldsIfValueNotNull(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object valueObj = null;
            try {
                valueObj = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (valueObj != null) {
                map.put(field.getName(), valueObj);
            } else {
                LogUtil.i(field.getName() + " is null.");
            }
        }
        LogUtil.i("map" + map);
        return map;
    }

    private boolean checkIdIsNull(Integer id) {
        if (id == null) {
            LogUtil.i("id is null.");
            return true;
        }
        return false;
    }
}
