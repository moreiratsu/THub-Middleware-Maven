package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.model.Country;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;


public class CountryDAO {

	private static final Logger logger = LogManager.getLogger(CountryDAO.class.getName());
	
	public CountryDAO() {
	}

	private static final String BASE_QUERY = 
			" SELECT country.id, country.name "
					+ " FROM COUNTRY country ";

	/**
	 * Encontrar país por su id. 
	 * @param id
	 * @return country
	 */
	public Country findById(Connection c,Long id) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			sqlBuilder.append(" WHERE country.id = ? ");

			ps = c.prepareStatement(sqlBuilder.toString());
			DAOUtils.setParameters(ps, id);				
			rs = ps.executeQuery();

			Country country = null;

			if (rs.next()) {
				country = loadNext(rs);
			}
			return country;

		} catch (SQLException e) {
			logger.error("Error al buscar país por id: {}", id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	
	
	// El método findAll es usdo en entidades de tablas maestras, como Country, para cargar los datos en un combo box. En este caso, se ordena por nombre para que el combo box se muestre ordenado alfabéticamente.
	/**
	 * Encontrar todos los países
	 * @return
	 */
	public List<Country> findAll(Connection c) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {           

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			// Se ordena por nombre para que el resultado se muestre ordenado alfabéticamente.
            // esto se hace en porque no tiene criteria
			sqlBuilder.append(" ORDER BY country.name ");

			ps = c.prepareStatement(sqlBuilder.toString());
			rs = ps.executeQuery();

			List<Country> resultados = new ArrayList<Country>();

			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error al buscar todos los países: {}", e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}

	/**
     * Buscar paises por nombre (búsqueda parcial)
     * @param name
     * @return
     */
    public List<Country> findByName(Connection c,String name) throws DataException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {           

            StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
            sqlBuilder.append(" WHERE UPPER(country.name) LIKE UPPER(?) ");      
            sqlBuilder.append(" ORDER BY country.name ");

            ps = c.prepareStatement(sqlBuilder.toString());
            DAOUtils.setParameters(ps, "%" + name + "%");              
            rs = ps.executeQuery();

            List<Country> resultados = new ArrayList<Country>();

            while (rs.next()) {
                resultados.add(loadNext(rs));
            }
            return resultados;

        } catch (SQLException e) {
            logger.error("Error al buscar países por nombre: {}", name, e);
			throw new DataException(e);
        } finally {
            JDBCUtils.close(rs, ps);
        }
    }
	// cargar un país desde el ResultSet
	private Country loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Country country = new Country();

		country.setId(rs.getLong(i++));
		country.setName(rs.getString(i++));

		return country;
	}
}
