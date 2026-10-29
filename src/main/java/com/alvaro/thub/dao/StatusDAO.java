package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Status;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class StatusDAO {

	private static Logger logger = LogManager.getLogger(StatusDAO.class.getName());

	public StatusDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT status.id, status.name "
					+ " FROM STATUS status ";

	/**
	 * Encontrar estado por su id.
	 * @param id
	 * @return status
	 */
	public Status findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE status.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			Status status = null;

			if (rs.next()) {
				status = loadNext(rs);
			}
			return status;

		} catch (SQLException e) {
			logger.error("Error al buscar el estado por id: " + id, e);
			throw new DataException("Error al buscar el estado por id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todos los estados
	 * @return
	 */
	public List<Status> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY status.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Status> resultados = new ArrayList<Status>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar todos los estados", e);
			throw new DataException("Error al buscar todos los estados", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Buscar estados por nombre (búsqueda parcial)
	 * @param name
	 * @return
	 */
	public List<Status> findByName(Connection c, String name) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE UPPER(status.name) LIKE UPPER(?) ");
			sqlBuilder.append(" ORDER BY status.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, "%" + name + "%");              
			rs = ps.executeQuery();

			List<Status> resultados = new ArrayList<Status>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar los estados por nombre: " + name, e);
			throw new DataException("Error al buscar los estados por nombre: " + name, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}




	// cargar un estado desde el ResultSet
	private Status loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Status status = new Status();

		status.setId(rs.getLong(i++));
		status.setName(rs.getString(i++));

		return status;
	}
}