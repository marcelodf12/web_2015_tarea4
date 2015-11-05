package py.una.web.tarea4.dto;

import java.util.Date;

import py.una.web.tarea4.model.Venta;

public class VentaDTO {

	private Integer numero;
	private Integer montoTotal;
	private String nombreCliente;
	private Date fecha;
	private ClienteDTO cliente;
	
	public VentaDTO(){
	}
	
	public VentaDTO(Venta v){
		this.numero= v.getNumero();
		this.montoTotal= v.getMontoTotal();
		this.nombreCliente= v.getNombreCliente();
		this.fecha= v.getFecha();
		if (v.getCliente()!= null) this.cliente= new ClienteDTO(v.getCliente());
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}


}
