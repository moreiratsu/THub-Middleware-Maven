package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.VenueCriteria;
import com.alvaro.thub.model.VenueDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.SQLUtils;

public class VenueDAO {

	private static Logger logger = LogManager.getLogger(VenueDAO.class.getName());

    public VenueDAO() {
    }

    private static final String BASE_QUERY = 
    		" SELECT venue.id, venue.name, venue.address, "
    				+ " venue.locality_id, locality.name as locality_name, "
    				+ " province.id as province_id, province.name as province_name, "
    				+ " country.id as country_id, country.name as country_name "
    				+ " FROM VENUE venue "
    				+ " INNER JOIN LOCALITY locality ON venue.locality_id = locality.id "
    				+ " INNER JOIN PROVINCE province ON locality.province_id = province.id "
    				+ " INNER JOIN COUNTRY country ON province.country_id = country.id ";

    /**
     * Encontrar recinto por su id.
     * @param id
     * @return venueDTO
     */
    public VenueDTO findById(Connection c, Long id) throws DataException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
            sqlBuilder.append(" WHERE v.id = ? ");

            ps = c.prepareStatement(sqlBuilder.toString());
            DAOUtils.setParameters(ps, id);              
            rs = ps.executeQuery();

            VenueDTO venue = null;

            if (rs.next()) {
                venue = loadNext(rs);
            }
            return venue;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, ps);
        }
        return null;
    }

    
    /**
	 * Encontrar recintos por criterios de búsqueda.
	 * @param criteria
	 * @return Lista de recintos que cumplen los criterios
	 */
    public List<VenueDTO> findByCriteria(Connection c,VenueCriteria criteria) throws DataException {
    		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			
			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(), condiciones, " v.id = ? ", parametros, criteria.getId());
            
			if (criteria.getId() == null) {
				SQLUtils.addClause(criteria.getName(), condiciones, " v.name LIKE ? ", parametros, "%" + criteria.getName() + "%");
				SQLUtils.addClause(criteria.getAddress(), condiciones, " v.address LIKE ? ", parametros, "%" + criteria.getAddress() + "%");
				SQLUtils.addClause(criteria.getLocalityId(), condiciones, " v.locality_id = ? ", parametros, criteria.getLocalityId());
				SQLUtils.addClause(criteria.getProvinceId(), condiciones, " province.id = ? ", parametros, criteria.getProvinceId());
				SQLUtils.addClause(criteria.getCountryId(), condiciones, " country.id = ? ", parametros, criteria.getCountryId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append( String.join(" AND ", condiciones) );
			}
			
            sqlBuilder.append(" ORDER BY v.name ");
            
			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);

			int i = 1;
			for (Object parameterValue: parametros) {
				ps.setObject(i++, parameterValue);
			}
			
			rs = ps.executeQuery();
			
			List<VenueDTO> resultados = new ArrayList<VenueDTO>();
			
			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta: " + e.getMessage(), e);
			throw new DataException("Error al ejecutar la consulta", e);
    	}
		finally {
			JDBCUtils.close(rs, ps);
		}
    }

    /**
     * Crear un nuevo recinto
     * @param venue
     * @return
     */
    public VenueDTO create(Connection c,VenueDTO venue) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

        try {

            String sql = "INSERT INTO VENUE (name, address, locality_id) VALUES (?, ?, ?)";
            
            ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            int i = 1; 
            ps.setString(i++, venue.getName());
            ps.setString(i++, venue.getAddress());
            ps.setLong(i++, venue.getLocalityId());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
				venue.setId(rs.getLong(1));
			} 
			return venue;
                

        } catch (SQLException e) {
            logger.error("Error al crear el recinto: " + e.getMessage(), e);
            throw new DataException("Error al crear el recinto", e);
        } finally {
            JDBCUtils.close(rs, ps);
        }
    }

    /**
     * Actualizar un recinto existente
     * @param venue
     * @return 
     */
    public boolean update(Connection c,VenueDTO venue) throws DataException {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE VENUE SET name = ?, address = ?, locality_id = ? WHERE id = ?";
            
            ps = c.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, venue.getName());
            ps.setString(i++, venue.getAddress());
            ps.setLong(i++, venue.getLocalityId());
            ps.setLong(i++, venue.getId());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logger.error("Error al actualizar el recinto: " + e.getMessage(), e);
			throw new DataException("Error al actualizar el recinto", e);
        } finally {
            JDBCUtils.close(null, ps);
        }
    }

    /**
     * Eliminar un recinto por su id
     * @param id
     * @return
     */
    public boolean delete(Connection c,Long id) throws DataException {

        PreparedStatement ps = null;

        try {

            String sql = "DELETE FROM VENUE WHERE id = ?";
            
            ps = c.prepareStatement(sql);
            
            int i = 1;
            ps.setLong(i++, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
           logger.error("Error al eliminar el recinto: " + e.getMessage(), e);
           throw new DataException("Error al eliminar el recinto", e);
        } finally {
            JDBCUtils.close(null, ps);
        }
    }

    // cargar un recinto desde el ResultSet
    private VenueDTO loadNext(ResultSet rs) throws SQLException {

        int i = 1;

        VenueDTO venue = new VenueDTO();

        venue.setId(rs.getLong(i++));
        venue.setName(rs.getString(i++));
        venue.setAddress(rs.getString(i++));
        venue.setLocalityId(rs.getLong(i++));
        venue.setLocalityName(rs.getString(i++));
        venue.setProvinceId(rs.getLong("province_id"));
		venue.setProvinceName(rs.getString("province_name"));
		venue.setCountryId(rs.getLong("country_id"));
		venue.setCountryName(rs.getString("country_name"));

        return venue;
    }
}