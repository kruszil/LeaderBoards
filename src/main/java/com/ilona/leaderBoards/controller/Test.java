package com.ilona.leaderBoards.controller;

import com.ilona.leaderBoards.dao.GamesDAO;
import com.ilona.leaderBoards.dao.UsersDAO;
import com.ilona.leaderBoards.dao.ScoresDAO;


public class Test {
	
	public GamesDAO gamesDAO;
	public UsersDAO usersDAO;
	public ScoresDAO scoresDAO;
	
	public Test(GamesDAO gamesDAO, UsersDAO usersDAO, ScoresDAO scoresDAO) {
		System.out.println("I HAVE BEEN CREATED");
		this.gamesDAO=gamesDAO;
		this.usersDAO=usersDAO;
		this.scoresDAO=scoresDAO;
		gamesDAO.insertGame("tictactoe");
		usersDAO.insertUser("player1");
		scoresDAO.insertScore("tictactoe", "player2", 1000);
		
		
	}

}
