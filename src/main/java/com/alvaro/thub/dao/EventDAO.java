package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.EventCriteria;
import com.alvaro.thub.model.Event;
import com.alvaro.thub.model.EventDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;
import com.alvaro.thub.utils.SQLUtils;


public class EventDAO {	

	private static Logger logger = LogManager.getLogger(EventDAO.class.getName());

	public EventDAO() {		
	}

	private static final String BASE_QUERY = 			
			" SELECT event.id, event.name, event.start_date, event.end_date,"
					+ " event.format_id, format.name as format_name,"
					+ " event.creator_user_id,"
					+ " u.name as creator_name, u.last_name1 as creator_last_name,"
					+ " u.email as creator_email, u.phone_number as creator_phone,"
					+ " event.status_id, status.name as status_name,"
					+ " event.country_id, country.name as country_name,"
					+ " event.province_id, province.name as province_name,"
					+ " event.locality_id, locality.name as locality_name"
					+ " FROM EVENT event"
					+ " INNER JOIN FORMAT format ON event.format_id = format.id"
					+ " INNER JOIN STATUS status ON event.status_id = status.id"
					+ " INNER JOIN `USER` u ON event.creator_user_id = u.id"  
					+ " LEFT JOIN COUNTRY country ON event.country_id = country.id" // ← LEFT por si es null
					+ " LEFT JOIN PROVINCE province ON event.province_id = province.id"
					+ " LEFT JOIN LOCALITY locality ON event.locality_id = locality.id";


