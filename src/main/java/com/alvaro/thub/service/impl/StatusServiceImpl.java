package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.StatusDAO;
import com.alvaro.thub.model.Status;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.service.StatusService;
import com.alvaro.thub.utils.JDBCUtils;

public class StatusServiceImpl implements StatusService {
    
    private static final Logger logger = LogManager.getLogger(StatusServiceImpl.class.getName());
    
    private StatusDAO statusDAO;
    
    public StatusServiceImpl() {
        this.statusDAO = new StatusDAO();
    }

    @Override
    public Status findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Status status = statusDAO.findById(c, id);
            
            commit = true;
            return status;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar estado con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Status> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Status> statuses = statusDAO.findAll(c);
            
            commit = true;
            return statuses;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los estados: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los estados: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Status> findByName(String name) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (name == null || name.trim().isEmpty()) {
                return new java.util.ArrayList<>();
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Status> statuses = statusDAO.findByName(c, name);
            
            commit = true;
            return statuses;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar estados por nombre {}: {}", name, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando estados por nombre {}: {}", name, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}