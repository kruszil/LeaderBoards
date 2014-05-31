package com.ilona.leaderBoards.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.ilona.leaderBoards.model.Game;

public class GamesDAO {

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int insertGame(String name) {

		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate("INSERT INTO GAMES (GAME) VALUES ('"
					+ name + "')");
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

	public Game getGameByGameName(String name) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			ResultSet rset = statement
					.executeQuery("SELECT * FROM games WHERE game='" + name
							+ "'");
			while (rset.next()) {
				Game requestedGame = new Game(rset.getInt("games.id"),
						rset.getString("games.game"));
				return requestedGame;
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
