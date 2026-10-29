package com.alvaro.thub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alvaro.thub.utils.DAOUtils;
import com.alvaro.thub.utils.JDBCUtils;

public class StatisticsDAO {

	private static Logger logger = LogManager.getLogger(StatisticsDAO.class.getName());
	
	public StatisticsDAO() {

	}

	// Cuenta el número de participantes por rango de edad en 1 enfrentamiento específico
	public Map<String, Integer> countParticipantsByAgeInMatch(Connection c,Long matchId) throws DataException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			String sql = "WITH player_age AS ( " +
					"  SELECT  " +
					"    p.id, " +
					"    TIMESTAMPDIFF(YEAR, p.born_date, CURDATE()) AS age " +
					"  FROM player p " +
					"  WHERE p.born_date IS NOT NULL " +
					"), " +
					"age_ranges AS ( " +
					"  SELECT  " +
					"    id, " +
					"    CASE  " +
					"      WHEN age < 18 THEN 'Menor de 18 años' " +
					"      WHEN age BETWEEN 18 AND 30 THEN '18 a 30 años' " +
					"      WHEN age BETWEEN 31 AND 45 THEN '31 a 45 años' " +
					"      WHEN age BETWEEN 46 AND 64 THEN '46 a 64 años' " +
					"      WHEN age >= 65 THEN '65 años o más' " +
					"      ELSE 'Edad no disponible' " +
					"    END AS age_range " +
					"  FROM player_age " +
					") " +
					"SELECT  " +
					"  ar.age_range, " +
					"  COUNT(DISTINCT p.id) AS participantes " +
					"FROM team_match tm " +
					"INNER JOIN player p ON tm.team_id = p.team_id " +
					"LEFT JOIN age_ranges ar ON p.id = ar.id " +
					"WHERE tm.match_id = ? " +
					"GROUP BY ar.age_range " +
					"ORDER BY FIELD(ar.age_range, " +
					"  'Menor de 18 años', " +
					"  '18 a 30 años', " +
					"  '31 a 45 años', " +
					"  '46 a 64 años', " +
					"  '65 años o más', " +
					"  'Edad no disponible' " +
					")";
			ps = c.prepareStatement(sql);
			DAOUtils.setParameters(ps, matchId);
			rs = ps.executeQuery();

			Map<String, Integer> participantesPorEdad = new HashMap<String, Integer>();


			while (rs.next()) {
				int i = 1;
				String rango = rs.getString(i++);
				Integer participantes = rs.getInt(i++);
				participantesPorEdad.put(rango, participantes);
			}
			return participantesPorEdad;
		} catch (SQLException e) {
			logger.error("Error al contar participantes por edad en el enfrentamiento con ID: " + matchId, e);
			throw new DataException("Error al contar participantes por edad en el enfrentamiento con ID: " + matchId, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}


	// cuenta el numero de participantes por rango de edad de un evento completo
	public Map<String, Integer> countParticipantsByAgeInEvent(Connection c,Long eventId) throws DataException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "WITH player_age AS ( " +
					"  SELECT  " +
					"    p.id, " +
					"    TIMESTAMPDIFF(YEAR, p.born_date, CURDATE()) AS age " +
					"  FROM player p " +
					"  WHERE p.born_date IS NOT NULL " +
					"), " +
					"age_ranges AS ( " +
					"  SELECT  " +
					"    id, " +
					"    CASE  " +
					"      WHEN age < 18 THEN 'Menor de 18 años' " +
					"      WHEN age BETWEEN 18 AND 30 THEN '18 a 30 años' " +
					"      WHEN age BETWEEN 31 AND 45 THEN '31 a 45 años' " +
					"      WHEN age BETWEEN 46 AND 64 THEN '46 a 64 años' " +
					"      WHEN age >= 65 THEN '65 años o más' " +
					"      ELSE 'Edad no disponible' " +
					"    END AS age_range " +
					"  FROM player_age " +
					") " +
					"SELECT  " +
					"  ar.age_range, " +
					"  COUNT(DISTINCT p.id) AS participantes " +
					"FROM `match` m " +     // En MySQL, "match" es una palabra reservada, por eso se pone entre comillas invertidas
					"INNER JOIN team_match tm ON m.id = tm.match_id " +
					"INNER JOIN player p ON tm.team_id = p.team_id " +
					"LEFT JOIN age_ranges ar ON p.id = ar.id " +
					"WHERE m.event_id = ? " +
					"GROUP BY ar.age_range " +
					"ORDER BY FIELD(ar.age_range, " +
					"  'Menor de 18 años', " +
					"  '18 a 30 años', " +
					"  '31 a 45 años', " +
					"  '46 a 64 años', " +
					"  '65 años o más', " +
					"  'Edad no disponible' " +
					")";
			ps = c.prepareStatement(sql);
			DAOUtils.setParameters(ps, eventId);
			rs = ps.executeQuery();

			Map<String, Integer> participantesPorEdad = new HashMap<String, Integer>();

			while (rs.next()) {
				int i = 1;
				String rango = rs.getString(i++);
				Integer participantes = rs.getInt(i++);
				participantesPorEdad.put(rango, participantes);
			}
			return participantesPorEdad;
		} catch (SQLException e) {
			logger.error("Error al contar participantes por edad en el evento con ID: " + eventId, e);
			throw new DataException("Error al contar participantes por edad en el evento con ID: " + eventId, e);
		} finally {
			JDBCUtils.close(rs, ps);
		}
	}
}



