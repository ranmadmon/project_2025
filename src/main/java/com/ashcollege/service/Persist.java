package com.ashcollege.service;


import com.ashcollege.entities.CourseEntity;
import com.ashcollege.entities.MaterialEntity;
import com.ashcollege.entities.MaterialHistoryEntity;
import com.ashcollege.entities.UserEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }
    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }
    public <T> void remove(Object o){
        sessionFactory.getCurrentSession().remove(o);
    }


    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz)
    {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }

    public List<MaterialEntity> getMaterialByTitle(String title){
        return this.getQuerySession()
                .createQuery("FROM MaterialEntity WHERE title = :title")
                .setParameter("title",title)
                .list();
    }

    public UserEntity getUserByUsernameAndPass(String username, String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE username = :name and password = :password", UserEntity.class)
                    .setParameter("name", username)
                    .setParameter("password",password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MaterialEntity> getMaterialByCourseId(int courseId) {
        return this.getQuerySession()
                .createQuery("FROM MaterialEntity WHERE course_id = :course_id")
                .setParameter("course_id",courseId)
                .list();
    }

    public UserEntity getUserByEmail(String email) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email ", UserEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    public UserEntity getUserByEmailAndPasswordRecovery(String email, String pass_recovery) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email and pass_recovery = :pass_recovery", UserEntity.class)
                .setParameter("email", email)
                .setParameter("pass_recovery" ,pass_recovery)
                .uniqueResult();
    }

    public UserEntity getUserByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE username = :name", UserEntity.class)
                .setParameter("name", username)
                .uniqueResult();
    }

    public UserEntity getUserByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE password = :password", UserEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<MaterialHistoryEntity> getMaterialHistoryByUserId(int user_id) {
        return this.getQuerySession()
                .createQuery(" FROM MaterialHistoryEntity mh WHERE mh.user.id = :user_id", MaterialHistoryEntity.class)
                .setParameter("user_id", user_id)
                .list();
    }
    public MaterialEntity getMaterialById(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM MaterialEntity WHERE id= :id" , MaterialEntity.class)
                .setParameter("id",id)
                .uniqueResult();
    }

    public List<MaterialHistoryEntity> getMaterialHistoryByUserIdAndMaterialId(int materialId,int user_id) {
        return this.getQuerySession()
                .createQuery(" FROM MaterialHistoryEntity mh WHERE mh.user.id = :user_id and material_id = : material_id", MaterialHistoryEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("material_id", materialId)
                .list();
    }

    public List<CourseEntity> getCoursesByLecturerId(int lecturerId) {
        return this.getQuerySession()
                .createQuery(" FROM CourseEntity mh WHERE mh.user_id = :user_id ", CourseEntity.class)
                .setParameter("user_id", lecturerId)
                .list();
    }


//
//    public List<MaterialEntity> getMaterialByTag(String tag){
//        return this.sessionFactory.getCurrentSession()
//                .createQuery("FROM MaterialEntity WHERE MaterialEntity.TagEntity.name = :name")
//                .setParameter("name",tag)
//                .list();
//    }

  /// שאילתא לפי כותרת של חומר, לפי תגית ,ולפי טקסט חופשי


}