package com.digicade.service.dto;

import java.util.Set;

public class LeaderBoard {

    private Set<LeaderBoardDTO> leaderBoardDTOS;

    public LeaderBoard() {}

    public LeaderBoard(Set<LeaderBoardDTO> leaderBoardDTOS) {
        this.leaderBoardDTOS = leaderBoardDTOS;
    }

    public Set<LeaderBoardDTO> getLeaderBoardDTOS() {
        return leaderBoardDTOS;
    }

    public void setLeaderBoardDTOS(Set<LeaderBoardDTO> leaderBoardDTOS) {
        this.leaderBoardDTOS = leaderBoardDTOS;
    }
}
