package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.VenueCriteria;
import com.alvaro.thub.model.VenueDTO;

public interface VenueService {
    
	/**
	 * Creates a new venue and returns its ID.
	 * @param venue
	 * @return The ID of the created venue.
	 */
    public Long create(VenueDTO venue)throws ServiceException;
    
    /**
	 * Finds a venue by its ID.
	 * @param id The ID of the venue to find.
	 * @return The VenueDTO if found, or null if not found.
	 */
    public VenueDTO findById(Long id)throws ServiceException;
    
    /**
     * Finds venues that match the given criteria.
     * @param criteria
     * @return A list of VenueDTOs that match the criteria, or an empty list if no matches are found.
     */
    public List<VenueDTO> findByCriteria(VenueCriteria criteria, int from, int pageSize)throws ServiceException;
    
    /**
	 * Updates an existing venue.
	 * @param venue The VenueDTO containing the updated information. The ID must be set to identify which venue to update.
	 * @return true if the update was successful, false otherwise (e.g., if the venue does not exist).
	 */
    public boolean update(VenueDTO venue)throws ServiceException;
    
    /**
	 * Deletes a venue by its ID.
	 * @param id The ID of the venue to delete.
	 * @return true if the deletion was successful, false otherwise (e.g., if the venue does not exist).
	 */
    public boolean delete(Long id)throws ServiceException;
    
    
}