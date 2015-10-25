package py.una.web.tarea4.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the productos database table.
 * 
 */
@Entity
@Table(name="productos")
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private Boolean activo;

	private String nombre;

	private Integer precio;

	private Integer stock;

	//bi-directional many-to-one association to CompraDetalle
	@OneToMany(mappedBy="producto")
	private List<CompraDetalle> compraDetalles;

	//bi-directional many-to-one association to VentaDetalle
	@OneToMany(mappedBy="producto")
	private List<VentaDetalle> ventaDetalles;

	public Producto() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return this.precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<CompraDetalle> getCompraDetalles() {
		return this.compraDetalles;
	}

	public void setCompraDetalles(List<CompraDetalle> compraDetalles) {
		this.compraDetalles = compraDetalles;
	}

	public CompraDetalle addCompraDetalle(CompraDetalle compraDetalle) {
		getCompraDetalles().add(compraDetalle);
		compraDetalle.setProducto(this);

		return compraDetalle;
	}

	public CompraDetalle removeCompraDetalle(CompraDetalle compraDetalle) {
		getCompraDetalles().remove(compraDetalle);
		compraDetalle.setProducto(null);

		return compraDetalle;
	}

	public List<VentaDetalle> getVentaDetalles() {
		return this.ventaDetalles;
	}

	public void setVentaDetalles(List<VentaDetalle> ventaDetalles) {
		this.ventaDetalles = ventaDetalles;
	}

	public VentaDetalle addVentaDetalle(VentaDetalle ventaDetalle) {
		getVentaDetalles().add(ventaDetalle);
		ventaDetalle.setProducto(this);

		return ventaDetalle;
	}

	public VentaDetalle removeVentaDetalle(VentaDetalle ventaDetalle) {
		getVentaDetalles().remove(ventaDetalle);
		ventaDetalle.setProducto(null);

		return ventaDetalle;
	}

}
