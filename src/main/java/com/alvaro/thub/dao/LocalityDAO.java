package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.LocalityCriteria;
import com.alvaro.thub.model.LocalityDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.SQLUtils;

public class LocalityDAO {

	private static Logger logger = LogManager.getLogger(LocalityDAO.class.getName());

	public LocalityDAO() {		
	}

	private static final String BASE_QUERY = 
			" SELECT l.id, l.name, l.province_id, p.name as province_name "
					+ " FROM LOCALITY l "
					+ " INNER JOIN PROVINCE p ON l.province_id = p.id ";

	/**
	 * Encontrar localidad por su id. 
	 * @param id
	 * @return
	 */
	public LocalityDTO findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {			

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE l.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);				
			rs = ps.executeQuery();

			LocalityDTO localidad = null; 	

			if (rs.next()) { 
				localidad = loadNext(rs);
			}

			return localidad; 	

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de localidades.", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}



	/**
	 * Encontrar localidades por criterios de búsqueda	
	 * @param criteria
	 * @return
	 */
	public List<LocalityDTO> findBy(Connection c, LocalityCriteria criteria) throws DataException {		

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " l.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getName(), condiciones, " UPPER(l.name) LIKE UPPER(?) ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getProvinceId(), condiciones, " l.province_id = ? ", parametros, criteria.getProvinceId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append(String.join(" AND ", condiciones));
			}

			sqlBuilder.append(" ORDER BY l.name");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);

			int i = 1;
			for (Object parameterValue : parametros) {
				ps.setObject(i++, parameterValue);
			}

			rs = ps.executeQuery();

			List<LocalityDTO> resultados = new ArrayList<LocalityDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de localidades.", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar localidades por provincia.
	 * @param provinceId
	 * @return
	 */
	public List<LocalityDTO> findByProvinceId(Connection c, Long provinceId) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE l.province_id = ? ");
			sqlBuilder.append(" ORDER BY l.name ");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);
			ps.setLong(1, provinceId);
			rs = ps.executeQuery();

			List<LocalityDTO> resultados = new ArrayList<LocalityDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de localidades.", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar todas las localidades.
	 * @return Lista de localidades.
	 */
	public List<LocalityDTO> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY l.name ");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();

			List<LocalityDTO> resultados = new ArrayList<LocalityDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de localidades.", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}



	// cargar una localidad desde el ResultSet
	private LocalityDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		LocalityDTO localidad = new LocalityDTO();

		localidad.setId(rs.getLong(i++));
		localidad.setName(rs.getString(i++));
		localidad.setProvinceId(rs.getLong(i++));
		localidad.setProvinceName(rs.getString(i++));

		return localidad;
	}
}