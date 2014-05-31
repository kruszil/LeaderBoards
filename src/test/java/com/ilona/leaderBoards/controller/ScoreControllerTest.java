package com.ilona.leaderBoards.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.ilona.leaderBoards.dao.GamesDAO;
import com.ilona.leaderBoards.dao.ScoresDAO;
import com.ilona.leaderBoards.dao.UsersDAO;
import com.ilona.leaderBoards.model.Game;
import com.ilona.leaderBoards.model.Score;
import com.ilona.leaderBoards.model.User;

public class ScoreControllerTest {

	private ScoreController scoreController;
	private GamesDAO gamesDAO;
	private UsersDAO usersDAO;
	private ScoresDAO scoresDAO;

	@Before
	public void init() {
		scoreController = new ScoreController();
		gamesDAO = Mockito.mock(GamesDAO.class);
		usersDAO = Mockito.mock(UsersDAO.class);
		scoresDAO = Mockito.mock(ScoresDAO.class);
		scoreController.setGamesDAO(gamesDAO);
		scoreController.setUsersDAO(usersDAO);
		scoreController.setScoresDAO(scoresDAO);
	}

	@Test
	public void testGetBestScoresReturnedListIsNull() {
		Mockito.when(scoresDAO.getBestScoresByGame(Matchers.eq("chess")))
				.thenReturn(null);
		try {
			scoreController.getBestScores("chess");
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("List is null", exception.getMessage());
			Mockito.verify(scoresDAO, Mockito.times(1)).getBestScoresByGame(
					Matchers.anyString());
		} catch (Exception e) {
			Assert.fail();
		}

	}

	@Test
	public void testGetBestScoresReturnedListIsEmpty() {
		// List<Score> scores = new ArrayList<>();
		Mockito.when(scoresDAO.getBestScoresByGame(Matchers.eq("chess")))
				.thenReturn(new ArrayList<Score>());
		List<ScoreEntry> scoreEntries = scoreController.getBestScores("chess");
		Mockito.verify(scoresDAO, Mockito.times(1)).getBestScoresByGame(
				Matchers.anyString());
		Assert.assertNotNull(scoreEntries);
		Assert.assertEquals(0, scoreEntries.size());
	}

	@Test
	public void testGetBestScoresReturnedListHasManyRecords() {
		List<Score> scores = new ArrayList<>();
		scores.add(new Score(1, 10, new Game(1, "tictactoe"), new User(1, "John")));
		scores.add(new Score(2, 20, new Game(1, "tictactoe"), new User(2, "Paul")));
		Mockito.when(scoresDAO.getBestScoresByGame(Matchers.eq("chess")))
				.thenReturn(scores);
		List<ScoreEntry> scoreEntries = scoreController.getBestScores("chess");
		Mockito.verify(scoresDAO, Mockito.times(1)).getBestScoresByGame(
				Matchers.anyString());
		Assert.assertNotNull(scoreEntries);
		Assert.assertEquals(2, scoreEntries.size());
	}

	@Test
	public void testGetBestScoresGameIsNull() {
		try {
			scoreController.getBestScores(null);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Game is null", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetBestScoresGameIsEmpty() {
		try {
			scoreController.getBestScores("");
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Game is empty", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoresGameUserNotInDatabase() {
		Mockito.when(gamesDAO.getGameByGameName(Matchers.eq("tictactoe")))
				.thenReturn(null);
		Mockito.when(gamesDAO.getGameByGameName(Matchers.eq("chess")))
				.thenReturn(new Game(1, "chess"));
		Mockito.when(gamesDAO.insertGame(Matchers.eq("tictactoe"))).thenReturn(
				1);
		Mockito.when(usersDAO.getUserByUsername(Matchers.eq("John")))
				.thenReturn(null);
		Mockito.when(usersDAO.insertUser(Matchers.eq("John"))).thenReturn(1);
		Mockito.when(
				scoresDAO.insertScore(Matchers.eq("tictactoe"),
						Matchers.eq("John"), Matchers.eq(100))).thenReturn(1);
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setGame("tictactoe");
		scoreEntry.setScore(100);
		scoreEntry.setUser("John");
		scoreController.saveScores(scoreEntry);
		Mockito.verify(gamesDAO, Mockito.times(1)).insertGame(
				Matchers.anyString());
		Mockito.verify(usersDAO, Mockito.times(1)).insertUser(
				Matchers.anyString());
		Mockito.verify(scoresDAO, Mockito.times(1)).insertScore(
				Matchers.anyString(), Matchers.anyString(), Matchers.anyInt());
	}

	@Test
	public void testSaveScoresGameUserInDatabase() {
		Mockito.when(gamesDAO.getGameByGameName(Matchers.eq("tictactoe")))
				.thenReturn(new Game(1, "tictactoe"));
		Mockito.when(gamesDAO.insertGame(Matchers.eq("tictactoe"))).thenReturn(
				1);
		Mockito.when(usersDAO.getUserByUsername(Matchers.eq("John")))
				.thenReturn(new User(1, "John"));
		Mockito.when(usersDAO.insertUser(Matchers.eq("John"))).thenReturn(1);
		Mockito.when(
				scoresDAO.insertScore(Matchers.eq("tictactoe"),
						Matchers.eq("John"), Matchers.eq(100))).thenReturn(1);
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setGame("tictactoe");
		scoreEntry.setScore(100);
		scoreEntry.setUser("John");
		scoreController.saveScores(scoreEntry);
		Mockito.verify(gamesDAO, Mockito.times(0)).insertGame(
				Matchers.anyString());
		Mockito.verify(usersDAO, Mockito.times(0)).insertUser(
				Matchers.anyString());
		Mockito.verify(scoresDAO, Mockito.times(1)).insertScore(
				Matchers.anyString(), Matchers.anyString(), Matchers.anyInt());
	}

	@Test
	public void testSaveScoresScoreEntryIsNull() {
		try {
			scoreController.saveScores(null);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Score entry is null", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoreScoresEntryGameIsNull() {
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setGame(null);
		scoreEntry.setUser("John");
		scoreEntry.setScore(5);
		try {
			scoreController.saveScores(scoreEntry);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Game entry is null", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoresEntryUserIsNull() {
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setUser(null);
		scoreEntry.setGame("tictactoe");
		scoreEntry.setScore(1);
		try {
			scoreController.saveScores(scoreEntry);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("User entry is null", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoreScoresEntryGameIsEmpty() {
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setGame("");
		scoreEntry.setUser("John");
		scoreEntry.setScore(2);
		try {
			scoreController.saveScores(scoreEntry);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Game entry is empty", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoresEntryUserIsEmpty() {
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setUser("");
		scoreEntry.setGame("chess");
		scoreEntry.setScore(10);
		try {
			scoreController.saveScores(scoreEntry);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("User entry is empty", exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSaveScoresEntryScoreIsLessThanZero() {
		ScoreEntry scoreEntry = new ScoreEntry();
		scoreEntry.setScore(-1);
		scoreEntry.setGame("chess");
		scoreEntry.setUser("John");
		try {
			scoreController.saveScores(scoreEntry);
			Assert.fail();
		} catch (RuntimeException exception) {
			Assert.assertEquals("Score entry is less or equal zero",
					exception.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
