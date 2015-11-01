package py.una.web.tarea4.util;

import java.io.Serializable;

public class CompraDetalleJson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2149842770929160004L;

	private Integer cantidad;

	private Integer precio;
	
	private Integer producto;

	public Integer getCantidad() {
		return cantidad;
	}

	public Integer getPrecio() {
		return precio;
	}

	public Integer getProducto() {
		return producto;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public void setProducto(Integer producto) {
		this.producto = producto;
	}

	public CompraDetalleJson(Integer cantidad, Integer precio, Integer producto) {
		super();
		this.cantidad = cantidad;
		this.precio = precio;
		this.producto = producto;
	}
	
	
}
