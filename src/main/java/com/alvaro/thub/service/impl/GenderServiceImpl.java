package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.GenderDAO;
import com.alvaro.thub.model.Gender;
import com.alvaro.thub.service.GenderService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class GenderServiceImpl implements GenderService {
    
    private static final Logger logger = LogManager.getLogger(GenderServiceImpl.class.getName());
    
    private GenderDAO genderDAO;
    
    public GenderServiceImpl() {
        this.genderDAO = new GenderDAO();
    }

    @Override
    public Gender findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Gender gender = genderDAO.findById(c, id);
            
            commit = true;
            return gender;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar género con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Gender> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Gender> genders = genderDAO.findAll(c);
            
            commit = true;
            return genders;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los géneros: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los géneros: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}