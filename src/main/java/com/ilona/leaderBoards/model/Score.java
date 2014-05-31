package com.ilona.leaderBoards.model;

public class Score {
	private int id;
	private int score;
	private Game game;
	private User user;

	public Score(int id, int score, Game game, User user) {
		super();
		this.id = id;
		this.score = score;
		this.game = game;
		this.user = user;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
