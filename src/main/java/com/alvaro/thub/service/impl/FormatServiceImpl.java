package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.FormatDAO;
import com.alvaro.thub.model.Format;
import com.alvaro.thub.service.FormatService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;

public class FormatServiceImpl implements FormatService {
    
    private static final Logger logger = LogManager.getLogger(FormatServiceImpl.class.getName());
    
    private FormatDAO formatDAO;
    
    public FormatServiceImpl() {
        this.formatDAO = new FormatDAO();
    }

    @Override
    public Format findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Format format = formatDAO.findById(c, id);
            
            commit = true;
            return format;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar formato con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<Format> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<Format> formats = formatDAO.findAll(c);
            
            commit = true;
            return formats;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los formatos: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los formatos: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}