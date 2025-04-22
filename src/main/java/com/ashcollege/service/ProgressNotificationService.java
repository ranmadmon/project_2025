package com.ashcollege.service;

import com.ashcollege.entities.ProgressNotificationEntity;
import com.ashcollege.entities.QuestionEntity;
import com.ashcollege.entities.UserEntity;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProgressNotificationService {

    @Autowired
    private Persist persist;

    private static final int BLOCK_SIZE = 10;
    private static final double DROP_THRESHOLD = 0.20;
    private static final double MIN_SUCCESS_RATE = 0.5;
    private static final double OVERALL_THRESHOLD = 0.30;

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void checkUsersPerformanceAndNotify() {
        List<UserEntity> allUsers = persist.loadList(UserEntity.class);

        for (UserEntity user : allUsers) {
            checkSubjectPerformance(user, 1, "Mathematics");
            checkSubjectPerformance(user, 2, "Verbal");
            checkSubjectPerformance(user, 3, "Variable");
        }
    }


    private void checkSubjectPerformance(UserEntity user, int type, String subject) {
        List<QuestionEntity> allQuestions = persist.getQuerySession()
                .createQuery("FROM QuestionEntity WHERE user = :user AND type = :type ORDER BY timeStamp DESC", QuestionEntity.class)
                .setParameter("user", user)
                .setParameter("type", type)
                .getResultList();

        if (allQuestions.size() >= BLOCK_SIZE * 2) {
            List<QuestionEntity> recentBlock = allQuestions.subList(0, BLOCK_SIZE);
            List<QuestionEntity> previousBlock = allQuestions.subList(BLOCK_SIZE, BLOCK_SIZE * 2);

            long correctRecent = recentBlock.stream()
                    .filter(q -> "Correct".equalsIgnoreCase(q.getCorrect()))
                    .count();
            long correctPrevious = previousBlock.stream()
                    .filter(q -> "Correct".equalsIgnoreCase(q.getCorrect()))
                    .count();

            double recentRate = (double) correctRecent / BLOCK_SIZE;
            double previousRate = (double) correctPrevious / BLOCK_SIZE;

            if (recentRate < previousRate - DROP_THRESHOLD) {
                String message = "Your recent performance in " + subject + " has dropped significantly compared to your previous performance. Please consider extra practice.";
                createNotification(user, subject, message);
            }
            if (correctRecent == 0) {
                String message = "You haven't answered any " + subject + " questions correctly in your recent block. Please review the material carefully.";
                createNotification(user, subject, message);
            }
        } else if (allQuestions.size() >= BLOCK_SIZE) {
            int limit = Math.min(BLOCK_SIZE, allQuestions.size());
            List<QuestionEntity> recentBlock = allQuestions.subList(0, limit);
            long correctRecent = recentBlock.stream()
                    .filter(q -> "Correct".equalsIgnoreCase(q.getCorrect()))
                    .count();
            double recentRate = (double) correctRecent / limit;
            if (recentRate < MIN_SUCCESS_RATE) {
                String message = "It appears you're struggling with " + subject + ". Consider extra practice.";
                createNotification(user, subject, message);
            }
            if (correctRecent == 0) {
                String message = "None of your recent " + subject + " questions were answered correctly. Please review the material.";
                createNotification(user, subject, message);
            }
        }

        if (!allQuestions.isEmpty()) {
            long totalCorrect = allQuestions.stream()
                    .filter(q -> "Correct".equalsIgnoreCase(q.getCorrect()))
                    .count();
            double overallRate = (double) totalCorrect / allQuestions.size();
            if (overallRate < OVERALL_THRESHOLD) {
                String message = "Your overall performance in " + subject + " is very low. Please seek additional help or review the material thoroughly.";
                createNotification(user, subject, message);
            }
        }

        if (allQuestions.size() >= 10) {
            List<QuestionEntity> lastTen = allQuestions.subList(0, 10);
            long correctLastTen = lastTen.stream()
                    .filter(q -> "Correct".equalsIgnoreCase(q.getCorrect()))
                    .count();
            double lastTenRate = (double) correctLastTen / 10;
            if (lastTenRate < 0.5) {
                String message = "Your performance in your last 10 " + subject + " questions is below 50%. Consider extra practice.";
                createNotification(user, subject, message);
            }
        }
    }

    private void createNotification(UserEntity user, String subject, String message) {
        List<ProgressNotificationEntity> existingNotifications = persist.getQuerySession()
                .createQuery("FROM ProgressNotificationEntity WHERE user = :user AND subject = :subject AND read = false", ProgressNotificationEntity.class)
                .setParameter("user", user)
                .setParameter("subject", subject)
                .getResultList();

        if (existingNotifications != null && !existingNotifications.isEmpty()) {
            return;
        }
        ProgressNotificationEntity notification = new ProgressNotificationEntity(user, subject, message);
        persist.saveNotification(notification);
    }
}
