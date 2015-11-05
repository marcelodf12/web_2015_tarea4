package py.una.web.tarea4.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;



/**
 * The persistent class for the facturas database table.
 * 
 */
@Entity
@Table(name="facturas")
@NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@Column(name="id_venta_cabecera")
	private Integer idVentaCabecera;

	private Boolean impreso;

	private Integer total;
	
	private Date fecha;

	private Venta venta;

	
	
	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Factura() {
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdVentaCabecera() {
		return this.idVentaCabecera;
	}

	public void setIdVentaCabecera(Integer idVentaCabecera) {
		this.idVentaCabecera = idVentaCabecera;
	}

	public Boolean getImpreso() {
		return this.impreso;
	}

	public void setImpreso(Boolean impreso) {
		this.impreso = impreso;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
