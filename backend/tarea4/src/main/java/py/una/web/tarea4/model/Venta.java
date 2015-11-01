package py.una.web.tarea4.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ventas database table.
 * 
 */
@Entity
@Table(name="ventas")
@NamedQuery(name="Venta.findAll", query="SELECT v FROM Venta v")
@SequenceGenerator(name="seqVenta", initialValue=100, allocationSize=1, sequenceName="seqVenta")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="monto_total")
	private Integer montoTotal;

	@Column(name="nombre_cliente")
	private String nombreCliente;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqVenta")
	private Integer numero;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="ruc_cliente", referencedColumnName="ruc")
	private Cliente cliente;

	//bi-directional many-to-one association to Factura
	@ManyToOne
	@JoinColumn(name="id_factura")
	private Factura factura;
	
	@OneToMany(mappedBy="venta", cascade=CascadeType.PERSIST)
	private List<VentaDetalle> ventaDetalles;

	public Venta() {
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getMontoTotal() {
		return this.montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getNombreCliente() {
		return this.nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public List<VentaDetalle> getVentaDetalles() {
		return ventaDetalles;
	}

	public void setVentaDetalles(List<VentaDetalle> ventaDetalles) {
		this.ventaDetalles = ventaDetalles;
	}

	public void edit(String fecha, Integer montoTotal,
			List<VentaDetalle> detalles, Cliente cliente) throws ParseException {
		this.montoTotal = montoTotal;
		this.cliente = cliente;
		this.setVentaDetalles(ventaDetalles);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.fecha = sdf.parse(fecha);
		
		
	}

}
