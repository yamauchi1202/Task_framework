package jp.co.axiz.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.co.axiz.web.entity.UserInfo;
import jp.co.axiz.web.exception.DataAccessException;
import jp.co.axiz.web.util.DbUtil;

public class PgUserInfoDao {

	public List<UserInfo> findAll() {
		ArrayList<UserInfo> list = new ArrayList<>();

		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT user_id, user_name, telephone, password FROM user_info ORDER BY user_id")) {
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserInfo u = new UserInfo();
				u.setId(rs.getInt("user_id"));
				u.setName(rs.getString("user_name"));
				u.setTelephone(rs.getString("telephone"));
				u.setPassword(rs.getString("password"));
				list.add(u);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	public List<UserInfo> find(UserInfo cond) {
		ArrayList<String> whereCond = new ArrayList<>();
		ArrayList<Object> param = new ArrayList<>();

		if (cond.getId() != null) {
			whereCond.add("user_id = ?");
			param.add(cond.getId());
		}
		if (cond.getName() != null && !cond.getName().isEmpty()) {
			whereCond.add("user_name = ?");
			param.add(cond.getName());
		}
		if (cond.getTelephone() != null && !cond.getTelephone().isEmpty()) {
			whereCond.add("telephone = ?");
			param.add(cond.getTelephone());
		}

		if (whereCond.isEmpty()) {
			return findAll();
		}

		String whereString = String.join(" AND ", whereCond.toArray(new String[]{}));
		ArrayList<UserInfo> list = new ArrayList<>();

		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT user_id, user_name, telephone, password FROM user_info WHERE " + whereString + " ORDER BY user_id")) {
			for (int i = 0; i < param.size(); i++) {
				// ? は 1 origin
				stmt.setObject(i + 1, param.get(i));
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserInfo u = new UserInfo();
				u.setId(rs.getInt("user_id"));
				u.setName(rs.getString("user_name"));
				u.setTelephone(rs.getString("telephone"));
				u.setPassword(rs.getString("password"));
				list.add(u);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return list;
	}

	public UserInfo findById(int id) {
		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT user_id, user_name, telephone, password FROM user_info WHERE user_id = ?")) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				UserInfo u = new UserInfo();
				u.setId(rs.getInt("user_id"));
				u.setName(rs.getString("user_name"));
				u.setTelephone(rs.getString("telephone"));
				u.setPassword(rs.getString("password"));
				return u;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return null;
	}

	public int register(UserInfo user) {
		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("INSERT INTO user_info (user_name, telephone, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getTelephone());
			stmt.setString(3, user.getPassword());

			int result = stmt.executeUpdate();

			ResultSet gkey = stmt.getGeneratedKeys();
			gkey.next();
			user.setId(gkey.getInt(1));

			return result;
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public int update(UserInfo user) {
		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("UPDATE user_info SET user_name = ?, telephone = ?, password = ? WHERE user_id = ?")) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getTelephone());
			stmt.setString(3, user.getPassword());
			stmt.setInt(4, user.getId());

			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public int delete(int id) {
		try (Connection con = DbUtil.getConnection();
				PreparedStatement stmt = con.prepareStatement("DELETE FROM user_info WHERE user_id = ?")) {
			stmt.setInt(1, id);

			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

}

