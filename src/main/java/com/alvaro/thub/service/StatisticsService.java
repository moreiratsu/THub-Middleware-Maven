package com.alvaro.thub.service;

import java.util.Map;


public interface StatisticsService {

	/**
	 * cuenta el número de participantes por rango de edad en 1 enfrentamiento específico
	 * @param matchId
	 * @return un mapa con el rango de edad como clave y el número de participantes como valor
	 */
	public Map<String, Integer> countParticipantsByAgeInMatch(Long matchId)throws ServiceException; 

	/**
	 * Cuenta el número de participantes por rango de edad en un evento completo
	 * @param eventId ID del evento
	 * @return Mapa con rango de edad y número de participantes
	 */
	public Map<String, Integer> countParticipantsByAgeInEvent(Long eventId)throws ServiceException;
}
