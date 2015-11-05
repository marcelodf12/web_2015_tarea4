package py.una.web.tarea4.dto;

import py.una.web.tarea4.model.Venta;
import py.una.web.tarea4.model.VentaDetalle;

public class VentaDetalleDTO {
	
	private Integer id;
	private Integer cantidad;
	private Integer precio;
	private Venta venta;
	private String producto;
	
	public VentaDetalleDTO() {
	}
	public VentaDetalleDTO(VentaDetalle ventaDetalle) {
		this.id = ventaDetalle.getId();
		this.cantidad = ventaDetalle.getCantidad();
		this.precio = ventaDetalle.getPrecio();
		this.venta = ventaDetalle.getVenta();
		this.producto= ventaDetalle.getProducto().getNombre();
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Integer getPrecio() {
		return precio;
	}
	public void setPrecio(Integer precio) {
		this.precio = precio;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	

}
