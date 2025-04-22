package com.ashcollege.service;


import com.ashcollege.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ashcollege.entities.ProgressNotificationEntity;
import com.ashcollege.entities.UserEntity;
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

    public AdminEntity getLIdByUserId(int userId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM AdminEntity  WHERE user_id= :user_id" , AdminEntity.class)
                .setParameter("user_id",userId)
                .uniqueResult();
    }

    public int getUserIdByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT id FROM UserEntity WHERE password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user ID by password", e);
        }
    }
    public List<QuestionEntity> getQuestionsByUserId(int userId) {
        try {
            List<QuestionEntity> questions = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM QuestionEntity WHERE user.id = :userId", QuestionEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
            return questions;
        } catch (Exception e) {
            System.err.println("Error while fetching questions for userId: " + userId);
            e.printStackTrace(); // מדפיס את החריגה המלאה
            throw new RuntimeException("Error fetching questions for userId: " + userId, e);
        }
    }
    public int getCorrectAnswersForUser(int userId) {
        try {
            Long count = this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT COUNT(q) FROM QuestionEntity q WHERE q.user.id = :userId AND q.correct = 'Correct'", Long.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching correct answers for userId: " + userId, e);
        }
    }




    public List<QuestionEntity> getOpenQuestionsForUser(int userId) {
        return this.sessionFactory.getCurrentSession().createQuery(
                        "SELECT q FROM QuestionEntity q WHERE q.id NOT IN " +
                                "(SELECT qh.question.id FROM QuestionHistoryEntity qh WHERE qh.user.id = :userId)", QuestionEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public List<QuestionEntity> getRepeatQuestionsForUser(String password) {
        return this.sessionFactory.getCurrentSession().createQuery(
                        "SELECT qh.question FROM QuestionHistoryEntity qh WHERE qh.user.password = :password AND qh.isCorrect = 0", QuestionEntity.class)
                .setParameter("password", password)
                .getResultList();
    }

    public int getMathematicalExercisesLevelByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.mathematicalExercisesLevel FROM UserProgressEntity p JOIN p.user u WHERE u.password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getMathematicalExercisesScoreByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.mathematicalExercisesScore FROM UserProgressEntity p JOIN p.user u WHERE u.password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public int getVariableQuestionsLevelByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.variableQuestionsLevel FROM UserProgressEntity p JOIN p.user u WHERE u.password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getVerbalQuestionsScoreScoreByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.verbalQuestionsScore FROM UserProgressEntity p JOIN p.user u WHERE u.password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getVariableQuestionsScoreByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.variableQuestionsScore FROM UserProgressEntity p JOIN p.user u WHERE u.password = :password", Integer.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateVerbalQuestionsScoreByPass(String password, int update) {
        try {
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user.password = :password", UserProgressEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();

            if (progress != null) {
                if (update == 0) {
                    progress.setVerbalQuestionsScore(0);
                    System.out.println("Verbal questions score reset for user with password: " + password);
                } else if (update == 1) {
                    progress.setVerbalQuestionsScore(progress.getVerbalQuestionsScore() + 1);
                    System.out.println("Verbal questions score incremented for user with password: " + password);
                } else if (update == -1) {
                    int currentScore = progress.getVerbalQuestionsScore();
                    if (currentScore > 0) {
                        progress.setVerbalQuestionsScore(currentScore - 1);
                        System.out.println("Verbal questions score decremented for user with password: " + password);
                    } else {
                        System.out.println("Verbal questions score is already 0 for user with password: " + password);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid update value: " + update);
                }
                this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
            } else {
                throw new RuntimeException("User progress not found for the given password.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update verbal questions score", e);
        }
    }

    public void updateMathematicalExercisesScoreByPass(String password, int update) {
        try {
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user.password = :password", UserProgressEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();

            if (progress != null) {
                if (update == 0) {
                    progress.setMathematicalExercisesScore(0);
                    System.out.println("Mathematical exercises score reset for user with password: " + password);
                } else if (update == 1) {
                    progress.setMathematicalExercisesScore(progress.getMathematicalExercisesScore() + 1);
                    System.out.println("Mathematical exercises score incremented for user with password: " + password);
                } else if (update == -1) {
                    int currentScore = progress.getMathematicalExercisesScore();
                    if (currentScore > 0) {
                        progress.setMathematicalExercisesScore(currentScore - 1);
                        System.out.println("Mathematical exercises score decremented for user with password: " + password);
                    } else {
                        System.out.println("Mathematical exercises score is already 0 for user with password: " + password);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid update value: " + update);
                }
                this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
            } else {
                throw new RuntimeException("User progress not found for the given password.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update mathematical exercises score", e);
        }
    }

    public void incrementMathematicalExercisesLevel(UserEntity user) {
        try {
            // קבלת ההתקדמות לפי המשתמש
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user = :user", UserProgressEntity.class)
                    .setParameter("user", user)
                    .uniqueResult();

            if (progress != null) {
                // קידום הרמה באחד
                int currentLevel = progress.getMathematicalExercisesLevel();
                if (currentLevel < 12) { // בדוק אם ניתן לקדם רמה
                    progress.setMathematicalExercisesLevel(currentLevel + 1);
                    this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
                } else {
                    throw new RuntimeException("User has already reached the maximum level.");
                }
            } else {
                throw new RuntimeException("User progress not found for the given user.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to increment mathematical exercises level", e);
        }
    }
    public void incrementVariableQuestionsLevel(UserEntity user) {
        try {
            // קבלת ההתקדמות לפי המשתמש
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user = :user", UserProgressEntity.class)
                    .setParameter("user", user)
                    .uniqueResult();

            if (progress != null) {
                // קידום הרמה באחד
                int currentLevel = progress.getVariableQuestionsLevel();
                if (currentLevel < 12) { // בדוק אם ניתן לקדם רמה
                    progress.setVariableQuestionsLevel(currentLevel + 1);
                    this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
                } else {
                    throw new RuntimeException("User has already reached the maximum level.");
                }
            } else {
                throw new RuntimeException("User progress not found for the given user.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to increment mathematical exercises level", e);
        }
    }
    public void incrementVerbalQuestionsLevelLevel(UserEntity user) {
        try {
            // קבלת ההתקדמות לפי המשתמש
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user = :user", UserProgressEntity.class)
                    .setParameter("user", user)
                    .uniqueResult();

            if (progress != null) {
                // קידום הרמה באחד
                int currentLevel = progress.getVerbalQuestionsLevel();
                if (currentLevel < 12) { // בדוק אם ניתן לקדם רמה
                    progress.setVerbalQuestionsLevel(currentLevel + 1);
                    this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
                } else {
                    throw new RuntimeException("User has already reached the maximum level.");
                }
            } else {
                throw new RuntimeException("User progress not found for the given user.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to increment mathematical exercises level", e);
        }
    }
    public void updateVariableQuestionsScoreByPass(String password, int update) {
        try {
            UserProgressEntity progress = this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProgressEntity p WHERE p.user.password = :password", UserProgressEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();

            if (progress != null) {
                if (update == 0) {
                    progress.setVariableQuestionsScore(0);
                    System.out.println("Variable questions score reset for user with password: " + password);
                } else if (update == 1) {
                    progress.setVariableQuestionsScore(progress.getVariableQuestionsScore() + 1);
                    System.out.println("Variable questions score incremented for user with password: " + password);
                } else if (update == -1) {
                    int currentScore = progress.getVariableQuestionsScore();
                    if (currentScore > 0) {
                        progress.setVariableQuestionsScore(currentScore - 1);
                        System.out.println("Variable questions score decremented for user with password: " + password);
                    } else {
                        System.out.println("Variable questions score is already 0 for user with password: " + password);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid update value: " + update);
                }
                this.sessionFactory.getCurrentSession().saveOrUpdate(progress);
            } else {
                throw new RuntimeException("User progress not found for the given password.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update variable questions score", e);
        }
    }

    public int getMathematicalExercisesLevelByUser(UserEntity user) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.mathematicalExercisesLevel FROM UserProgressEntity p WHERE p.user = :user", Integer.class)
                    .setParameter("user", user)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<QuestionTemplateEntity> getTemplatesByLevel(int level) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM QuestionTemplateEntity WHERE level = :level", QuestionTemplateEntity.class)
                .setParameter("level", level)
                .getResultList();
    }
    public List<QuestionTemplateEntity> getQuestionTemplatesByLevel(int level) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM QuestionTemplateEntity WHERE level = :level", QuestionTemplateEntity.class)
                    .setParameter("level", level)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching question templates for level: " + level, e);
        }
    }
    public int getVerbalQuestionsLevelByUser(UserEntity user) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.verbalQuestionsLevel FROM UserProgressEntity p WHERE p.user = :user", Integer.class)
                    .setParameter("user", user)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getVariableQuestionsLevelByUser(UserEntity user) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("SELECT p.variableQuestionsLevel FROM UserProgressEntity p WHERE p.user = :user", Integer.class)
                    .setParameter("user", user)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * שומר התראה
     */
    public void saveNotification(ProgressNotificationEntity notification) {
        sessionFactory.getCurrentSession().saveOrUpdate(notification);
    }

    /**
     * מחזיר את כל ההתראות של משתמש, לפי סדר יורד של זמן
     */
    public List<ProgressNotificationEntity> getNotificationsByUser(UserEntity user) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM ProgressNotificationEntity WHERE user = :user AND read = false ORDER BY timeStamp DESC", ProgressNotificationEntity.class)
                .setParameter("user", user)
                .getResultList();
    }


    /**
     * מסמן התראה כנקראת, לפי מזהה ההתראה
     */
    public void markNotificationAsRead(int notificationId) {
        ProgressNotificationEntity notification = sessionFactory.getCurrentSession().get(ProgressNotificationEntity.class, notificationId);
        if (notification != null) {
            notification.setRead(true);
            sessionFactory.getCurrentSession().saveOrUpdate(notification);
        }
    }



}