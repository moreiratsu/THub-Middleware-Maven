package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Gender;

public interface GenderService {
    
	/**
	 * Encontrar un género por su id
	 * @param id
	 * @return el género encontrado o null si no existe
	 */
    public Gender findById(Long id)throws ServiceException;
    
    /**
	 * Encontrar todos los géneros
	 * @return la lista de géneros que hay
	 */
    public List<Gender> findAll()throws ServiceException;
    
    
}