package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.SportDAO;
import com.alvaro.thub.model.Sport;
import com.alvaro.thub.service.SportService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class SportServiceImpl implements SportService {
    
    private static final Logger logger = LogManager.getLogger(SportServiceImpl.class.getName());
    
    private SportDAO sportDAO;
    
    public SportServiceImpl() {
        this.sportDAO = new SportDAO();
    }

    @Override
    public Sport findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Sport sport = sportDAO.findById(c, id);
            
            commit = true;
            return sport;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar deporte con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Sport> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Sport> sports = sportDAO.findAll(c);
            
            commit = true;
            return sports;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los deportes: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los deportes: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Sport> findByName(String name) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (name == null || name.trim().isEmpty()) {
                return new java.util.ArrayList<>();
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Sport> sports = sportDAO.findByName(c, name);
            
            commit = true;
            return sports;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar deportes por nombre {}: {}", name, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando deportes por nombre {}: {}", name, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}