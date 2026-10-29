package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.CategoryDAO;
import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.criteria.CategoryCriteria;
import com.alvaro.thub.model.CategoryDTO;
import com.alvaro.thub.service.CategoryService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class.getName());

	private CategoryDAO categoryDAO = null;

	public CategoryServiceImpl() {
		categoryDAO = new CategoryDAO();
	}
	
	@Override
	public CategoryDTO create(CategoryDTO category) throws ServiceException {
		Connection c = null;
		boolean commit = false;
		
		try {
			// Validaciones
			if (category == null) {
				throw new IllegalArgumentException("La categoría no puede ser null");
			}
			if (category.getName() == null || category.getName().trim().isEmpty()) {
				throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
			}
			if (category.getGenderId() == null) {
				throw new IllegalArgumentException("El género de la categoría es obligatorio");
			}
			
			c = JDBCUtils.getConnection();
			c.setAutoCommit(false);
			
			CategoryDTO created = categoryDAO.create(c, category);
			
			commit = true;
			logger.info("Categoría creada exitosamente con ID: {}", created != null ? created.getId() : null);
			return created;
			
		} catch (DataException e) {
			logger.error("Error de persistencia al crear categoría {}: {}", category, e.getMessage());
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			logger.error("Error de validación al crear categoría: {}", e.getMessage());
			throw e; // Re-lanzar excepciones de validación
		} catch (Exception e) {
			logger.error("Creando categoría {}: {}", category, e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			JDBCUtils.close(c, commit);
		}
	}

	@Override
	public CategoryDTO findById(Long id) throws ServiceException {
		Connection c = null;
		boolean commit = false;
		
		try {
			if (id == null) {
				throw new IllegalArgumentException("El id no puede ser null");
			}
			
			c = JDBCUtils.getConnection();
			c.setAutoCommit(false);
			
			CategoryDTO category = categoryDAO.findById(c, id);
			
			commit = true;
			
			if (logger.isDebugEnabled()) {
				logger.debug("Categoría encontrada con id {}: {}", id, category != null ? category.getName() : "no existe");
			}
			return category;
			
		} catch (DataException e) {
			logger.error("Error de persistencia al buscar categoría con id {}: {}", id, e.getMessage());
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			logger.error("Error de validación al buscar categoría: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Buscando categoría por id {}: {}", id, e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			JDBCUtils.close(c, commit);
		}
	}
	
	@Override
	public List<CategoryDTO> findBy(CategoryCriteria criteria) throws ServiceException {
		Connection c = null;
		boolean commit = false;
		
		try {
			if (criteria == null) {
				throw new IllegalArgumentException("El criterio de búsqueda no puede ser null");
			}
			
			c = JDBCUtils.getConnection();
			c.setAutoCommit(false);
			
			List<CategoryDTO> resultados = categoryDAO.findByCriteria(c, criteria);
			
			commit = true;
			
			if (logger.isInfoEnabled()) {
				logger.info("Búsqueda por criterios completada. Resultados encontrados: {}", resultados.size());
			}
			return resultados;
			
		} catch (DataException e) {
			logger.error("Error de persistencia al buscar categorías con criteria {}: {}", criteria, e.getMessage());
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			logger.error("Error de validación al buscar categorías: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Buscando categorías por criteria {}: {}", criteria, e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			JDBCUtils.close(c, commit);
		}
	}
	
	@Override
	public boolean update(CategoryDTO category) throws ServiceException {
		Connection c = null;
		boolean commit = false;
		
		try {
			if (category == null) {
				throw new IllegalArgumentException("La categoría no puede ser null");
			}
			if (category.getId() == null) {
				throw new IllegalArgumentException("El ID de la categoría es obligatorio para actualizar");
			}
			if (category.getName() == null || category.getName().trim().isEmpty()) {
				throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
			}
			
			c = JDBCUtils.getConnection();
			c.setAutoCommit(false);
			
			boolean updated = categoryDAO.update(c, category);
			
			commit = true;
			
			if (updated) {
				logger.info("Categoría actualizada exitosamente con ID: {}", category.getId());
			} else {
				logger.warn("No se encontró categoría con ID: {} para actualizar", category.getId());
			}
			return updated;
			
		} catch (DataException e) {
			logger.error("Error de persistencia al actualizar categoría {}: {}", category, e.getMessage());
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			logger.error("Error de validación al actualizar categoría: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Actualizando categoría {}: {}", category, e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			JDBCUtils.close(c, commit);
		}
	}
	
	@Override
	public boolean delete(Long id) throws ServiceException {
		Connection c = null;
		boolean commit = false;
		
		try {
			if (id == null) {
				throw new IllegalArgumentException("El id no puede ser null");
			}
			
			c = JDBCUtils.getConnection();
			c.setAutoCommit(false);
			
			boolean deleted = categoryDAO.delete(c, id);
			
			commit = true;
			
			if (deleted) {
				logger.info("Categoría eliminada exitosamente con ID: {}", id);
			} else {
				logger.warn("No se encontró categoría con ID: {} para eliminar", id);
			}
			return deleted;
			
		} catch (DataException e) {
			logger.error("Error de persistencia al eliminar categoría con id {}: {}", id, e.getMessage());
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			logger.error("Error de validación al eliminar categoría: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Eliminando categoría con id {}: {}", id, e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			JDBCUtils.close(c, commit);
		}
	}

}
