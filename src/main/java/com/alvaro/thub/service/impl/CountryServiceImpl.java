package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.CountryDAO;
import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.model.Country;
import com.alvaro.thub.service.CountryService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class CountryServiceImpl implements CountryService {
    
    private static final Logger logger = LogManager.getLogger(CountryServiceImpl.class.getName());
    
    private CountryDAO countryDAO;
    
    public CountryServiceImpl() {
        this.countryDAO = new CountryDAO();
    }

    @Override
    public Country findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Country country = countryDAO.findById(c, id);
            
            commit = true;
            return country;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar país con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Country> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Country> countries = countryDAO.findAll(c);
            
            commit = true;
            return countries;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los países: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los países: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}