package com.alvaro.thub.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.TeamDAO;
import com.alvaro.thub.dao.criteria.TeamCriteria;
import com.alvaro.thub.model.TeamDTO;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.service.TeamService;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;

public class TeamServiceImpl implements TeamService {
    
    private static final Logger logger = LogManager.getLogger(TeamServiceImpl.class.getName());
    
    private TeamDAO teamDAO;
    
    public TeamServiceImpl() {
        this.teamDAO = new TeamDAO();
    }

    @Override
    public Long create(TeamDTO team) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (team.getName() == null || team.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del equipo es obligatorio");
            }
            if (team.getSportId() == null) {
                throw new IllegalArgumentException("El deporte es obligatorio");
            }
            if (team.getLocalityId() == null) {
                throw new IllegalArgumentException("La localidad es obligatoria");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            TeamDTO created = teamDAO.create(c, team);
            
            commit = true;
            return created != null ? created.getId() : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al crear equipo {}: {}", team, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Creando equipo {}: {}", team, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public TeamDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            TeamDTO team = teamDAO.findById(c, id);
            
            commit = true;
            return team;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar equipo con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public Results<TeamDTO> findByCriteria(TeamCriteria criteria, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Results<TeamDTO> results = teamDAO.findByCriteria(c, criteria, from, pageSize);
            
            commit = true;
            return results;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar equipos con criteria {}: {}", criteria, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", criteria, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(TeamDTO team) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (team.getId() == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = teamDAO.update(c, team);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar equipo {}: {}", team, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando equipo {}: {}", team, e.getMessage(), e);
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
            
            boolean deleted = teamDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar equipo con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando equipo con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
    
    @Override
    public List<TeamDTO> findAll() throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            List<TeamDTO> teams = teamDAO.findAll(c);
            
            commit = true;
            return teams;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar todos los equipos: {}", e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando todos los equipos: {}", e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}