package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.ProvinceDAO;
import com.alvaro.thub.dao.criteria.ProvinceCriteria;
import com.alvaro.thub.model.ProvinceDTO;
import com.alvaro.thub.service.ProvinceService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class ProvinceServiceImpl implements ProvinceService {
    
    private static final Logger logger = LogManager.getLogger(ProvinceServiceImpl.class.getName());
    
    private ProvinceDAO provinceDAO;
    
    public ProvinceServiceImpl() {
        this.provinceDAO = new ProvinceDAO();
    }

    @Override
    public ProvinceDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            ProvinceDTO province = provinceDAO.findById(c, id);
            
            commit = true;
            return province;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar provincia con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<ProvinceDTO> findBy(ProvinceCriteria criteria) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<ProvinceDTO> provinces = provinceDAO.findBy(c, criteria);
            
            commit = true;
            return provinces;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar provincias con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<ProvinceDTO> findByCountry(Long countryId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (countryId == null) {
                return new java.util.ArrayList<>();
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<ProvinceDTO> provinces = provinceDAO.findByCountryId(c, countryId);
            
            commit = true;
            return provinces;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar provincias por país {}: {}", countryId, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando provincias por país {}: {}", countryId, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}