package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Rol;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class RolDAO {

	private static Logger logger = LogManager.getLogger(RolDAO.class.getName());

	public RolDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT rol.id, rol.name "
					+ " FROM ROL rol ";

	/**
	 * Encontrar rol por su id.
	 * @param id
	 * @return rol
	 */
	public Rol findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE rol.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			Rol rol = null;

			if (rs.next()) {
				rol = loadNext(rs);
			}
			return rol;

		} catch (SQLException e) {
			logger.error("Error al buscar el rol por id: " + id, e);
			throw new DataException("Error al buscar el rol por id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todos los roles
	 * @return
	 */
	public List<Rol> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY rol.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Rol> resultados = new ArrayList<Rol>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar todos los roles", e);
			throw new DataException("Error al buscar todos los roles", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Buscar roles por nombre (búsqueda parcial)
	 * @param name
	 * @return
	 */
	public List<Rol> findByName(Connection c,String name) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE UPPER(rol.name) LIKE UPPER(?) ");
			sqlBuilder.append(" ORDER BY rol.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, "%" + name + "%");              
			rs = ps.executeQuery();

			List<Rol> resultados = new ArrayList<Rol>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar los roles por nombre: " + name, e);
			throw new DataException("Error al buscar los roles por nombre: " + name, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}



	// cargar un rol desde el ResultSet
	private Rol loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Rol rol = new Rol();

		rol.setId(rs.getLong(i++));
		rol.setName(rs.getString(i++));

		return rol;
	}
}