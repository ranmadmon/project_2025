package com.ashcollege.entities;

import java.io.Serializable;
import java.util.Objects;

public class TeamMemberEntity implements Serializable {
    private int teamId;
    private int userId;

    public TeamMemberEntity() {}

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamMemberEntity)) return false;
        TeamMemberEntity that = (TeamMemberEntity) o;
        return teamId == that.teamId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, userId);
    }
}
