package com.alvaro.thub.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DAOUtils {

	public static void setParameters(PreparedStatement ps, List<Object> params) throws SQLException {
	    int i = 1;
	    for (Object param : params) {
	        ps.setObject(i++, param);
	    }
	}
	// Método nuevo que acepta argumentos sueltos (...)
	public static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
	    setParameters(ps, Arrays.asList(params)); // Reutiliza tu método original
	}
	
}