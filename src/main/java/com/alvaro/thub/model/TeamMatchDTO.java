package com.alvaro.thub.model;

/*
 * En esta clase tenemos que hacer un DTO porque a parte de los id foraneos tiene un atributo propio
 */

public class TeamMatchDTO extends AbstractValueObject{
	
	private Long TeamId = null;
	private String TeamName = null;
	
	private Long MatchId = null;
	private String MatchDate = null;
	private String MatchRound = null;
	
	private String score = null;
	
	public TeamMatchDTO() {
	}

	public Long getTeamId() {
		return TeamId;
	}

	public void setTeamId(Long teamId) {
		TeamId = teamId;
	}

	public String getTeamName() {
		return TeamName;
	}

	public void setTeamName(String teamName) {
		TeamName = teamName;
	}

	public Long getMatchId() {
		return MatchId;
	}

	public void setMatchId(Long matchId) {
		MatchId = matchId;
	}

	public String getMatchDate() {
		return MatchDate;
	}

	public void setMatchDate(String matchDate) {
		MatchDate = matchDate;
	}

	public String getMatchRound() {
		return MatchRound;
	}

	public void setMatchRound(String matchRound) {
		MatchRound = matchRound;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	
}
