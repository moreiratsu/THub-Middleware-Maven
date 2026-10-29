package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.StatisticsDAO;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.service.StatisticsService;
import com.alvaro.thub.utils.JDBCUtils;

public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LogManager.getLogger(StatisticsServiceImpl.class.getName());
    
    private StatisticsDAO statisticsDAO;

    public StatisticsServiceImpl() {
        this.statisticsDAO = new StatisticsDAO();
    }

    @Override
    public Map<String, Integer> countParticipantsByAgeInMatch(Long matchId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (matchId == null) {
                throw new IllegalArgumentException("El ID del enfrentamiento no puede ser nulo");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Map<String, Integer> result = statisticsDAO.countParticipantsByAgeInMatch(c, matchId);
            
            commit = true;
            return result;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al contar participantes por edad en enfrentamiento {}: {}", matchId, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Contando participantes por edad en enfrentamiento {}: {}", matchId, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public Map<String, Integer> countParticipantsByAgeInEvent(Long eventId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (eventId == null) {
                throw new IllegalArgumentException("El ID del evento no puede ser nulo");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Map<String, Integer> result = statisticsDAO.countParticipantsByAgeInEvent(c, eventId);
            
            commit = true;
            return result;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al contar participantes por edad en evento {}: {}", eventId, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Contando participantes por edad en evento {}: {}", eventId, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}