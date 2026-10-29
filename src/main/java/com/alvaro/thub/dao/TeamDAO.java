package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.TeamCriteria;
import com.alvaro.thub.model.TeamDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;
import com.alvaro.thub.utils.SQLUtils;

public class TeamDAO {

	private static Logger logger = LogManager.getLogger(TeamDAO.class.getName());

	public TeamDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT t.id, t.initialism, t.name, "
					+ " s.id as sport_id, s.name as sport_name, "
					+ " l.id as locality_id, l.name as locality_name, "
					+ " p.id as province_id, p.name as province_name, "
					+ " c.id as country_id, c.name as country_name "
					+ " FROM TEAM t "
					+ " INNER JOIN SPORT s ON t.sport_id = s.id "
					+ " INNER JOIN LOCALITY l ON t.locality_id = l.id "
					+ " INNER JOIN PROVINCE p ON l.province_id = p.id "
					+ " INNER JOIN COUNTRY c ON p.country_id = c.id ";

	/**
	 * Encontrar equipo por su id.
	 * @param id
	 * @return teamDTO
	 */
	public TeamDTO findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE t.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			TeamDTO team = null;

			if (rs.next()) {
				team = loadNext(rs);
			}
			return team;

		} catch (SQLException e) {
			logger.error("ID: {}, Exception message: {} ", id, e.getMessage(), e);
			throw new DataException("Error finding team by ID: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	/**
	 * Encontrar equipos por criterios de búsqueda.
	 * @param criteria
	 * @return Lista de equipos que cumplen los criterios
	 */
	public Results<TeamDTO> findByCriteria(Connection c,TeamCriteria criteria, int from, int pageSize) throws DataException {

		if (logger.isInfoEnabled()) {
			logger.info("Criteria: {}", criteria);
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Results<TeamDTO> results = new Results<TeamDTO>();
		
		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " t.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getInitialism(), condiciones, " t.initialism LIKE ? ", parametros, "%" + criteria.getInitialism() + "%");
				SQLUtils.addClause(criteria.getName(), condiciones, " t.name LIKE ? ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getSportId(), condiciones, " t.sport_id = ? ", parametros, criteria.getSportId());
				SQLUtils.addClause(criteria.getLocalityId(), condiciones, " t.locality_id = ? ", parametros, criteria.getLocalityId());
				SQLUtils.addClause(criteria.getProvinceId(), condiciones, " p.id = ? ", parametros, criteria.getProvinceId());
				SQLUtils.addClause(criteria.getCountryId(), condiciones, " c.id = ? ", parametros, criteria.getCountryId());
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
			
			// aqui antes sacabamos todos los resultados, pero ahora hace la lista de resultados de una pagina (la paginacion hace que haya 3 listas por ejemplo)
			List<TeamDTO> paginaDeResultados = new ArrayList<TeamDTO>();

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
			results.setTotal(totalResults);
			
			return results;

		} catch (SQLException e) {
			logger.error("Criteria: {}, Exception message: {} ", criteria.toString(), e.getMessage(), e);
			throw new DataException("Error finding teams by criteria: " + criteria.toString(), e);
		}
		finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Crear un nuevo equipo
	 * @param team
	 * @return
	 */
	public TeamDTO create(Connection c,TeamDTO team) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "INSERT INTO TEAM (initialism, name, sport_id, locality_id) VALUES (?, ?, ?, ?)";

			ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			int i = 1; 
			ps.setString(i++, team.getInitialism());
			ps.setString(i++, team.getName());
			ps.setLong(i++, team.getSportId());
			ps.setLong(i++, team.getLocalityId());
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				team.setId(rs.getLong(1));
			} 
			return team;


		} catch (SQLException e) {
			logger.error("Team: {}, Exception message: {} ", team, e.getMessage(), e);
			throw new DataException("Error creating team: " + team, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Actualizar un equipo existente
	 * @param team
	 * @return 
	 */
	public boolean update(Connection c,TeamDTO team) throws DataException {

		PreparedStatement ps = null;

		try {

			String sql = "UPDATE TEAM SET initialism = ?, name = ?, sport_id = ?, locality_id = ? WHERE id = ?";

			ps = c.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, team.getInitialism());
			ps.setString(i++, team.getName());
			ps.setLong(i++, team.getSportId());
			ps.setLong(i++, team.getLocalityId());
			ps.setLong(i++, team.getId());

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			logger.error("Team: {}, Exception message: {} ", team, e.getMessage(), e);
			throw new DataException("Error updating team: " + team, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	/**
	 * Eliminar un equipo por su id
	 * @param id
	 * @return
	 */
	public boolean delete(Connection c,Long id) throws DataException {

		PreparedStatement ps = null;

		try {

			String sql = "DELETE FROM TEAM WHERE id = ?";

			ps = c.prepareStatement(sql);

			int i = 1;
			ps.setLong(i++, id);
			int deleted = ps.executeUpdate();
			return deleted > 0;

		} catch (SQLException e) {
			logger.error("ID: {}, Exception message: {} ", id, e.getMessage(), e);
			throw new DataException("Error deleting team by ID: " + id, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}



	/**
	 * Encontrar todos los equipos
	 * @return lista de todos los equipos
	 */
	public List<TeamDTO> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" ORDER BY t.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<TeamDTO> resultados = new ArrayList<TeamDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Exception message: {} ", e.getMessage(), e);
			throw new DataException("Error finding all teams", e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	// cargar un equipo desde el ResultSet
	private TeamDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		TeamDTO team = new TeamDTO();

		team.setId(rs.getLong(i++));
		team.setInitialism(rs.getString(i++));
		team.setName(rs.getString(i++));
		team.setSportId(rs.getLong(i++));
		team.setSportName(rs.getString(i++));
		team.setLocalityId(rs.getLong(i++));
		team.setLocalityName(rs.getString(i++));
		team.setProvinceId(rs.getLong(i++));
		team.setProvinceName(rs.getString(i++));
		team.setCountryId(rs.getLong(i++));
		team.setCountryName(rs.getString(i++));

		return team;
	}
}