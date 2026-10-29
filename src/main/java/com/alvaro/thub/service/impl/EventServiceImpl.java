package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.EventDAO;
import com.alvaro.thub.dao.criteria.EventCriteria;
import com.alvaro.thub.model.Event;
import com.alvaro.thub.model.EventDTO;
import com.alvaro.thub.service.EventService;
import com.alvaro.thub.service.MailService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;

public class EventServiceImpl implements EventService {

    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class.getName());
    
    private EventDAO eventDAO = null;
    private MailService mailService = null;

    public EventServiceImpl() {
        eventDAO = new EventDAO();
        mailService = new MailServiceApacheImpl();
    }

    @Override
    public EventDTO create(Event event) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (event == null || event.getName() == null || event.getStartDate() == null || 
                event.getEndDate() == null || event.getCreatorUserId() == null) {
                throw new IllegalArgumentException("Datos obligatorios del evento no proporcionados");
            }
            if (event.getStartDate().after(event.getEndDate())) {
                throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Event createdEvent = eventDAO.create(c, event);
            
            commit = true;
            return createdEvent != null ? eventDAO.findById(c, createdEvent.getId()) : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al crear evento {}: {}", event, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Creando evento {}: {}", event, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public EventDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                return null;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            EventDTO event = eventDAO.findById(c, id);
            
            commit = true;
            return event;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar evento con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public Results<EventDTO> findBy(EventCriteria criteria, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Results<EventDTO> resultados = eventDAO.findBy(c, criteria, from, pageSize);
            
            commit = true;
            return resultados;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar eventos con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(Event event) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (event.getStartDate() != null && event.getEndDate() != null && 
                event.getStartDate().after(event.getEndDate())) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = eventDAO.update(c, event);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar evento {}: {}", event, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando evento {}: {}", event, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean updateStatus(Long id, Long statusId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null || statusId == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = eventDAO.updateStatus(c, id, statusId);
            
            if (updated) {
                EventDTO event = eventDAO.findById(c, id);
                sendStatusChangeNotification(event, statusId);
            }
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar estado del evento con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando estado del evento con id {}: {}", id, e.getMessage(), e);
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
            
            EventDTO event = eventDAO.findById(c, id);
            if (event == null) {
                return false;
            }
            
            boolean deleted = eventDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar evento con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando evento con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public List<EventDTO> findByCreator(Long userId) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (userId == null) {
                return new java.util.ArrayList<>();
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            EventCriteria criteria = new EventCriteria();
            criteria.setCreatorUserId(userId);
            
            Results<EventDTO> results = eventDAO.findBy(c, criteria, 1, Integer.MAX_VALUE);
            
            commit = true;
            return results != null ? results.getPage() : new java.util.ArrayList<>();
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar eventos por creador {}: {}", userId, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando eventos por creador {}: {}", userId, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    private void sendStatusChangeNotification(EventDTO event, Long newStatusId) {
        try {
            String subject = "Cambio de estado en tu evento - " + event.getName();
            String message = "Hola " + event.getCreatorUserName() + ",\n\n" +
                            "El estado de tu evento \"" + event.getName() + "\" ha cambiado.\n" +
                            "Nuevo estado: " + event.getStatusName() + "\n\n" +
                            "Gracias por usar THub.";
            
            mailService.sendEmail(event.getCreatorUserEmail(), subject, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}