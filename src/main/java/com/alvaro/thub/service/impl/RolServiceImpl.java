package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.RolDAO;
import com.alvaro.thub.model.Rol;
import com.alvaro.thub.service.RolService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class RolServiceImpl implements RolService {
    
    private static final Logger logger = LogManager.getLogger(RolServiceImpl.class.getName());
    
    private RolDAO rolDAO;
    
    public RolServiceImpl() {
        this.rolDAO = new RolDAO();
    }

    @Override
    public Rol findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Rol rol = rolDAO.findById(c, id);
            
            commit = true;
            return rol;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar rol con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Rol> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Rol> roles = rolDAO.findAll(c);
            
            commit = true;
            return roles;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los roles: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los roles: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}