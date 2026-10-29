package com.alvaro.thub.service;

import java.util.List;

import com.alvaro.thub.dao.criteria.EventCriteria;
import com.alvaro.thub.model.Event;
import com.alvaro.thub.model.EventDTO;
import com.alvaro.thub.utils.Results;

/**
 * API del servicio de eventos.
 */
public interface EventService {

    /**
     * Crea un nuevo evento en el sistema.
     * @param event Datos del evento a crear.
     * @return Id del evento creado, y también lo setea en el objeto.
     * Retorna null si la creación falla.
     */
    public EventDTO create(Event event)throws ServiceException;

    /**
     * Busca un evento por su ID.
     * @param id ID del evento.
     * @return Datos del evento si existe, null en otro caso.
     */
    public EventDTO findById(Long id)throws ServiceException;

    /**
     * Encuentra eventos que cumplan los criterios especificados.
     * @param criteria Criterios de búsqueda.
     * @return Lista de eventos que cumplen los criterios.
     */
    public Results<EventDTO> findBy(EventCriteria criteria, int from, int pageSize)throws ServiceException;

    /**
     * Actualiza todos los datos de un evento.
     * @param event Datos a actualizar.
     * @return true si se ha podido actualizar, false en otro caso.
     */
    public boolean update(Event event)throws ServiceException;
    /**
     * Actualiza el estado de un evento.
     * @param id ID del evento.
     * @param statusId ID del nuevo estado.
     * @return true si se ha podido actualizar, false en otro caso.
     */
    public boolean updateStatus(Long id, Long statusId)throws ServiceException;

    /**
     * Borra un evento por su ID.
     * @param id ID del evento a borrar.
     * @return true si el evento fue borrado, false en otro caso.
     */
    public boolean delete(Long id)throws ServiceException;

    /**
     * Verifica si el usuario tiene permisos para editar el evento.
     * @param eventId ID del evento.
     * @param userId ID del usuario.
     * @return true si el usuario puede editar el evento, false en otro caso.
     */
//    public boolean canUserEditEvent(Long eventId, Long userId);

    /**
     * Obtiene todos los eventos creados por un usuario.
     * @param userId ID del usuario creador.
     * @return Lista de eventos creados por el usuario.
     */
    public List<EventDTO> findByCreator(Long userId)throws ServiceException;
}