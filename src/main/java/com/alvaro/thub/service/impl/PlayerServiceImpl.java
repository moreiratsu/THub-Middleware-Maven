package com.alvaro.thub.service.impl;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.PlayerDAO;
import com.alvaro.thub.dao.criteria.PlayerCriteria;
import com.alvaro.thub.model.PlayerDTO;
import com.alvaro.thub.service.PlayerService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;

public class PlayerServiceImpl implements PlayerService {
    
    private static final Logger logger = LogManager.getLogger(PlayerServiceImpl.class.getName());
    
    private PlayerDAO playerDAO;
    
    public PlayerServiceImpl() {
        this.playerDAO = new PlayerDAO();
    }

    @Override
    public Long create(PlayerDTO player) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (player.getDni() == null || player.getDni().trim().isEmpty()) {
                throw new IllegalArgumentException("El DNI es obligatorio");
            }
            if (player.getName() == null || player.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }
            if (player.getLastName1() == null || player.getLastName1().trim().isEmpty()) {
                throw new IllegalArgumentException("El primer apellido es obligatorio");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            PlayerDTO created = playerDAO.create(c, player);
            
            commit = true;
            return created != null ? created.getId() : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al crear jugador {}: {}", player, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Creando jugador {}: {}", player, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public PlayerDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            PlayerDTO player = playerDAO.findById(c, id);
            
            commit = true;
            return player;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar jugador con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public Results<PlayerDTO> findByCriteria(PlayerCriteria criteria, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Results<PlayerDTO> results = playerDAO.findByCriteria(c, criteria, from, pageSize);
            
            commit = true;
            return results;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar jugadores con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(PlayerDTO player) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (player.getId() == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = playerDAO.update(c, player);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar jugador {}: {}", player, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando jugador {}: {}", player, e.getMessage(), e);
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
            
            boolean deleted = playerDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar jugador con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando jugador con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}