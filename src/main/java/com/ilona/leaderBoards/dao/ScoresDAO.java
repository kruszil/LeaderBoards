package com.ilona.leaderBoards.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ilona.leaderBoards.model.Game;
import com.ilona.leaderBoards.model.Score;
import com.ilona.leaderBoards.model.User;

public class ScoresDAO {
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int insertScore(String game, String user, int score) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			return statement
					.executeUpdate("INSERT INTO SCORES (game_id, user_id, score) VALUES((SELECT id FROM games WHERE game='"
							+ game
							+ "'), (SELECT id FROM users WHERE username='"
							+ user + "'), " + score + ")");
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;

	}

	public List<Score> getBestScoresByGame(String game) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			ResultSet rset = statement
					.executeQuery("SELECT TOP 10 * FROM games, users, scores WHERE games.game='"
							+ game
							+ "' AND games.id=scores.game_id AND users.id=scores.user_id ORDER BY scores.score DESC");
			List<Score> scores = new ArrayList<>();
			while (rset.next()) {
				Game requestedGame = new Game(rset.getInt("games.id"),
						rset.getString("games.game"));
				User user = new User(rset.getInt("users.id"),
						rset.getString("users.username"));

				Score score = new Score(rset.getInt("scores.id"),
						rset.getInt("scores.score"), requestedGame, user);
				scores.add(score);
			}
			return scores;

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	// Checking if a new score is equal or less than the score already existing
	// in the database for particular game and the user.
	public Score checkScore(String game, String user, int score) {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			ResultSet rset = statement
					.executeQuery("SELECT * FROM games, users, scores WHERE "
							+ "games.id = scores.game_id AND users.id = scores.user_id AND "
							+ "games.game='"
							+ game
							+ "' AND users.username='"
							+ user
							+ "' AND scores.score>=" + score);
			while (rset.next()) {
				Game requestedGame = new Game(rset.getInt("games.id"),
						rset.getString("games.game"));
				User requestedUser = new User(rset.getInt("users.id"),
						rset.getString("users.username"));

				Score requestedScore = new Score(rset.getInt("scores.id"),
						rset.getInt("scores.score"), requestedGame,
						requestedUser);
				return requestedScore;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
