package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Country;

public interface CountryService {
    
	/**
	 * Find a country by id
	 * @param id
	 * @return Country  
	 */
    public Country findById(Long id)throws ServiceException;
    
    
    /**
	 * Find all countries
	 * @return List of Country
	 */
    public List<Country> findAll()throws ServiceException;
    
    
}