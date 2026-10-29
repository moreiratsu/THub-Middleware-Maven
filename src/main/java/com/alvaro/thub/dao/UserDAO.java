package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.UserCriteria;
import com.alvaro.thub.model.UserDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;
import com.alvaro.thub.utils.SQLUtils;

public class UserDAO {

	private static Logger logger = LogManager.getLogger(UserDAO.class.getName());

	public UserDAO() {		
	}

	// Cambiamos a LEFT JOIN para permitir usuarios sin localidad o género asignado al registrarse
	private static final String BASE_QUERY = 			
			" SELECT user.id, user.dni, user.name, user.last_name1, user.last_name2, "
					+ " user.born_date, user.phone_number, user.email, user.password, "
					+ " user.locality_id, locality.name as locality_name, "
					+ " province.id as province_id, province.name as province_name, "
					+ " country.id as country_id, country.name as country_name, "
					+ " user.rol_id, rol.name as rol_name, "
					+ " user.gender_id, gender.name as gender_name "
					+ " FROM USER user "
					+ " LEFT JOIN LOCALITY locality ON user.locality_id = locality.id "
					+ " LEFT JOIN PROVINCE province ON locality.province_id = province.id "
					+ " LEFT JOIN COUNTRY country ON province.country_id = country.id "
					+ " LEFT JOIN ROL rol ON user.rol_id = rol.id "
					+ " LEFT JOIN GENDER gender ON user.gender_id = gender.id ";

