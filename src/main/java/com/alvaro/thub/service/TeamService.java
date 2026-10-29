package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.TeamCriteria;
import com.alvaro.thub.model.TeamDTO;
import com.alvaro.thub.utils.Results;

public interface TeamService {
    
	/** * Creates a new team.
	 *
	 * @param team the team to create
	 * @return the ID of the created team
	 */
    public Long create(TeamDTO team)throws ServiceException;
    
    /**
     * Finds a team by its ID.
     * @param id
     * @return the team with the specified ID, or null if not found
     */
    public TeamDTO findById(Long id)throws ServiceException;
    
    /**
     * 	Finds teams that match the specified criteria.
     * @param criteria, and from and pageSize for pagination
     * @return a list of teams that match the criteria
     */
    public Results<TeamDTO> findByCriteria(TeamCriteria criteria, int from, int pageSize)throws ServiceException;
    
    /** Updates an existing team.
	 * @param team the team to update
	 * @return true if the update was successful, false otherwise
	 */
    public boolean update(TeamDTO team)throws ServiceException;
    
    /** Deletes a team by its ID.
     * @param id
     * @return true if the deletion was successful, false otherwise
     */
    public boolean delete(Long id)throws ServiceException;
    
    
    /** Finds all teams.
	 * @return a list of all teams
	 */
	public List<TeamDTO> findAll()throws ServiceException;
    
    
}