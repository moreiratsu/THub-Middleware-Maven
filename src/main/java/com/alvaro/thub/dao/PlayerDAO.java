package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.PlayerCriteria;
import com.alvaro.thub.model.PlayerDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;
import com.alvaro.thub.utils.SQLUtils;

public class PlayerDAO {

	private static Logger logger = LogManager.getLogger(PlayerDAO.class.getName());

	public PlayerDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT p.id, p.dni, p.name, p.last_name_1, p.last_name_2, "
					+ " p.born_date, "
					+ " p.locality_id, locality.name as locality_name, "
					+ " province.id as province_id, province.name as province_name, "
					+ " country.id as country_id, country.name as country_name, "
					+ " p.team_id, team.name as team_name, "  
					+ " p.gender_id, gender.name as gender_name "
					+ " FROM PLAYER p "  
					+ " INNER JOIN LOCALITY locality ON p.locality_id = locality.id "
					+ " INNER JOIN PROVINCE province ON locality.province_id = province.id "
					+ " INNER JOIN COUNTRY country ON province.country_id = country.id "
					+ " INNER JOIN TEAM team ON p.team_id = team.id "
					+ " INNER JOIN GENDER gender ON p.gender_id = gender.id ";

	/**
	 * Encontrar jugador por su id.
	 * @param id
	 * @return playerDTO
	 */
	public PlayerDTO findById(Connection c,Long id) throws DataException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE p.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			PlayerDTO player = null;

