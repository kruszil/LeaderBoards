package com.ilona.leaderBoards.controller;

import org.codehaus.jackson.annotate.JsonProperty;

public class ScoreEntry {
	@JsonProperty
	private String game;
	@JsonProperty
	private String user;
	@JsonProperty
	private int score;
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	

}
