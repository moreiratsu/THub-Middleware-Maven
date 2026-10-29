package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Sport;

public interface SportService {
    
	/**
	 * Find a sport by its id
	 * @param id
	 * @return the sport with the given id, or null if not found
	 */
    public Sport findById(Long id)throws ServiceException;
    
    /**
	 * Find all sports
	 * @return a list of all sports
	 */
    public List<Sport> findAll()throws ServiceException;
    
    /**
	 * Find sports by their name
	 * @param name
	 * @return a list of sports with the given name, or an empty list if not found
	 */
    public List<Sport> findByName(String name)throws ServiceException;
}