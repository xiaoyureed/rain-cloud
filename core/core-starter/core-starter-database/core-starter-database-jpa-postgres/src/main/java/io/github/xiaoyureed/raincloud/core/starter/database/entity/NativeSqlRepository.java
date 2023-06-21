package io.github.xiaoyureed.raincloud.core.starter.database.entity;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;

@Service
public class NativeSqlRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    /**
     * Execute update sql
     * @param sql native sql 
     * @return affected rows
     */
    public Integer update(String sql) {
        Query query = em.createNativeQuery(sql);
        int rows = query.executeUpdate();
        em.close();

        return rows;
    }

    /**
     * 查询多个属性
     * 返回List<Object[]>数组形式的List，数组中内容按照查询字段先后
     * @param sql   原生SQL语句
     * @return
     */
    public List<Object[]> sqlArrayList(String sql){
        // EntityManager em=emf.createEntityManager();
        
        Query query=em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        em.close();
        return  list;
    }

    /**
     * 查询多个属性
     * 返回List<Object>对象形式的List，Object为Class格式对象
     * @param sql   原生SQL语句
     * @param obj   Class格式对象
     * @return
     */
    public List sqlObjectList(String sql, Object obj){
        // EntityManager em=emf.createEntityManager();
        Query query=em.createNativeQuery(sql,obj.getClass());
        List list = query.getResultList();
        em.close();
        return  list;
    }

    /**
     * 查询单个属性
     * 返回List<Object>对象形式的List，Object为对象数据类型
     * @param sql  原生SQL语句
     * @return
     */
    public List sqlSingleList(String sql){
        // EntityManager em=emf.createEntityManager();
        Query query=em.createNativeQuery(sql);
        List list = query.getResultList();
        em.close();
        return  list;
    }
}
