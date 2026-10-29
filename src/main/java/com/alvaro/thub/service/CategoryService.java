package com.alvaro.thub.service;
import java.util.List;

import com.alvaro.thub.dao.criteria.CategoryCriteria;
import com.alvaro.thub.model.CategoryDTO;

public interface CategoryService {

	/**
	 * Crea una nueva categoría en el sistema.
	 * @param category Datos de la categoría a crear.
	 * @return Id de la categoría creada, y también lo setea en el objeto.
	 * Retorna null si la creación falla.
	 * @throws ServiceException 
	 */
	public CategoryDTO create(CategoryDTO category) throws ServiceException;
	
	/**
	 * Busca una categoría por su ID.
	 * @param id ID de la categoría.
	 * @return Datos de la categoría si existe, null en otro caso.
	 */
	public CategoryDTO findById(Long id)throws ServiceException;
	
	/**
	 * Encuentra categorías que cumplan los criterios especificados.
	 * @param criteria Criterios de búsqueda.
	 * @return Lista de categorías que cumplen los criterios.
	 */
	public List<CategoryDTO> findBy(CategoryCriteria criteria)throws ServiceException;
	
	/**
	 * Actualiza todos los datos de una categoría.
	 * @param category Datos a actualizar.
	 * @return true si se ha podido actualizar, false en otro caso.
	 */
	public boolean update(CategoryDTO category)throws ServiceException;
	
	/**
	 * Borra una categoría por su ID.
	 * @param id ID de la categoría a borrar.
	 * @return true si la categoría fue borrada, false en otro caso.
	 */
	public boolean delete(Long id)throws ServiceException;
	
}
