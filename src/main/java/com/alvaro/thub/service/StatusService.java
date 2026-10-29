package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Status;

public interface StatusService {
    
	/**
	 * Find a status by its id
	 * @param id
	 * @return status with the given id, null if not found
	 */
    public Status findById(Long id)throws ServiceException;
    
    /**
	 * Find all the statuse
	 * @return list of all the statuses
	 */
    public List<Status> findAll()throws ServiceException;
    
    /**
	 * Find a status by its name
	 * @param name
	 * @return list of statuses with the given name, empty list if not found
	 */
    public List<Status> findByName(String name)throws ServiceException;
}