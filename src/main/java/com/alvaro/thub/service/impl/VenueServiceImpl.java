package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.VenueDAO;
import com.alvaro.thub.dao.criteria.VenueCriteria;
import com.alvaro.thub.model.VenueDTO;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.service.VenueService;
import com.alvaro.thub.utils.JDBCUtils;

public class VenueServiceImpl implements VenueService {
    
    private static final Logger logger = LogManager.getLogger(VenueServiceImpl.class.getName());
    
    private VenueDAO venueDAO;
    
    public VenueServiceImpl() {
        this.venueDAO = new VenueDAO();
    }

    @Override
    public Long create(VenueDTO venue) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (venue.getName() == null || venue.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del recinto es obligatorio");
            }
            if (venue.getLocalityId() == null) {
                throw new IllegalArgumentException("La localidad es obligatoria");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            VenueDTO created = venueDAO.create(c, venue);
            
            commit = true;
            return created != null ? created.getId() : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al crear recinto {}: {}", venue, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Creando recinto {}: {}", venue, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public VenueDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            VenueDTO venue = venueDAO.findById(c, id);
            
            commit = true;
            return venue;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar recinto con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<VenueDTO> findByCriteria(VenueCriteria criteria, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<VenueDTO> venues = venueDAO.findByCriteria(c, criteria);
            
            commit = true;
            return venues;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar recintos con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(VenueDTO venue) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (venue.getId() == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = venueDAO.update(c, venue);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar recinto {}: {}", venue, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando recinto {}: {}", venue, e.getMessage(), e);
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
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean deleted = venueDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar recinto con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando recinto con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}