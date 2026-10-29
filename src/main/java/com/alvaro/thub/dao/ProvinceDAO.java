package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.ProvinceCriteria;
import com.alvaro.thub.model.Country;
import com.alvaro.thub.model.ProvinceDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.SQLUtils;

public class ProvinceDAO {

	private static Logger logger = LogManager.getLogger(ProvinceDAO.class.getName());

	public ProvinceDAO() {		
	}

	private static final String BASE_QUERY = 
			" SELECT p.id, p.name, p.country_id, c.name as country_name "
					+ " FROM PROVINCE p "
					+ " INNER JOIN COUNTRY c ON p.country_id = c.id ";

	/**
	 * Encontrar provincia por su id. 
	 * @param id
	 * @return
	 */
	public ProvinceDTO findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {			

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE p.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);				
			rs = ps.executeQuery();

			ProvinceDTO provincia = null; 	

			if (rs.next()) { 
				provincia = loadNext(rs);
			}

			return provincia; 	

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de la provincia con id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	/**
	 * Encontrar provincias por criterios de búsqueda	
	 * @param criteria
	 * @return
	 */
	public List<ProvinceDTO> findBy(Connection c, ProvinceCriteria criteria) throws DataException {		

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " p.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getName(), condiciones, " UPPER(p.name) LIKE UPPER(?) ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getCountryId(), condiciones, " p.country_id = ? ", parametros, criteria.getCountryId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append(String.join(" AND ", condiciones));
			}

			sqlBuilder.append(" ORDER BY p.name");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);

			int i = 1;
			for (Object parameterValue : parametros) {
				ps.setObject(i++, parameterValue);
			}

			rs = ps.executeQuery();

			List<ProvinceDTO> resultados = new ArrayList<ProvinceDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de las provincias con criterios: " + criteria, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar provincias por país.
	 * @param countryId
	 * @return
	 */
	public List<ProvinceDTO> findByCountryId(Connection c, Long countryId) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE p.country_id = ? ");
			sqlBuilder.append(" ORDER BY p.name ");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);
			ps.setLong(1, countryId);
			rs = ps.executeQuery();

			List<ProvinceDTO> resultados = new ArrayList<ProvinceDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
			throw new DataException("Error al acceder a los datos de las provincias del país con id: " + countryId, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}
	/**
	 * Encontrar todos laas provincias. Se ordena por nombre para que el resultado se muestre ordenado alfabéticamente.
	 * @return
	 */
	public List<ProvinceDTO> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			// Se ordena por nombre para que el resultado se muestre ordenado alfabéticamente.
            // esto se hace en porque no tiene criteria
			sqlBuilder.append(" ORDER BY p.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<ProvinceDTO> resultados = new ArrayList<ProvinceDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar todos las provincias: {}", e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	// cargar una provincia desde el ResultSet
	private ProvinceDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		ProvinceDTO provincia = new ProvinceDTO();

		provincia.setId(rs.getLong(i++));
		provincia.setName(rs.getString(i++));
		provincia.setCountryId(rs.getLong(i++));
		provincia.setCountryName(rs.getString(i++));

		return provincia;
	}
}