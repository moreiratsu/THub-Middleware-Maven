package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.criteria.CategoryCriteria;
import com.alvaro.thub.model.CategoryDTO;
import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.SQLUtils;

public class CategoryDAO {
	
	private static Logger logger = LogManager.getLogger(CategoryDAO.class.getName());

    public CategoryDAO() {
    }

    private static final String BASE_QUERY = 
            " SELECT cat.id, cat.name, cat.description, cat.gender_id, gen.name as gender_name "
                    + " FROM CATEGORY cat "
                    + " INNER JOIN GENDER gen ON cat.gender_id = gen.id ";

    /**
     * Encontrar categoría por su id.
     * @param id
     * @return categoryDTO
     */
    // aqui evidentemente no hay order by porque solo devuelve un resultado por id
    public CategoryDTO findById(Connection c,Long id) throws DataException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
            sqlBuilder.append(" WHERE cat.id = ? ");

            ps = c.prepareStatement(sqlBuilder.toString());
            DAOUtils.setParameters(ps, id);              
            rs = ps.executeQuery();

            CategoryDTO category = null;

            if (rs.next()) {
                category = loadNext(rs);
            }
            return category;

        } catch (SQLException e) {
        	logger.error("Error al buscar categoría por id: {}", id, e.getMessage());
			throw new DataException(e);
        } finally {
            JDBCUtils.close(rs, ps);
        }
    }

    
    /**
	 * Encontrar categoría por criterios de búsqueda.
	 * @param criteria
	 * @return Lista de categorías que cumplen los criterios
	 */
    	public List<CategoryDTO> findByCriteria(Connection c,CategoryCriteria criteria) throws DataException {
    		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
			
			List<String> condiciones = new ArrayList<>();
			List<Object> parametros = new ArrayList<>();

			SQLUtils.addClause(criteria.getId(),condiciones," cat.id = ? ",parametros,criteria.getId());
            
			if (criteria.getId()==null) {
				SQLUtils.addClause(criteria.getName(),condiciones," cat.name LIKE ? ",parametros,"%"+criteria.getName()+"%");
				SQLUtils.addClause(criteria.getGenderId(),condiciones," cat.gender_id = ? ",parametros,criteria.getGenderId());
			}

			if (!condiciones.isEmpty()) {
				sqlBuilder.append(" WHERE ");
				sqlBuilder.append( String.join(" AND ", condiciones) );
			}
			
			//aqui añadimos la parte de ordenación, si se ha indicado en los criterios de la query
			if (criteria.getOrderBy() != null) {
				sqlBuilder.append(" ORDER BY ").append(criteria.getOrderBy());
				sqlBuilder.append(criteria.isAscDesc() ? " ASC " : " DESC ");
			}
			
			String sql = sqlBuilder.toString();
			System.out.println("SQL: " + sql);
			ps = c.prepareStatement(sql);

			int i = 1;
			for (Object parameterValue: parametros) {
				ps.setObject(i++, parameterValue);
			}
			
			rs = ps.executeQuery();
			
			List<CategoryDTO> resultados = new ArrayList<CategoryDTO>();
			
			while (rs.next()) {
				resultados.add(loadNext(rs));
			}
			return resultados;

		} catch (SQLException e) {
			logger.error("Error SQL al buscar por criterios: {}", criteria, e);
			throw new DataException(e);
    	}
		finally {
			JDBCUtils.close(rs, ps);
		}
		// return new ArrayList<>();
    	}

    /**
     * Crear una nueva categoría
     * @param category
     * @return
     */
    public CategoryDTO create(Connection c,CategoryDTO category) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;

        try {

            String sql = "INSERT INTO CATEGORY (name, description, gender_id) VALUES (?, ?, ?)";
            
            ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            int i = 1; 
            ps.setString(i++, category.getName());
            ps.setString(i++, category.getDescription());
            ps.setLong(i++, category.getGenderId());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
				category.setId(rs.getLong(1));
			} 
			return category;
                

        } catch (SQLException e) {
        	logger.error("Error al crear categoría: {}", category, e);
            throw new DataException(e);
        } finally {
            JDBCUtils.close(null, ps);
        }
    }

    /**
     * Actualizar una categoría existente
     * @param category
     * @return 
     */
    public boolean update(Connection c,CategoryDTO category) throws DataException {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE CATEGORY SET name = ?, description = ?, gender_id = ? WHERE id = ?";
            
            ps = c.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, category.getName());
            ps.setString(i++, category.getDescription());
            ps.setLong(i++, category.getGenderId());
            ps.setLong(i++, category.getId());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	logger.error("Error SQL al actualizar categoría con ID: {}", category.getId(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.close(null, ps);
        }
    }

    /**
     * Eliminar una categoría por su id
     * @param id
     * @return
     */
    public boolean delete(Connection c, Long id) throws DataException {

        PreparedStatement ps = null;

        try {

            String sql = "DELETE FROM CATEGORY WHERE id = ?";
            
            ps = c.prepareStatement(sql);
            
            int i = 1;
            ps.setLong(i++, id);
            ps.executeUpdate();
            return true;
            

        } catch (SQLException e) {
        	logger.error("Error SQL al eliminar categoría con ID: {}", id, e);
            throw new DataException(e);
        } finally {
            JDBCUtils.close(null, ps);
        }
    }

    // cargar una categoría desde el ResultSet
    private CategoryDTO loadNext(ResultSet rs) throws SQLException {

        int i = 1;

        CategoryDTO category = new CategoryDTO();

        category.setId(rs.getLong(i++));
        category.setName(rs.getString(i++));
        category.setDescription(rs.getString(i++));
        category.setGenderId(rs.getLong(i++));
        category.setGenderName(rs.getString(i++));

        return category;
    }
}