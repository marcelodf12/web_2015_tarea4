package py.una.web.tarea4.util;

import java.util.List;

public class VentaJson {
	
	private String fecha;
	
	private String nombreCliente;
	
	private String cliente;
	
	private List<VentaDetalleJson> detalles;
	
	private Integer montoTotal; 

	public String getFecha() {
		return fecha;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getCliente() {
		return cliente;
	}

	public List<VentaDetalleJson> getDetalles() {
		return detalles;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setDetalles(List<VentaDetalleJson> detalles) {
		this.detalles = detalles;
	}

	
	public Integer getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public VentaJson(String fecha, Integer montoTotal, String nombreCliente,
			String cliente, List<VentaDetalleJson> detalles) {
		super();
		this.fecha = fecha;
		this.montoTotal = montoTotal;
		this.nombreCliente = nombreCliente;
		this.cliente = cliente;
		this.detalles = detalles;
	}
	
	
}
