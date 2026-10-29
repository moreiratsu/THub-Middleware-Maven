package com.alvaro.thub.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLUtils {

	public static final void addClause(Object criteriaParameter, List<String> conditions,
										String condition, List<Object> parameterValues,
										Object parameterValue) { 

		if (criteriaParameter!=null) {
			conditions.add(condition);
			parameterValues.add(parameterValue);
		}
	}
	
	/**
	 * Obtencion del total de filas de un resulset, sin repetir consulta.
	 * Metodo orientado a implementar paginacion.
	 * No existe una solucion en el API estandar de JDBC.
	 * Esta es una solucion para todas las BD pero NO ES LA MEJOR EN RENDIMIENTO.
	 * Por ello en este case es habitual usar soluciones propietarias
	 * de cada BD, como SQL_CALC_FOUND_ROWS en MySQL o COUNT(*) OVER() en PostgreSQL.
	 * Lo de OFFSET y FETCH NEXT no es compatible con esto,
	 * pero esto permite sacar emn una sola query el total de filas y los datos paginados.
	 */
	public static final int getTotalRows(ResultSet rs) throws SQLException {
		int totalRows = 0;
		if (rs.last()) {
			totalRows = rs.getRow();
		}
		rs.beforeFirst();
		return totalRows;
	}
}