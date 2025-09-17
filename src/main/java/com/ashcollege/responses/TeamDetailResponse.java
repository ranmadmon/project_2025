package com.ashcollege.responses;

import java.util.List;

public record TeamDetailResponse(
        int id,
        String name,
        String leaderUsername,
        List<String> memberUsernames,
        List<WorkerSimple> availableWorkers
) {}
