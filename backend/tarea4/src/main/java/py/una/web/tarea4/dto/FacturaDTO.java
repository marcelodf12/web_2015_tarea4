package py.una.web.tarea4.dto;

import py.una.web.tarea4.model.Factura;

public class FacturaDTO {
	
	private Integer id;
	private Integer idVentaCabecera;
	private Boolean impreso;
	private Integer total;
	
	public FacturaDTO() {
	}
	
	public FacturaDTO(Factura f){
		this.id= f.getId();
		this.idVentaCabecera= f.getIdVentaCabecera();
		this.impreso= f.getImpreso();
		this.total= f.getTotal();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdVentaCabecera() {
		return idVentaCabecera;
	}

	public void setIdVentaCabecera(Integer idVentaCabecera) {
		this.idVentaCabecera = idVentaCabecera;
	}

	public Boolean getImpreso() {
		return impreso;
	}

	public void setImpreso(Boolean impreso) {
		this.impreso = impreso;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
