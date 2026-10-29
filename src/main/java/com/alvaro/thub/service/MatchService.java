package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.MatchCriteria;
import com.alvaro.thub.model.MatchDTO;

public interface MatchService {

	/**
	 * Creates a new match.
	 *
	 * @param match the match to create
	 * @return the ID of the created match
	 */
	public Long create(MatchDTO match)throws ServiceException;

	/**
	 * Finds a match by its ID.
	 * @param id
	 * @return the match with the specified ID, or null if not found
	 */
	public MatchDTO findById(Long id)throws ServiceException;

	/**
	 * Encontrar partidos que cumplan con los criterios especificados.
	 * @param criteria
	 * @return una lista de partidos que cumplen con los criterios, o una lista vacía si no se encuentran partidos que coincidan
	 */
	public List<MatchDTO> findByCriteria(MatchCriteria criteria, int from, int pageSize)throws ServiceException;

	/**
	 * Actualiza un partido existente.
	 * @param match el partido con los datos actualizados
	 * @return true si la actualización fue exitosa, false si el partido no existe o no se pudo actualizar
	 */
	public boolean update(MatchDTO match)throws ServiceException;

	/**
	 * Elimina un partido por su ID.
	 * @param id el ID del partido a eliminar
	 * @return true si la eliminación fue exitosa, false si el partido no existe o no se pudo eliminar
	 */
	public boolean delete(Long id)throws ServiceException;


}