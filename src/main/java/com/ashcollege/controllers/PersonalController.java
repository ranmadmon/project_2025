package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.PersonalDetailsResponse;
import com.ashcollege.service.Persist;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class PersonalController {
    private final Persist persist;
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonalController(Persist persist, SessionFactory sessionFactory) {
        this.persist = persist;
        this.sessionFactory = sessionFactory;
    }

    @GetMapping("/my-details")
    @Transactional   // ← Ensure the Hibernate Session stays open for this call
    public PersonalDetailsResponse getMyDetails(@RequestParam String token) {
        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // 1) יכולות
        UserAbilityEntity ua = sessionFactory.getCurrentSession()
                .get(UserAbilityEntity.class, user.getId());
        List<String> abilities = ua == null
                ? Collections.emptyList()
                : Arrays.asList(ua.getAbilities().split(","));

        // 2) לקוחות מנוהלים
        List<String> clients = persist.getClientsByManager(user.getUsername())
                .stream()
                .map(ClientEntity::getName)
                .toList();

        // 3) שם הצוות
        TeamEntity team = user.getTeamId() > 0
                ? sessionFactory.getCurrentSession().get(TeamEntity.class, user.getTeamId())
                : null;
        String teamName = (team != null) ? team.getName() : "";

        // 4) האם ראש צוות
        boolean isLeader = (user.getRole().getId() == 2 || user.getRole().getId() == 4);
        String isLeaderYesOrNo = isLeader ? "כן" : "לא";

        // 5) שעות עבודה — עתה מחושב מסכום שעות ב־AssignmentParticipantEntity
        List<AssignmentParticipantEntity> parts = persist.getParticipantsForUserId(user.getId());
        int hoursWorked = parts.stream()
                .mapToInt(AssignmentParticipantEntity::getHours)
                .sum();

        return new PersonalDetailsResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                abilities,
                clients,
                teamName,
                isLeaderYesOrNo,
                hoursWorked
        );
    }


    @PostMapping("/update-username")
    @Transactional
    public BasicResponse updateUsername(@RequestParam String token,
                                        @RequestParam String newUsername) {
        BasicResponse resp = new BasicResponse();
        UserEntity me = persist.getUserByPass(token);
        if (me == null) {
            resp.setSuccess(false);
            resp.setErrorCode(1);
            return resp;
        }
        // בדיקה לא ריק וללא כפילויות
        if (newUsername.isBlank() || persist.getUserByUsername(newUsername) != null) {
            resp.setSuccess(false);
            resp.setErrorCode(2);
            return resp;
        }
        me.setUsername(newUsername);
        persist.save(me);
        resp.setSuccess(true);
        resp.setErrorCode(0);
        return resp;
    }

    @PostMapping("/update-email")
    @Transactional
    public BasicResponse updateEmail(@RequestParam String token,
                                     @RequestParam String newEmail) {
        BasicResponse resp = new BasicResponse();
        UserEntity me = persist.getUserByPass(token);
        if (me == null) {
            resp.setSuccess(false);
            resp.setErrorCode(1);
            return resp;
        }
        // תבנית דואר בסיסית + בדיקת כפילות
        if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")
                || persist.getUserByEmail(newEmail) != null) {
            resp.setSuccess(false);
            resp.setErrorCode(2);
            return resp;
        }
        me.setEmail(newEmail);
        persist.save(me);
        resp.setSuccess(true);
        resp.setErrorCode(0);
        return resp;
    }

    @PostMapping("/update-phone")
    @Transactional
    public BasicResponse updatePhone(@RequestParam String token,
                                     @RequestParam String newPhone) {
        BasicResponse resp = new BasicResponse();
        UserEntity me = persist.getUserByPass(token);
        if (me == null) {
            resp.setSuccess(false);
            resp.setErrorCode(1);
            return resp;
        }
        // תבנית לטלפון ישראלי (05XXXXXXXX) + בדיקת כפילות
        if (!newPhone.matches("^05\\d{8}$")
                || persist.getUserByPhone(newPhone) != null) {
            resp.setSuccess(false);
            resp.setErrorCode(2);
            return resp;
        }
        me.setPhoneNumber(newPhone);
        persist.save(me);
        resp.setSuccess(true);
        resp.setErrorCode(0);
        return resp;
    }
}
