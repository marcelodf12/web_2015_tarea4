package py.una.web.tarea4.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


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

	//bi-directional many-to-one association to Venta
	@OneToMany(mappedBy="factura")
	private List<Venta> ventas;

	public Factura() {
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

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setFactura(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setFactura(null);

		return venta;
	}

}
