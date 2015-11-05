package py.una.web.tarea4.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import py.una.web.tarea4.ejb.ClienteEjb;
import py.una.web.tarea4.ejb.ProductoEjb;
import py.una.web.tarea4.ejb.VentaEjb;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Venta;
import py.una.web.tarea4.model.VentaDetalle;
import py.una.web.tarea4.util.ListaPaginada;

@ManagedBean(name = "ventaService")
@ViewScoped
public class VentaService {
	@Inject
	VentaEjb ventaEjb;

	@Inject
	ProductoEjb productoEjb;

	@Inject
	ClienteEjb clienteEjb;

	private String rucCliente;

	private Integer nuevoIdProducto;

	private Integer nuevoCantidad;

	private Integer nuevoPrecio;

	private Venta nuevaVenta;

	private Integer paginaActual = 1;

	private Integer cantidadPorPagina = 5;

	private String filtroGeneral;

	private String orden = "ASC";

	private Integer totalPaginas;

	public List<Venta> getVentas() {
		try {
			if (filtroGeneral == null)
				filtroGeneral = "";
			ListaPaginada<Venta> l = ventaEjb.listar(5, paginaActual, "fecha",
					orden, null, filtroGeneral);
			totalPaginas = l.getCantidadDePaginas();
			return l.getLista();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void agregar() {
		System.out.println("Agregar nuevo detalle");
		if (nuevaVenta == null) {
			nuevaVenta = new Venta();
			nuevaVenta.setVentaDetalles(new ArrayList<VentaDetalle>());
		}
		System.out.println(nuevoIdProducto);
		Producto producto = productoEjb.findById(nuevoIdProducto);
		if (producto != null) {
			nuevaVenta.getVentaDetalles().add(
					new VentaDetalle(nuevoCantidad, nuevoPrecio, producto));
			nuevoCantidad = 1;
			nuevoIdProducto = 0;
			nuevoPrecio = 0;
		} else {
			System.out.println("No se encontro el producto");
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg1').show();");
		context.update("form");
	}

	public void remove(VentaDetalle d) {
		nuevaVenta.getVentaDetalles().remove(d);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg1').show();");
		context.update("form");
	}

	public void guardarVenta() {
		if (nuevaVenta != null) {
			if (nuevaVenta.getVentaDetalles().size() > 0) {
				nuevaVenta.setCliente(clienteEjb.findById(rucCliente));
				nuevaVenta.setMontoTotal(0);
				for (VentaDetalle c : nuevaVenta.getVentaDetalles()) {
					nuevaVenta.setMontoTotal(nuevaVenta.getMontoTotal()
							+ c.getCantidad() * c.getPrecio());
				}
				nuevaVenta.setFecha(new Date());
				ventaEjb.nuevaVenta(nuevaVenta);
				nuevaVenta = new Venta();
				nuevaVenta.setVentaDetalles(new ArrayList<VentaDetalle>());
				nuevoCantidad = 1;
				nuevoIdProducto = 0;
				nuevoPrecio = 0;
			}
		}

	}

	public List<VentaDetalle> getDetalles() {
		if (nuevaVenta == null) {
			nuevaVenta = new Venta();
			nuevaVenta.setVentaDetalles(new ArrayList<VentaDetalle>());
		}
		return nuevaVenta.getVentaDetalles();
	}

	public String getRucCliente() {
		return rucCliente;
	}

	public Integer getNuevoIdProducto() {
		return nuevoIdProducto;
	}

	public Integer getNuevoCantidad() {
		return nuevoCantidad;
	}

	public Integer getNuevoPrecio() {
		return nuevoPrecio;
	}

	public Venta getNuevaVenta() {
		return nuevaVenta;
	}

	public void setRucCliente(String rucCliente) {
		this.rucCliente = rucCliente;
	}

	public void setNuevoIdProducto(Integer nuevoIdProducto) {
		this.nuevoIdProducto = nuevoIdProducto;
	}

	public void setNuevoCantidad(Integer nuevoCantidad) {
		this.nuevoCantidad = nuevoCantidad;
	}

	public void setNuevoPrecio(Integer nuevoPrecio) {
		this.nuevoPrecio = nuevoPrecio;
	}

	public void setNuevaVenta(Venta nuevaVenta) {
		this.nuevaVenta = nuevaVenta;
	}

	public void primeraPagina() {
		paginaActual = 1;
	}

	public void ultimaPagina() {
		paginaActual = totalPaginas;
	}

	public void siguientePagina() {
		if (this.getPaginaActual() < this.getTotalPaginas()) {
			paginaActual++;
		}
	}

	public void anteriorPagina() {
		if (getPaginaActual() > 1) {
			paginaActual += -1;
		}
	}

	private UploadedFile file;

	private final String destination = "/home/marcelo/temp/";

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void fileUploadListener(FileUploadEvent event) {

		this.file = event.getFile();

		try {
			copyFile(event.getFile().getFileName(), event.getFile()
					.getInputstream());
		} catch (IOException e) {
		}
	}

	public void copyFile(String fileName, InputStream in) {

		String direccion = destination + fileName;
		try {
			File fichero = new File(direccion);
			if (fichero.delete())
				System.out
						.println("El fichero ha sido borrado satisfactoriamente");
			else
				System.out.println("El fichero no puede ser borrado");
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(direccion));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			System.out.println("Archivo Creado!");

			ArrayList<Integer> errores = ventaEjb.cargaMasiva(direccion);

			String summary;
			String mensaje;
			Severity s;

			if (errores.size() == 0) {
				summary = "Exito";
				mensaje = "Se subio correctamente el archivo";
				s = FacesMessage.SEVERITY_INFO;
			} else {
				mensaje = "Hubo errores en las lineas: ";
				for (Integer i : errores)
					mensaje += i.toString() + ", ";
				summary = "Error";
				s = FacesMessage.SEVERITY_ERROR;
			}

			FacesMessage message = new FacesMessage(s, summary, mensaje);
			FacesContext.getCurrentInstance().addMessage("messages", message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlg3').hide();");
			context.update("form");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public Integer getPaginaActual() {
		return paginaActual;
	}

	public Integer getCantidadPorPagina() {
		return cantidadPorPagina;
	}

	public String getFiltroGeneral() {
		return filtroGeneral;
	}

	public String getOrden() {
		return orden;
	}

	public Integer getTotalPaginas() {
		return totalPaginas;
	}

	public void setPaginaActual(Integer paginaActual) {
		this.paginaActual = paginaActual;
	}

	public void setCantidadPorPagina(Integer cantidadPorPagina) {
		this.cantidadPorPagina = cantidadPorPagina;
	}

	public void setFiltroGeneral(String filtroGeneral) {
		this.filtroGeneral = filtroGeneral;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public void setTotalPaginas(Integer totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	
	

}
