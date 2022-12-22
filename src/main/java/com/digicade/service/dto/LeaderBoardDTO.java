package com.digicade.service.dto;

import com.digicade.domain.GameScore;
import java.util.HashSet;
import java.util.Set;

public class LeaderBoardDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Set<GameScore> gameScores = new HashSet<>();

    public LeaderBoardDTO() {}

    public LeaderBoardDTO(Long id, String firstName, String lastName, Set<GameScore> gameScores) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gameScores = gameScores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<GameScore> getGameScores() {
        return gameScores;
    }

    public void setGameScores(Set<GameScore> gameScores) {
        this.gameScores = gameScores;
    }
}
