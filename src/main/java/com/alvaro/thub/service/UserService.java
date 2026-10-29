package com.alvaro.thub.service;

import com.alvaro.thub.dao.criteria.UserCriteria;
import com.alvaro.thub.model.UserDTO;
import com.alvaro.thub.utils.Results;

/**
 * API del servicio de usuario.
 */

public interface UserService {

	/**
	 * Registra un nuevo usuario en el sistema.
	 * @param user Datos del usuario a insertar.
	 * @return Id del usuario registrado, pero tambien lo setea en el objeto,
	 * y si el registro falla retorno null.
	 */
	public Long register(UserDTO user)throws ServiceException;


	/**
	 * Autentica un usuario.
	 * @param email E-mail del usuario.
	 * @param password Contraseña en claro.
	 * @return Datos del usuario si la autenticación ha sido exitosa,
	 * o null en otro caso.
	 */
	public UserDTO login(String email, String password)throws ServiceException;


	/**
	 * Busca un usuario por email. 
	 * @param email
	 * @return
	 */
	public UserDTO findByEmail(String email)throws ServiceException;


	/**
	 * Busca usuario por su id.
	 * @param id
	 * @return
	 */
	public UserDTO findById(Long id)throws ServiceException;


	/**
	 * Encuentra usuarios que cumplan los criterios especificados.
	 * @param user Criterios de búsqueda.
	 * @return Lista de usuarios que cumplen los criterios.
	 */
	public Results<UserDTO> findBy(UserCriteria user, int from, int pageSize)throws ServiceException;


	/**
	 * Actualiza todos los datos de un usuario,
	 * en base a su id.
	 * @param user Datos a actualizar.
	 * @return Si se ha podido actualizar.
	 */
	public boolean update(UserDTO user)throws ServiceException;


	/**
	 * Actualiza la contraseña de un usuario.
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 */
	public boolean updatePassword(Long id, String oldPassword, String newPassword)throws ServiceException;


	/**
	 * Genera la contraseña de un usuario y le envía una nueva por email.
	 * @param email
	 */
	public void resetPassword(String email)throws ServiceException;
	public void resetPassword(Long id) throws ServiceException;

	
	/**
	 * Borra un usuario por su ID.
	 * @param id ID del usuario a borrar.
	 * @return true si el usuario fue borrado, false en otro caso.
	 */
	public boolean delete(Long id)throws ServiceException;


	// Para 2º curso public boolean verifyEmail(Long id, String verificationCode);

}
