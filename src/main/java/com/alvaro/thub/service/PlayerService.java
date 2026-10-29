package com.alvaro.thub.service;

import com.alvaro.thub.dao.criteria.PlayerCriteria;
import com.alvaro.thub.model.PlayerDTO;
import com.alvaro.thub.utils.Results;

public interface PlayerService {

	/**
	 * Creates a new player in the system.
	 * @param player
	 * @return 
	 */
	public Long create(PlayerDTO player)throws ServiceException;

	/**
	 * Retrieves a player by their unique identifier.
	 * @param id
	 * @return the player DTO if found, otherwise null
	 */
	public PlayerDTO findById(Long id)throws ServiceException;

	/**
	 * Finds players based on the provided criteria.
	 * @param criteria
	 * @return a list of players matching the criteria
	 */
	public Results<PlayerDTO> findByCriteria(PlayerCriteria criteria, int from, int pageSize)throws ServiceException;

	/**
	 * Updates the information of an existing player.
	 * @param player
	 * @return it returns true if the update was successful, false otherwise
	 */
	public boolean update(PlayerDTO player)throws ServiceException;

	/**
	 * Deletes a player from the system by their unique identifier.
	 * @param id
	 * @return it returns true if the deletion was successful, false otherwise
	 */
	public boolean delete(Long id)throws ServiceException;

}