package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Rol;

public interface RolService {
    
	/**
	 * Busca un rol por su id
	 * @param id
	 * @return El rol encontrado o null si no se encuentra
	 */
    public Rol findById(Long id)throws ServiceException;
    
    /**
	 * Busca un rol por su nombre
	 * @param name
	 * @return El rol encontrado o null si no se encuentra
	 */
    public List<Rol> findAll()throws ServiceException;
    
    
}