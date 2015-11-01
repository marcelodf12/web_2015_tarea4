package py.una.web.tarea4.util;

import java.io.Serializable;
import java.util.List;

public class CompraJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4819730310442005098L;

	private String fecha;
	
	private String proveedor;
	
	private List<CompraDetalleJson> detalles;

	public String getFecha() {
		return fecha;
	}

	public String getProveedor() {
		return proveedor;
	}

	public List<CompraDetalleJson> getDetalles() {
		return detalles;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public void setDetalles(List<CompraDetalleJson> detalles) {
		this.detalles = detalles;
	}

	public CompraJson(String fecha, String proveedor,
			List<CompraDetalleJson> detalles) {
		super();
		this.fecha = fecha;
		this.proveedor = proveedor;
		this.detalles = detalles;
	}
	
	
}
