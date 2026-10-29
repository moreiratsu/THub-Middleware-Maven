package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.MatchCriteria;
import com.alvaro.thub.model.MatchDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.SQLUtils;

public class MatchDAO {

	private static Logger logger = LogManager.getLogger(MatchDAO.class.getName());

	public MatchDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT m.id, m.date, m.round, "
					+ " v.id as venue_id, v.name as venue_name, v.address as venue_address, "
					+ " e.id as event_id, e.name as event_name, "
					+ " s.id as status_id, s.name as status_name "
					+ " FROM `MATCH` m "
					+ " INNER JOIN VENUE v ON m.venue_id = v.id "
					+ " INNER JOIN EVENT e ON m.event_id = e.id "
					+ " INNER JOIN STATUS s ON m.status_id = s.id ";

	/**
	 * Encontrar partido por su id.
	 * @param id
	 * @return matchDTO
	 */
	public MatchDTO findById(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE m.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);              
			rs = ps.executeQuery();

			MatchDTO match = null;

			if (rs.next()) {
				match = loadNext(rs);
			}
			return match;

		} catch (SQLException e) {
			logger.error("Error finding match by id: " + id, e);
			throw new DataException("Error finding match by id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	/**
	 * Encontrar partidos por criterios de búsqueda.
	 * @param criteria
	 * @return Lista de partidos que cumplen los criterios
	 */
	public List<MatchDTO> findByCriteria(Connection c, MatchCriteria criteria) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " m.id = ? ", parametros, criteria.getId());

			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getDate(), condiciones, " m.date = ? ", parametros, criteria.getDate());
				SQLUtils.addClause(criteria.getRound(), condiciones, " m.round LIKE ? ", parametros, "%" + criteria.getRound() + "%");
				SQLUtils.addClause(criteria.getVenueId(), condiciones, " m.venue_id = ? ", parametros, criteria.getVenueId());
				SQLUtils.addClause(criteria.getEventId(), condiciones, " m.event_id = ? ", parametros, criteria.getEventId());
				SQLUtils.addClause(criteria.getStatusId(), condiciones, " m.status_id = ? ", parametros, criteria.getStatusId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append( String.join(" AND ", condiciones) );
			}

			sqlBuilder.append(" ORDER BY m.date ");

			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);

			int i = 1;
			for (Object parameterValue: parametros) {
				ps.setObject(i++, parameterValue);
			}

			rs = ps.executeQuery();

			List<MatchDTO> resultados = new ArrayList<MatchDTO>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error finding matches by criteria: " + criteria, e);
			throw new DataException("Error finding matches by criteria: " + criteria, e);
		}
		finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Crear un nuevo partido
	 * @param match
	 * @return
	 */
	public MatchDTO create(Connection c, MatchDTO match) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "INSERT INTO `MATCH` (date, round, venue_id, event_id, status_id) VALUES (?, ?, ?, ?, ?)";

			ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			int i = 1; 
			ps.setDate(i++, new java.sql.Date(match.getDate().getTime()));
			ps.setString(i++, match.getRound());
			ps.setLong(i++, match.getVenueId());
			ps.setLong(i++, match.getEventId());
			ps.setLong(i++, match.getStatusId());
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				match.setId(rs.getLong(1));
			} 
			return match;


		} catch (SQLException e) {
			logger.error("Error creating match: " + match, e);
			throw new DataException("Error creating match: " + match, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
	 * Actualizar un partido existente
	 * @param match
	 * @return 
	 */
	public boolean update(Connection c, MatchDTO match) throws DataException {

		PreparedStatement ps = null;

		try {

			String sql = "UPDATE `MATCH` SET date = ?, round = ?, venue_id = ?, event_id = ?, status_id = ? WHERE id = ?";

			ps = c.prepareStatement(sql);
			int i = 1;
			ps.setDate(i++, new java.sql.Date(match.getDate().getTime()));
			ps.setString(i++, match.getRound());
			ps.setLong(i++, match.getVenueId());
			ps.setLong(i++, match.getEventId());
			ps.setLong(i++, match.getStatusId());
			ps.setLong(i++, match.getId());

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			logger.error("Error updating match: " + match, e);
			throw new DataException("Error updating match: " + match, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	/**
	 * Eliminar un partido por su id
	 * @param id
	 * @return
	 */
	public boolean delete(Connection c, Long id) throws DataException {

		PreparedStatement ps = null;

		try {

			String sql = "DELETE FROM `MATCH` WHERE id = ?";

			ps = c.prepareStatement(sql);

			int i = 1;
			ps.setLong(i++, id);
			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			logger.error("Error deleting match with id: " + id, e);
			throw new DataException("Error deleting match with id: " + id, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	// cargar un partido desde el ResultSet
	private MatchDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		MatchDTO match = new MatchDTO();

		match.setId(rs.getLong(i++));
		match.setDate(rs.getDate(i++));
		match.setRound(rs.getString(i++));
		match.setVenueId(rs.getLong(i++));
		match.setVenueName(rs.getString(i++));
		match.setVenueAddres(rs.getString(i++));
		match.setEventId(rs.getLong(i++));
		match.setEventName(rs.getString(i++));
		match.setStatusId(rs.getLong(i++));
		match.setStatusName(rs.getString(i++));

		return match;
	}
}