package com.alvaro.thub.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para representar resultados paginados, con la lista de resultados y el total de registros que cumplen el criterio de búsqueda
 * para poder calcular el número de páginas.
 * TIPOS PARAMETRIZADOS: se puede usar esta clase para cualquier tipo de resultado, por ejemplo List<TeamDTO>, List<PlayerDTO>, etc.
 */
public class Results<T> {

	private List<T> page = null; // Lista de resultados de la página actual
	private int total = 0; // Total de registros que cumplen el criterio de búsqueda (sin paginar)
	
	public Results() {
		page = new ArrayList<T>();
	}

	public List<T> getPage() {
		return page;
	}

	public void setPage(List<T> page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
