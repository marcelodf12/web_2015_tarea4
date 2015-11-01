package py.una.web.tarea4.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the venta_detalle database table.
 * 
 */
@Entity
@Table(name="venta_detalle")
@NamedQuery(name="VentaDetalle.findAll", query="SELECT v FROM VentaDetalle v")
@SequenceGenerator(name="seqVentaDetalle", initialValue=100, allocationSize=1, sequenceName="seqVentaDetalle")
public class VentaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqVentaDetalle")
	private Integer id;

	private Integer cantidad;

	@Column(name="id_venta")
	private Integer idVenta;

	private Integer precio;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="id_producto")
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name="numero")
	private Venta venta;

	public VentaDetalle() {
	}

	public VentaDetalle(Integer cantidad, Integer precio, Producto p) {
		super();
		this.cantidad = cantidad;
		this.precio = precio;
		this.setProducto(producto);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdVenta() {
		return this.idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public Integer getPrecio() {
		return this.precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

}
