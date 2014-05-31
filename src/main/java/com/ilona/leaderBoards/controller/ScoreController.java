package com.ilona.leaderBoards.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ilona.leaderBoards.dao.GamesDAO;
import com.ilona.leaderBoards.dao.ScoresDAO;
import com.ilona.leaderBoards.dao.UsersDAO;
import com.ilona.leaderBoards.model.Score;

@RestController
@RequestMapping("/score")
public class ScoreController {

	@Autowired
	private ScoresDAO scoresDAO;
	@Autowired
	private UsersDAO usersDAO;
	@Autowired
	private GamesDAO gamesDAO;

	@RequestMapping(value = "/{game}", method = RequestMethod.GET)
	public List<ScoreEntry> getBestScores(@PathVariable String game) {
		List<ScoreEntry> scoreList = new ArrayList<>();
		List<Score> scores = scoresDAO.getBestScoresByGame(game);
		if (game == null) {
			throw new RuntimeException("Game is null");
		}
		if (game.equals("")) {
			throw new RuntimeException("Game is empty");
		}
		if(scores==null){
			throw new RuntimeException("List is null");
		}
		
		for (Score score : scores) {
			ScoreEntry scoreEntry = new ScoreEntry();
			scoreEntry.setGame(score.getGame().getName());
			scoreEntry.setUser(score.getUser().getUsername());
			scoreEntry.setScore(score.getScore());
			scoreList.add(scoreEntry);
		}
		return scoreList;

	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveScores(@RequestBody ScoreEntry scoreEntry) {
		// check request
		if (scoreEntry == null) {
			throw new RuntimeException("Score entry is null");
		}
		if (scoreEntry.getGame() == null) {
			throw new RuntimeException("Game entry is null");
		}
		if (scoreEntry.getUser() == null) {
			throw new RuntimeException("User entry is null");
		}
		if (scoreEntry.getGame().equals("")) {
			throw new RuntimeException("Game entry is empty");
		}
		if (scoreEntry.getUser().equals("")) {
			throw new RuntimeException("User entry is empty");
		}
		if (scoreEntry.getScore() <= 0) {
			throw new RuntimeException("Score entry is less or equal zero");
		}
		// System.out.println(scoreEntry.getScore());
		if (gamesDAO.getGameByGameName(scoreEntry.getGame()) == null) {
			gamesDAO.insertGame(scoreEntry.getGame());
		}
		if (usersDAO.getUserByUsername(scoreEntry.getUser()) == null) {
			usersDAO.insertUser(scoreEntry.getUser());
		}
		scoresDAO.insertScore(scoreEntry.getGame(), scoreEntry.getUser(),
				scoreEntry.getScore());
	}

	public void setScoresDAO(ScoresDAO scoresDAO) {
		this.scoresDAO = scoresDAO;
	}

	public void setUsersDAO(UsersDAO usersDAO) {
		this.usersDAO = usersDAO;
	}

	public void setGamesDAO(GamesDAO gamesDAO) {
		this.gamesDAO = gamesDAO;
	}
}