	/**
	 * Encontrar evento por su id. 
	 * @param id
	 * @return
	 */
	public EventDTO findById(Connection c,Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {			
			// obtener la conexion

			// agregar la consulta SQL
			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE event.id = ? ");


			// crear el PreparedStatement y ejecutar la consulta
			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);				
			rs = ps.executeQuery();

			EventDTO evento = null; 	

			if (rs.next()) { 
				evento = loadNext(rs);
			}

			return evento; 	

		} catch (SQLException e) {
			logger.error("Error finding event by id: {}, message: {} ", id, e.getMessage(), e);
			throw new DataException("Error finding event by id: " + id, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}




	/**
	 * Encontrar evento por criterios de búsqueda	
	 * @param criteria
	 * @return
	 */

	public Results<EventDTO> findBy(Connection c, EventCriteria criteria, int from, int pageSize) throws DataException {		

		if (logger.isInfoEnabled()) {
			logger.info("Criteria: {}", criteria);
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		Results<EventDTO> results = new Results<EventDTO>();

		try {		

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);

			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			// Obtener los valores de los criterios
			SQLUtils.addClause(criteria.getId(),condiciones," event.id = ? ",parametros,criteria.getId());

			if(criteria.getId()==null) {
				SQLUtils.addClause(criteria.getName(),condiciones," UPPER(event.name) LIKE UPPER(?) ",parametros,"%"+criteria.getName()+"%");
				SQLUtils.addClause(criteria.getStartDate(),condiciones," event.start_date >= ? ",parametros,criteria.getStartDate()==null?null:(new java.sql.Date(criteria.getStartDate().getTime())));
				SQLUtils.addClause(criteria.getEndDate(),condiciones," event.end_date <= ? ",parametros,criteria.getEndDate()==null?null:(new java.sql.Date(criteria.getEndDate().getTime())));
				SQLUtils.addClause(criteria.getFormatId(), condiciones, " event.format_id = ? ", parametros, criteria.getFormatId());
				SQLUtils.addClause(criteria.getCreatorUserId(), condiciones, " event.creator_user_id = ? ", parametros, criteria.getCreatorUserId());
				SQLUtils.addClause(criteria.getStatusId(), condiciones, " event.status_id = ? ", parametros, criteria.getStatusId());
				SQLUtils.addClause(criteria.getCountryId(),condiciones," event.country_id = ? ",parametros,criteria.getCountryId());
				SQLUtils.addClause(criteria.getProvinceId(),condiciones," event.province_id = ? ",parametros,criteria.getProvinceId());
				SQLUtils.addClause(criteria.getLocalityId(),condiciones," event.locality_id = ? ",parametros,criteria.getLocalityId());
			}

			if (condiciones.size() > 0) {
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

			List<EventDTO> paginaDeResultados = new ArrayList<EventDTO>(); // lista para almacenar los eventos encontrados

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
			throw new DataException("Error finding events by criteria: " + criteria.toString(), e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}




	/**
	 * Crear un nuevo evento.
	 * @param event
	 * @return
	 */
	public Event create(Connection c, Event event) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			// preparar la consulta			
			String sql = "INSERT INTO EVENT (name, start_date, end_date, format_id, status_id, creator_user_id, country_id, province_id, locality_id) "
					+ "\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// crear el PreparedStatement
			ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); 

			// asignar los valores a los parametros
			int i = 1; // contador de parametros
			ps.setString(i++, event.getName());
			ps.setDate(i++, new java.sql.Date(event.getStartDate().getTime()));
			ps.setDate(i++, new java.sql.Date(event.getEndDate().getTime()));
			ps.setLong(i++, event.getFormatId());
			ps.setLong(i++, event.getStatusId());
			ps.setLong(i++, event.getCreatorUserId());
			// Estos campos pueden ser null, usar setObject en vez de setLong para permitir asignar null
			ps.setObject(i++, event.getCountryId());     // Puede ser null
			ps.setObject(i++, event.getProvinceId());    // Puede ser null
			ps.setObject(i++, event.getLocalityId());    // Puede ser null

			// ejecutar la consulta
			ps.executeUpdate(); // para operaciones de insercion, actualizacion y borrado se pone update porque lo que se hace es MODIFICAR la BD

			// obtener la clave generada
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				event.setId(rs.getLong(1));
			} 
			return event;

		} catch (SQLException e) {
			logger.error("Error creating event: {}, message: {} ", event, e.getMessage(), e);
			throw new DataException("Error creating event: " + event, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}







	/**
	 * Actualizar un evento existente.
	 * @param event
	 * @return
	 */
	public boolean update(Connection c, Event event) throws DataException {
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE EVENT SET name = ?, start_date = ?, end_date = ?, "
					+ " format_id = ?, status_id = ?, creator_user_id = ?, country_id = ?, province_id = ?, locality_id = ? "
					+ " WHERE id = ?";
			ps = c.prepareStatement(sql);
			int i = 1;

			ps.setString(i++, event.getName());
			ps.setDate(i++, new java.sql.Date(event.getStartDate().getTime()));
			ps.setDate(i++, new java.sql.Date(event.getEndDate().getTime()));
			ps.setLong(i++, event.getFormatId());
			ps.setLong(i++, event.getStatusId());
			ps.setLong(i++, event.getCreatorUserId());
			ps.setObject(i++, event.getCountryId());     // Puede ser null
			ps.setObject(i++, event.getProvinceId());    // Puede ser null
			ps.setObject(i++, event.getLocalityId());	// Puede ser null
			ps.setLong(i++, event.getId());

			return ps.executeUpdate()==1;			


		} catch (SQLException e) {
			logger.error("Error updating event: {}, message: {} ", event, e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}

	
	
	public boolean updateStatus(Connection c, Long eventId, Long statusId) throws DataException {
		PreparedStatement ps = null;
		try {
			String sql =  " UPDATE EVENT SET status_id = ? "
					+ " WHERE id = ?";
			ps = c.prepareStatement(sql);

			int i = 1;
			ps.setLong(i++, eventId);
			ps.setLong(i++, statusId);

			return ps.executeUpdate()>0;			

		} catch (SQLException e) {
			logger.error("Error updating event status: eventId={}, statusId={}, message: {} ", eventId, statusId, e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}




	/**
	 * Eliminar un evento por su id.
	 * @param id
	 * @return
	 */

	public boolean delete(Connection c,Long id) throws DataException {
		PreparedStatement ps = null;

		try {
			String sql = "DELETE FROM EVENT WHERE id = ?";
			ps = c.prepareStatement(sql);
			int i = 1;
			ps.setLong(i++, id);
			int deleted = ps.executeUpdate();
			return deleted > 0;

		} catch (SQLException e) {
			logger.error("Error deleting event by id: {}, message: {} ", id, e.getMessage(), e);
			throw new DataException("Error deleting event by id: " + id, e);
		} finally {
			JDBCUtils.close(null, ps);
		}
	}



	// cargar un evento desde el ResultSet
	private EventDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		EventDTO evento = new EventDTO();

		evento.setId(rs.getLong(i++));
		evento.setName(rs.getString(i++));
		evento.setStartDate(rs.getDate(i++));
		evento.setEndDate(rs.getDate(i++));
		evento.setFormatId(rs.getLong(i++));
		evento.setFormatName(rs.getString(i++));
		evento.setCreatorUserId(rs.getLong(i++));
		evento.setCreatorUserName(rs.getString(i++));
		evento.setCreatorUserLastName(rs.getString(i++));
		evento.setCreatorUserEmail(rs.getString(i++));
		evento.setCreatorUserPhoneNumber(rs.getString(i++));
		evento.setStatusId(rs.getLong(i++));
		evento.setStatusName(rs.getString(i++));
		// Usar getObject para campos que pueden ser null
		Object countryIdObj = rs.getObject(i++);
		evento.setCountryId(countryIdObj != null ? ((Number) countryIdObj).longValue() : null);
		evento.setCountryName(rs.getString(i++));
		Object provinceIdObj = rs.getObject(i++);
		evento.setProvinceId(provinceIdObj != null ? ((Number) provinceIdObj).longValue() : null);
		evento.setProvinceName(rs.getString(i++));
		Object localityIdObj = rs.getObject(i++);
		evento.setLocalityId(localityIdObj != null ? ((Number) localityIdObj).longValue() : null);	  
		evento.setLocalityName(rs.getString(i++));

		return evento;
	}
}