	/**
	 * Encontrar usuario por su id. 
	 */
	public UserDTO findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {		
			StringBuilder sql = new StringBuilder(BASE_QUERY);
			sql.append(" WHERE user.id = ? ");

			ps = c.prepareStatement(sql.toString());
			DAOUtils.setParameters(ps, id);				
			rs = ps.executeQuery(); 

			UserDTO usuario = null; 

			if (rs.next()) { 
				usuario = loadNext(rs);
			}

			return usuario;	

		} catch (SQLException e) {
			logger.error("Error finding user by id: {}, message: {} ", id, e.getMessage(), e);
			throw new DataException("Error finding user by id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Encontrar usuario por su email. 
	 */
	public UserDTO findByEmail(Connection c, String email) throws DataException {
		UserCriteria criteria = new UserCriteria();
		criteria.setEmail(email);

		List<UserDTO> results = findBy(c, criteria, 1, Integer.MAX_VALUE).getPage();

		if (results != null && !results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}			
	}

	/**
	 * Encontrar usuarios por criterios de búsqueda.
	 */
	public Results<UserDTO> findBy(Connection c, UserCriteria criteria, int from, int pageSize) throws DataException {	

		if (logger.isInfoEnabled()) {
			logger.info("Criteria: {}", criteria);
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		Results<UserDTO> results = new Results<UserDTO>();

		try {		
			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();	

			SQLUtils.addClause(criteria.getId(), condiciones, " user.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getName(), condiciones, " UPPER(user.name) LIKE UPPER(?) ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getDni(), condiciones, " UPPER(user.dni) LIKE UPPER(?) ", parametros, "%" + criteria.getDni() + "%");
				SQLUtils.addClause(criteria.getEmail(), condiciones, " UPPER(user.email) LIKE UPPER(?) ", parametros, "%" + criteria.getEmail() + "%");
				SQLUtils.addClause(criteria.getLocalityId(), condiciones, " user.locality_id = ? ", parametros, criteria.getLocalityId());
				SQLUtils.addClause(criteria.getProvinceId(), condiciones, " province.id = ? ", parametros, criteria.getProvinceId());
				SQLUtils.addClause(criteria.getCountryId(), condiciones, " country.id = ? ", parametros, criteria.getCountryId());
				SQLUtils.addClause(criteria.getRolId(), condiciones, " user.rol_id = ? ", parametros, criteria.getRolId());
				SQLUtils.addClause(criteria.getGenderId(), condiciones, " user.gender_id = ? ", parametros, criteria.getGenderId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append(String.join(" AND ", condiciones));
			}

			sqlBuilder.append(" ORDER BY ").append(criteria.getOrderBy()).append(criteria.isAscDesc() ? " ASC " : " DESC ");

			String sql = sqlBuilder.toString();

			ps = c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;
			for (Object parameterValue : parametros) {
				ps.setObject(i++, parameterValue);
			}

			rs = ps.executeQuery();

			List<UserDTO> paginaDeResultados = new ArrayList<UserDTO>();

			if (rs.next()) {
				int count = 0;
				if (from > 1) {
					rs.absolute(from);
				}
				do {
					paginaDeResultados.add(loadNext(rs));
					++count;
				} while (count < pageSize && rs.next());
			}

			int totalResults = SQLUtils.getTotalRows(rs);

			results.setPage(paginaDeResultados);
			results.setTotal(totalResults);

			return results; 

		} catch (SQLException e) {
			logger.error("Criteria: {}, Exception message: {} ", criteria.toString(), e.getMessage(), e);
			throw new DataException("Error finding users by criteria: " + criteria.toString(), e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Crear un nuevo usuario.
	 */
	public UserDTO create(Connection c, UserDTO usuario) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "INSERT INTO USER (dni, name, last_name1, last_name2, born_date, phone_number, email, password, locality_id, rol_id, gender_id) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

			ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); 

			int i = 1; 
			ps.setString(i++, usuario.getDni());
			ps.setString(i++, usuario.getName());
			ps.setString(i++, usuario.getLastName1());
			ps.setString(i++, usuario.getLastName2());
			ps.setDate(i++, usuario.getBornDate() != null ? new java.sql.Date(usuario.getBornDate().getTime()) : null);
			ps.setString(i++, usuario.getPhoneNumber());
			ps.setString(i++, usuario.getEmail());
			ps.setString(i++, usuario.getPassword());
			
			// Usamos setObject para soportar nulos sin lanzar NullPointerException
			ps.setObject(i++, usuario.getLocalityId());
			ps.setObject(i++, usuario.getRolId());
			ps.setObject(i++, usuario.getGenderId());

			ps.executeUpdate(); 

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				usuario.setId(rs.getLong(1));
			} 
			return usuario;

		} catch (SQLException e) {
			logger.error("Error creating user: {}, message: {} ", usuario, e.getMessage(), e);
			throw new DataException("Error creating user: " + usuario, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Actualizar un usuario existente.
	 */
	public boolean update(Connection c, UserDTO usuario) throws DataException {

		PreparedStatement ps = null;

		try {
			String sql = "UPDATE USER SET dni = ?, name = ?, last_name1 = ?, last_name2 = ?, "
					+ "born_date = ?, phone_number = ?, email = ?, password = ?, "
					+ "locality_id = ?, rol_id = ? , gender_id = ? "
					+ "WHERE id = ?";

			ps = c.prepareStatement(sql);

			int i = 1;	
			ps.setString(i++, usuario.getDni());
			ps.setString(i++, usuario.getName());
			ps.setString(i++, usuario.getLastName1());
			ps.setString(i++, usuario.getLastName2());
			ps.setDate(i++, usuario.getBornDate() != null ? new java.sql.Date(usuario.getBornDate().getTime()) : null);
			ps.setString(i++, usuario.getPhoneNumber());
			ps.setString(i++, usuario.getEmail());
			ps.setString(i++, usuario.getPassword());
			
			// Usamos setObject para soportar nulos sin lanzar NullPointerException
			ps.setObject(i++, usuario.getLocalityId());
			ps.setObject(i++, usuario.getRolId());	
			ps.setObject(i++, usuario.getGenderId());
			ps.setObject(i++, usuario.getId());

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			logger.error("Error updating user: {}, message: {} ", usuario, e.getMessage(), e);
			throw new DataException("Error updating user: " + usuario, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	/**
	 * Eliminar un usuario por su id.
	 */
	public boolean delete(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;

		try {
			String sql = "DELETE FROM USER WHERE id = ? ";

			ps = c.prepareStatement(sql);

			int i = 1;
			ps.setObject(i++, id);
			int deleted = ps.executeUpdate();
			return deleted > 0;

		} catch (SQLException e) {
			logger.error("Error deleting user by id: {}, message: {} ", id, e.getMessage(), e);
			throw new DataException("Error deleting user by id: " + id, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	/**
	 * Mapea la fila actual del ResultSet a un objeto UserDTO.
	 */
	private UserDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		UserDTO usuario = new UserDTO();

		usuario.setId(rs.getLong(i++));
		usuario.setDni(rs.getString(i++));
		usuario.setName(rs.getString(i++));
		usuario.setLastName1(rs.getString(i++));
		usuario.setLastName2(rs.getString(i++));
		usuario.setBornDate(rs.getDate(i++));
		usuario.setPhoneNumber(rs.getString(i++));
		usuario.setEmail(rs.getString(i++));
		usuario.setPassword(rs.getString(i++));
		
		// Manejo seguro de nulos al leer del ResultSet
		long localityId = rs.getLong(i++);
		usuario.setLocalityId(rs.wasNull() ? null : localityId);
		usuario.setLocalityName(rs.getString(i++));

		long provinceId = rs.getLong(i++);
		usuario.setProvinceId(rs.wasNull() ? null : provinceId);
		usuario.setProvinceName(rs.getString(i++));

		long countryId = rs.getLong(i++);
		usuario.setCountryId(rs.wasNull() ? null : countryId);
		usuario.setCountryName(rs.getString(i++));

		long rolId = rs.getLong(i++);
		usuario.setRolId(rs.wasNull() ? null : rolId);
		usuario.setRolName(rs.getString(i++));

		long genderId = rs.getLong(i++);
		usuario.setGenderId(rs.wasNull() ? null : genderId);
		usuario.setGenderName(rs.getString(i++));

		return usuario;
	}
}