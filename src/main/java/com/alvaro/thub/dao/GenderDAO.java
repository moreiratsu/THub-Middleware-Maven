package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Gender;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class GenderDAO {

	private static Logger logger = LogManager.getLogger(GenderDAO.class.getName());

	public GenderDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT gender.id, gender.name "
					+ " FROM GENDER gender ";

	/**
	 * Encontrar género por su id.
	 * @param id
	 * @return gender
	 */
	public Gender findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE gender.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			Gender gender = null;

			if (rs.next()) {
				gender = loadNext(rs);
			}
			return gender;

		} catch (SQLException e) {
			logger.error("Error finding gender by id: " + id, e);
			throw new DataException("Error finding gender by id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todos los géneros
	 * @return
	 */
	public List<Gender> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY gender.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Gender> resultados = new ArrayList<Gender>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error finding all genders", e);
			throw new DataException ("Error finding all genders ", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}




	// cargar un género desde el ResultSet
	private Gender loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Gender gender = new Gender();

		gender.setId(rs.getLong(i++));
		gender.setName(rs.getString(i++));

		return gender;
	}
}