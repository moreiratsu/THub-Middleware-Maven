package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.ProvinceCriteria;
import com.alvaro.thub.model.ProvinceDTO;

public interface ProvinceService {
    
	/**
	 * Find a province by its id.
	 * @param id the id of the province to find.
	 * @return the province with the given id, or null if not found.
	 */
    public ProvinceDTO findById(Long id)throws ServiceException;
    
    
    /**
	 * find provinces by criteria.
	 * @param criteria the criteria to use for finding provinces.
	 * @return a list of provinces that match the given criteria.
	 */
    public List<ProvinceDTO> findBy(ProvinceCriteria criteria)throws ServiceException;
    
    /**
     * Find by Country.
     * @param countryId the id of the country to find provinces for.
     * @return a list of provinces that belong to the given country.
     */
    public List<ProvinceDTO> findByCountry(Long countryId)throws ServiceException;
}