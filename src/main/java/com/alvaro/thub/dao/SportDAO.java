package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Sport;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class SportDAO {

	private static Logger logger = LogManager.getLogger(SportDAO.class.getName());

	public SportDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT sport.id, sport.name "
					+ " FROM SPORT sport ";

	/**
	 * Encontrar deporte por su id.
	 * @param id
	 * @return sport
	 */
	public Sport findById(Connection c,Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE sport.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			Sport sport = null;

			if (rs.next()) {
				sport = loadNext(rs);
			}
			return sport;

		} catch (SQLException e) {
			logger.error("Error finding sport by id: " + id, e);
			throw new DataException("Error finding sport by id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todos los deportes
	 * @return
	 */
	public List<Sport> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY sport.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Sport> resultados = new ArrayList<Sport>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error finding all sports", e);
			throw new DataException("Error finding all sports", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Buscar deportes por nombre (búsqueda parcial)
	 * @param name
	 * @return
	 */
	public List<Sport> findByName(Connection c, String name) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE UPPER(sport.name) LIKE UPPER(?) ");
			sqlBuilder.append(" ORDER BY sport.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, "%" + name + "%");              
			rs = ps.executeQuery();

			List<Sport> resultados = new ArrayList<Sport>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error finding sports by name: " + name, e);
			throw new DataException("Error finding sports by name: " + name, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}




	// cargar un deporte desde el ResultSet
	private Sport loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Sport sport = new Sport();

		sport.setId(rs.getLong(i++));
		sport.setName(rs.getString(i++));

		return sport;
	}
}