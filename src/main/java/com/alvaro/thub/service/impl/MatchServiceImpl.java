package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.MatchDAO;
import com.alvaro.thub.dao.criteria.MatchCriteria;
import com.alvaro.thub.model.MatchDTO;
import com.alvaro.thub.service.MatchService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class MatchServiceImpl implements MatchService {
    
    private static final Logger logger = LogManager.getLogger(MatchServiceImpl.class.getName());
    
    private MatchDAO matchDAO;
    
    public MatchServiceImpl() {
        this.matchDAO = new MatchDAO();
    }

    @Override
    public Long create(MatchDTO match) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (match.getDate() == null) {
                throw new IllegalArgumentException("La fecha del partido es obligatoria");
            }
            if (match.getEventId() == null) {
                throw new IllegalArgumentException("El evento es obligatorio");
            }
            if (match.getVenueId() == null) {
                throw new IllegalArgumentException("El recinto es obligatorio");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            MatchDTO created = matchDAO.create(c, match);
            
            commit = true;
            return created != null ? created.getId() : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al crear partido {}: {}", match, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Creando partido {}: {}", match, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public MatchDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            MatchDTO match = matchDAO.findById(c, id);
            
            commit = true;
            return match;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar partido con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<MatchDTO> findByCriteria(MatchCriteria criteria, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<MatchDTO> matches = matchDAO.findByCriteria(c, criteria);
            
            commit = true;
            return matches;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar partidos con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(MatchDTO match) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (match.getId() == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = matchDAO.update(c, match);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar partido {}: {}", match, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando partido {}: {}", match, e.getMessage(), e);
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
            
            boolean deleted = matchDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar partido con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando partido con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}