			if (rs.next()) {
				player = loadNext(rs);
			}
			return player;

		} catch (SQLException e) {
			logger.error("Error al buscar jugador por id: {}", id, e.getMessage());
			throw new DataException(e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	/**
	 * Encontrar jugadores por criterios de búsqueda.
	 * @param criteria
	 * @return Lista de jugadores que cumplen los criterios
	 */
	public Results<PlayerDTO> findByCriteria(Connection c,PlayerCriteria criteria, int from, int pageSize) throws DataException{

		if (logger.isInfoEnabled()) {
			logger.info("Criteria: {}", criteria);
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		Results<PlayerDTO> results = new Results<PlayerDTO>();

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " p.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getDni(), condiciones, " p.dni LIKE ? ", parametros, "%" + criteria.getDni() + "%");
				SQLUtils.addClause(criteria.getName(), condiciones, " p.name LIKE ? ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getLastName1(), condiciones, " p.last_Name_1 LIKE ? ", parametros, "%" + criteria.getLastName1() + "%");
				SQLUtils.addClause(criteria.getLastName2(), condiciones, " p.last_Name_2 LIKE ? ", parametros, "%" + criteria.getLastName2() + "%");


				if (criteria.getBornDateFrom() != null) {
					condiciones.add(" p.born_Date >= ? ");
					parametros.add(new java.sql.Date(criteria.getBornDateFrom().getTime()));
				}
				if (criteria.getBornDateTo() != null) {
					condiciones.add(" p.born_Date <= ? ");
					parametros.add(new java.sql.Date(criteria.getBornDateTo().getTime()));
				}

				SQLUtils.addClause(criteria.getLocalityId(), condiciones, " p.locality_id = ? ", parametros, criteria.getLocalityId());
				SQLUtils.addClause(criteria.getProvinceId(), condiciones, " province.id = ? ", parametros, criteria.getProvinceId());
				SQLUtils.addClause(criteria.getCountryId(), condiciones, " country.id = ? ", parametros, criteria.getCountryId());
				SQLUtils.addClause(criteria.getTeamId(), condiciones, " p.team_id = ? ", parametros, criteria.getTeamId());
				SQLUtils.addClause(criteria.getGenderId(), condiciones, " p.gender_id = ? ", parametros, criteria.getGenderId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append( String.join(" AND ", condiciones) );
			}

			sqlBuilder.append(" ORDER BY ").append(criteria.getOrderBy()).append(criteria.isAscDesc()?" ASC ":" DESC ");

			String sql = sqlBuilder.toString();

			if(logger.isInfoEnabled()) {
				logger.debug("Criteria SQL: {}: {}: ", criteria, sql);
			}

			ps = c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;
			for (Object parameterValue: parametros) {
				ps.setObject(i++, parameterValue);
			}

			rs = ps.executeQuery();

			List<PlayerDTO> paginaDeResultados = new ArrayList<PlayerDTO>();

			if(from >= 1) {
				int count = 0;
				rs.absolute(from); 
				do {
					paginaDeResultados.add(loadNext(rs));
					++count;
				}while (count<pageSize && rs.next());
			}

			int totalResults = SQLUtils.getTotalRows(rs);

			results.setPage(paginaDeResultados);
			results.setTotal(totalResults);;

			return results;

		} catch (SQLException e) {
			logger.error("Error SQL al buscar por criterios: {}", criteria, e);
			throw new DataException(e);
		}	finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Crear un nuevo jugador
	 * @param player
	 * @return
	 */
	public PlayerDTO create(Connection c,PlayerDTO player) throws DataException{

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "INSERT INTO PLAYER (dni, name, last_Name_1, last_Name_2, born_Date, locality_id, team_id, gender_id) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			int i = 1; 
			ps.setString(i++, player.getDni());
			ps.setString(i++, player.getName());
			ps.setString(i++, player.getLastName1());
			ps.setString(i++, player.getLastName2());
			ps.setDate(i++, new java.sql.Date(player.getBornDate().getTime()));
			ps.setLong(i++, player.getLocalityId());
			ps.setLong(i++, player.getTeamId());
			ps.setLong(i++, player.getGenderId());
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				player.setId(rs.getLong(1));
			} 
			return player;


		} catch (SQLException e) {
			logger.error("Error SQL al crear jugador con DNI: {}", player.getDni(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Actualizar un jugador existente
	 * @param player
	 * @return 
	 */
	public boolean update(Connection c,PlayerDTO player) throws DataException{

		PreparedStatement ps = null;

		try {

			String sql = "UPDATE PLAYER SET dni = ?, name = ?, last_Name_1 = ?, last_Name_2 = ?, born_Date = ?, "
					+ "locality_id = ?, team_id = ?, gender_id = ? WHERE id = ?";

			ps = c.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, player.getDni());
			ps.setString(i++, player.getName());
			ps.setString(i++, player.getLastName1());
			ps.setString(i++, player.getLastName2());
			ps.setDate(i++, new java.sql.Date(player.getBornDate().getTime()));
			ps.setLong(i++, player.getLocalityId());
			ps.setLong(i++, player.getTeamId());
			ps.setLong(i++, player.getGenderId());
			ps.setLong(i++, player.getId());

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			logger.error("Error SQL al actualizar jugador con ID: {}", player.getId(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	/**
	 * Eliminar un jugador por su id
	 * @param id
	 * @return
	 */
	public boolean delete(Connection c,Long id) throws DataException{

		PreparedStatement ps = null;

		try {

			String sql = "DELETE FROM PLAYER WHERE id = ?";

			ps = c.prepareStatement(sql);

			int i = 1;
			ps.setLong(i++, id);
			int deleted = ps.executeUpdate();
			return deleted > 0;

		} catch (Exception e) {
			logger.error("Error SQL al eliminar jugador con ID: {}", id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	// cargar un jugador desde el ResultSet
	private PlayerDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		PlayerDTO player = new PlayerDTO();

		player.setId(rs.getLong(i++));
		player.setDni(rs.getString(i++));
		player.setName(rs.getString(i++));
		player.setLastName1(rs.getString(i++));
		player.setLastName2(rs.getString(i++));
		player.setBornDate(rs.getDate(i++));
		player.setLocalityId(rs.getLong(i++));
		player.setLocalityName(rs.getString(i++));
		player.setProvinceId(rs.getLong(i++));
		player.setProvinceName(rs.getString(i++));
		player.setCountryId(rs.getLong(i++));
		player.setCountryName(rs.getString(i++));
		player.setTeamId(rs.getLong(i++));
		player.setTeamName(rs.getString(i++));
		player.setGenderId(rs.getLong(i++));
		player.setGenderName(rs.getString(i++));

		return player;
	}
}