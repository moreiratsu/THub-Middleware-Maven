package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.model.Format;

public interface FormatService {
    
	/**	 
	 * Encuentra un formato por su ID.
	 * @param id del formato a buscar
	 * @return el formato encontrado, o null si no se encuentra ningún formato con el ID proporcionado
	 */
    public Format findById(Long id)throws ServiceException;
    
    /**
	 * Encuentra todos los formatos disponibles.
	 * @return una lista de todos los formatos disponibles
	 */
    public List<Format> findAll()throws ServiceException;
    
   
}