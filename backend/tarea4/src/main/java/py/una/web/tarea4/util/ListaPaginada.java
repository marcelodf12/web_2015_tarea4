package py.una.web.tarea4.util;

import java.util.ArrayList;

public class ListaPaginada<T> {
	
	private ArrayList<T> lista;

	private Integer cantidadDePaginas;

	private Integer paginaActual;

	public ArrayList<T> getLista() {
		return lista;
	}

	public Integer getCantidadDePaginas() {
		return cantidadDePaginas;
	}

	public Integer getPaginaActual() {
		return paginaActual;
	}

	public void setLista(ArrayList<T> lista) {
		this.lista = lista;
	}

	public void setCantidadDePaginas(Integer cantidadDePaginas) {
		this.cantidadDePaginas = cantidadDePaginas;
	}

	public void setPaginaActual(Integer paginaActual) {
		this.paginaActual = paginaActual;
	}


}
