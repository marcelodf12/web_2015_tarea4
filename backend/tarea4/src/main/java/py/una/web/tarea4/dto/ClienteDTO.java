package py.una.web.tarea4.dto;

import py.una.web.tarea4.model.Cliente;

public class ClienteDTO {

	private String ruc;
	private Boolean activo;
	private String direccion;
	private String nombre;
	
	public ClienteDTO(){
		
	}
	
	public ClienteDTO(Cliente c){
		this.ruc= c.getRuc();
		this.activo= c.getActivo();
		this.direccion= c.getDireccion();
		this.nombre= c.getNombre();
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
