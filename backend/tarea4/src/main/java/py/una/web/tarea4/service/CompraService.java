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

import py.una.web.tarea4.ejb.CompraEjb;
import py.una.web.tarea4.ejb.ProductoEjb;
import py.una.web.tarea4.ejb.ProveedorEjb;
import py.una.web.tarea4.model.Compra;
import py.una.web.tarea4.model.CompraDetalle;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Proveedor;

@ManagedBean(name = "compraService")
@ViewScoped
public class CompraService {

	@Inject
	CompraEjb compraEjb;

	@Inject
	ProveedorEjb proveedorEjb;

	@Inject
	ProductoEjb productoEjb;

	private String rucProveedor;

	private Integer nuevoIdProducto;

	private Integer nuevoCantidad;

	private Integer nuevoPrecio;

	private Compra nuevaCompra;

	public void agregar() {
		System.out.println("Agregar nuevo detalle");
		if (nuevaCompra == null) {
			nuevaCompra = new Compra();
			nuevaCompra.setCompraDetalles(new ArrayList<CompraDetalle>());
		}
		System.out.println(nuevoIdProducto);
		Producto producto = productoEjb.findById(nuevoIdProducto);
		if (producto != null) {
			nuevaCompra.getCompraDetalles().add(
					new CompraDetalle(nuevoCantidad, nuevoPrecio, producto));
			nuevoCantidad = 1;
			nuevoIdProducto = 0;
			nuevoPrecio = 0;
		} else {
			System.out.println("No se encontro el producto");
		}
	}

	public void guardarCompra() {
		if (nuevaCompra != null) {
			if (nuevaCompra.getCompraDetalles().size() > 0) {
				nuevaCompra.setProveedor(proveedorEjb.findById(rucProveedor));
				nuevaCompra.setMontoTotal(0);
				for(CompraDetalle c: nuevaCompra.getCompraDetalles()){
					nuevaCompra.setMontoTotal(nuevaCompra.getMontoTotal() + c.getCantidad()*c.getPrecio());
				}
				nuevaCompra.setFecha(new Date());
				compraEjb.nuevaCompra(nuevaCompra);
				nuevaCompra = new Compra();
				nuevaCompra.setCompraDetalles(new ArrayList<CompraDetalle>());
				nuevoCantidad = 1;
				nuevoIdProducto = 0;
				nuevoPrecio = 0;
			}
		}

	}

	public List<CompraDetalle> getDetalles() {
		if (nuevaCompra == null) {
			nuevaCompra = new Compra();
			nuevaCompra.setCompraDetalles(new ArrayList<CompraDetalle>());
		}
		return nuevaCompra.getCompraDetalles();
	}

	public String getRucProveedor() {
		return rucProveedor;
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

	public void setRucProveedor(String rucProveedor) {
		this.rucProveedor = rucProveedor;
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

			ArrayList<Integer> errores = compraEjb.cargaMasiva(direccion);

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

}
