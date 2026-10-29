package com.alvaro.thub.service.impl;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.dao.DataException;
import com.alvaro.thub.dao.UserDAO;
import com.alvaro.thub.dao.criteria.UserCriteria;
import com.alvaro.thub.model.UserDTO;
import com.alvaro.thub.service.EncryptionService;
import com.alvaro.thub.service.MailService;
import com.alvaro.thub.service.ServiceException;
import com.alvaro.thub.service.UserService;
import com.alvaro.thub.utils.JDBCUtils;
import com.alvaro.thub.utils.Results;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
    
    private EncryptionService encryptionService = null;
    private MailService mailService = null;
    private UserDAO userDAO = null;

    public UserServiceImpl() {
        encryptionService = new EncryptionServiceBCryptImpl();
        mailService = new MailServiceApacheImpl();
        userDAO = new UserDAO();
    }

    @Override
    public Long register(UserDTO user) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (user == null) {
                throw new IllegalArgumentException("El usuario no puede ser null");
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email es obligatorio");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña es obligatoria");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            // Comprobar si ya existe
            UserDTO existingUser = userDAO.findByEmail(c, user.getEmail());
            if (existingUser != null) {
                throw new ServiceException("Ya existe un usuario con el email: " + user.getEmail());
            }
            
            // Si no existe, lo insertamos
            String passwordEncrypted = encryptionService.encrypt(user.getPassword());
            user.setPassword(passwordEncrypted);
            
            UserDTO created = userDAO.create(c, user);
            
            // Enviar email
            mailService.sendEmail(user.getEmail(), 
                    "Bienvenido a THub", 
                    "Hola " + user.getName() + ", ...");
            
            commit = true;
            return created != null ? created.getId() : null;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al registrar usuario {}: {}", user, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Registrando usuario {}: {}", user, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public UserDTO login(String email, String password) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("El email es obligatorio");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña es obligatoria");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            UserDTO user = userDAO.findByEmail(c, email);
            if (user == null) {
                System.out.println(">>> [DEBUG SERVICE]: userDAO.findByEmail() devolvió NULL para el email: " + email);
                return null;
            }
            
            System.out.println(">>> [DEBUG SERVICE]: Usuario encontrado en BD.");
            System.out.println("    -> Password en BD: [" + user.getPassword() + "]");
            System.out.println("    -> Password introducida: [" + password + "]");
            
            boolean matches = encryptionService.checkEncryption(password, user.getPassword());
            System.out.println(">>> [DEBUG SERVICE]: ¿Coincide la contraseña con BCrypt? = " + matches);

            if (matches) {
                commit = true;
                return user;
            } else {
                return null;
            }
            
        } catch (DataException e) {
            logger.error("Error de persistencia al hacer login con email {}: {}", email, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Login con email {}: {}", email, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public UserDTO findByEmail(String email) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (email == null || email.trim().isEmpty()) {
                return null;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            UserDTO user = userDAO.findByEmail(c, email);
            
            commit = true;
            return user;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar usuario por email {}: {}", email, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando usuario por email {}: {}", email, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public UserDTO findById(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser null");
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            UserDTO user = userDAO.findById(c, id);
            
            commit = true;
            return user;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar usuario con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public Results<UserDTO> findBy(UserCriteria user, int from, int pageSize) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            Results<UserDTO> results = userDAO.findBy(c, user, from, pageSize);
            
            commit = true;
            return results;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar usuarios con criteria {}: {}", user, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Buscando por criteria {}: {}", user, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean update(UserDTO user) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (user.getId() == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            boolean updated = userDAO.update(c, user);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar usuario {}: {}", user, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando usuario {}: {}", user, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            if (id == null) {
                return false;
            }
            
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            UserDTO user = userDAO.findById(c, id);
            if (user == null) {
                return false;
            }
            
            // Comprobar contraseña antigua
            if (!encryptionService.checkEncryption(oldPassword, user.getPassword())) {
                return false;
            }
            
            // Comprobar que la nueva no es igual a la antigua
            if (encryptionService.checkEncryption(newPassword, user.getPassword())) {
                return false;
            }
            
            // Actualizar a la nueva
            String newPasswordEncrypted = encryptionService.encrypt(newPassword);
            user.setPassword(newPasswordEncrypted);
            
            boolean updated = userDAO.update(c, user);
            
            commit = true;
            return updated;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar contraseña del usuario con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Actualizando contraseña del usuario con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public void resetPassword(String email) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            // TODO: Implementar reset de contraseña por email
            
            commit = true;
            
        } catch (Exception e) {
            logger.error("Reset password por email {}: {}", email, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }

    @Override
    public void resetPassword(Long id) throws ServiceException {
        Connection c = null;
        boolean commit = false;
        
        try {
            c = JDBCUtils.getConnection();
            c.setAutoCommit(false);
            
            // TODO: Implementar reset de contraseña por id
            
            commit = true;
            
        } catch (Exception e) {
            logger.error("Reset password por id {}: {}", id, e.getMessage(), e);
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
            
            UserDTO user = userDAO.findById(c, id);
            if (user == null) {
                return false;
            }
            
            boolean deleted = userDAO.delete(c, id);
            
            commit = true;
            return deleted;
            
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar usuario con id {}: {}", id, e.getMessage());
            throw new ServiceException(e);
        } catch (Exception e) {
            logger.error("Eliminando usuario con id {}: {}", id, e.getMessage(), e);
            throw new ServiceException(e);
        } finally {
            JDBCUtils.close(c, commit);
        }
    }
}