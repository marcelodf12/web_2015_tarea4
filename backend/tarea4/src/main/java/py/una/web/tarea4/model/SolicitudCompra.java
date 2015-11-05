package py.una.web.tarea4.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="solicitudcompras")
@NamedQuery(name="SolicitudCompra.findAll", query="SELECT s FROM SolicitudCompra s")
public class SolicitudCompra {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	public SolicitudCompra() {
	}
	
	@Id
	@Column(name="nombre_producto")
	private String nombre;
	
	private Date fecha;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
