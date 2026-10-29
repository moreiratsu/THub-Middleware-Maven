package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Format;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class FormatDAO {

	private static Logger logger = LogManager.getLogger(FormatDAO.class.getName());

	public FormatDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT format.id, format.name "
					+ " FROM FORMAT format ";

	/**
	 * Encontrar formato por su id.
	 * @param id
	 * @return format
	 */
	public Format findById(Connection c,Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE format.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			Format format = null;

			if (rs.next()) {
				format = loadNext(rs);
			}
			return format;

		} catch (SQLException e) {
			logger.error("Error al encontrar el formato con id " + id, e);
			throw new DataException("Error al encontrar el formato con id " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todos los formatos
	 * @return
	 */
	public List<Format> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY format.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Format> resultados = new ArrayList<Format>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al encontrar los formatos", e);
			throw new DataException("Error al encontrar los formatos", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	// cargar un formato desde el ResultSet
	private Format loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Format format = new Format();

		format.setId(rs.getLong(i++));
		format.setName(rs.getString(i++));

		return format;
	}
}