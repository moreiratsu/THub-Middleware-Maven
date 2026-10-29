package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.LocalityCriteria;
import com.alvaro.thub.model.Gender;
import com.alvaro.thub.model.LocalityDTO;

public interface LocalityService {

	/**
	 * Finds a locality by its ID.
	 * @param id the ID of the locality to find
	 * @return the found locality, or null if not found
	 */
	public LocalityDTO findById(Long id)throws ServiceException;


	/**
	 * Finds localities by criteria.
	 * @param criteria the criteria to filter localities
	 * @return a list of localities matching the criteria
	 */
	public List<LocalityDTO> findBy(LocalityCriteria criteria)throws ServiceException;


	/**
	 * Encuentra localidades por su provincia.
	 * @param provinceId el ID de la provincia para filtrar localidades
	 * @return una lista de localidades que pertenecen a la provincia especificada
	 */
	public List<LocalityDTO> findByProvinceId(Long provinceId)throws ServiceException;
	
	
	/**
	 * Encontrar todas las localidades
	 * @return la lista de localidades que hay
	 */
    public List<LocalityDTO> findAll()throws ServiceException;
}