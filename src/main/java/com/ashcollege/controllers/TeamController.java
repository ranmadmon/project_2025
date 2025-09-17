package com.ashcollege.controllers;

import com.ashcollege.entities.TeamEntity;
import com.ashcollege.entities.UserEntity;
import com.ashcollege.responses.TeamDetailResponse;
import com.ashcollege.responses.TeamSummaryResponse;
import com.ashcollege.responses.WorkerSimple;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {

    @Autowired
    private Persist persist;

    // 1. רשימת הצוותים
    @GetMapping("/teams")
    public List<TeamSummaryResponse> listTeams(@RequestParam String token) {
        return persist.loadList(TeamEntity.class).stream()
                .map(t -> new TeamSummaryResponse(t.getId(), t.getName()))
                .toList();
    }

    // 2. פרטי צוות
    @GetMapping("/teams/{id}")
    public TeamDetailResponse teamDetail(
            @PathVariable int id,
            @RequestParam String token
    ) {
        TeamEntity team = persist.loadObject(TeamEntity.class, id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }

        String leader = team.getHead().getUsername();

        // חברי צוות (לפי teamId, בלי ה־leader)
        List<String> memberNames = persist.loadList(UserEntity.class).stream()
                .filter(u -> u.getTeamId() == id && !u.getUsername().equals(leader))
                .map(UserEntity::getUsername)
                .toList();

        // רשימת כל העובדים להוספה (כולל מידע על roleId ו־teamId)
        List<WorkerSimple> all = persist.getAllWorkersWithData().stream()
                .map(w -> new WorkerSimple(w.getUsername(), w.getRoleId(), w.getTeamId()))
                .toList();

        return new TeamDetailResponse(
                id,
                team.getName(),
                leader,
                memberNames,
                all
        );
    }

    // 3. הוספת חבר
    @PostMapping("/teams/{id}/members")
    public void addMember(
            @PathVariable int id,
            @RequestParam String token,
            @RequestParam String username
    ) {
        UserEntity user = persist.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        user.setTeamId(id);
        persist.save(user);
    }

    // 4. החלפת ראש צוות
    @PostMapping("/teams/{id}/leader")
    public void changeLeader(
            @PathVariable int id,
            @RequestParam String token,
            @RequestParam String newLeaderUsername
    ) {
        TeamEntity team = persist.loadObject(TeamEntity.class, id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }

        String oldLeader = team.getHead().getUsername();
        UserEntity old = persist.getUserByUsername(oldLeader);
        UserEntity neu = persist.getUserByUsername(newLeaderUsername);

        if (old == null || neu == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        // עדכון תפקידים
        int oldRole = old.getRole().getId();
        if (oldRole == 2) old.getRole().setId(1);
        else if (oldRole == 4) old.getRole().setId(3);

        int newRole = neu.getRole().getId();
        if (newRole == 1) neu.getRole().setId(2);
        else if (newRole == 3) neu.getRole().setId(4);

        // החלפת ה־head
        team.setHead(neu);

        persist.save(old);
        persist.save(neu);
        persist.save(team);
    }

    // 5. מחיקת צוות (רק אם ריק)
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable int id, @RequestParam String token) {
        // שליפה
        TeamEntity team = persist.loadObject(TeamEntity.class, id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }

        // בדיקה אם יש משתמשים בצוות
        boolean hasMembers = persist.loadList(UserEntity.class).stream()
                .anyMatch(u -> u.getTeamId() == id && !u.getUsername().equals(team.getHead().getUsername()));
        if (hasMembers) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete non-empty team");
        }

        // שליפת ראש הצוות
        UserEntity leader = team.getHead();
        int roleId = leader.getRole().getId();

        // עדכון תפקיד ראש הצוות
        if (roleId == 2) {
            leader.getRole().setId(1); // teamLeader → worker
        } else if (roleId == 4) {
            leader.getRole().setId(3); // admin teamLeader → admin worker
        }
        leader.setTeamId(0); // להסיר מהצוות
        persist.save(leader);

        // מחיקת הצוות
        persist.remove(team);
    }

}
