package com.ilona.leaderBoards.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.ilona.leaderBoards.model.Game;
import com.ilona.leaderBoards.model.User;

public class UsersDAO {
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int insertUser(String username) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate("INSERT INTO USERS (USERNAME) VALUES ('"
					+ username + "')");
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
	
	public User getUserByUsername(String username) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			ResultSet rset = statement
					.executeQuery("SELECT * FROM users WHERE username='" + username
							+ "'");
			while (rset.next()) {
				User requestedUser = new User(rset.getInt("users.id"),
						rset.getString("users.username"));
				return requestedUser;
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
