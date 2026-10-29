package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.LocalityDAO;
import com.alvaro.thub.dao.criteria.LocalityCriteria;
import com.alvaro.thub.model.LocalityDTO;
import com.alvaro.thub.service.LocalityService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class LocalityServiceImpl implements LocalityService {
    
    private static final Logger logger = LogManager.getLogger(LocalityServiceImpl.class.getName());
    
    private LocalityDAO localityDAO;
    
    public LocalityServiceImpl() {
        this.localityDAO = new LocalityDAO();
    }

    @Override
    public LocalityDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            LocalityDTO locality = localityDAO.findById(c, id);
            
            commit = true;
            return locality;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar localidad con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<LocalityDTO> findBy(LocalityCriteria criteria) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<LocalityDTO> localities = localityDAO.findBy(c, criteria);
            
            commit = true;
            return localities;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar localidades con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<LocalityDTO> findByProvinceId(Long provinceId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (provinceId == null) {
                return new java.util.ArrayList<>();
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<LocalityDTO> localities = localityDAO.findByProvinceId(c, provinceId);
            
            commit = true;
            return localities;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar localidades por provincia {}: {}", provinceId, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando localidades por provincia {}: {}", provinceId, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
    
    @Override
    public List<LocalityDTO> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<LocalityDTO> localities = localityDAO.findAll(c);
            
            commit = true;
            return localities;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todas las localidades: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todas las localidades: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}