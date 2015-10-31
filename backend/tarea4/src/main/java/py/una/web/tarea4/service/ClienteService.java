package py.una.web.tarea4.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
import py.una.web.tarea4.model.Cliente;
import py.una.web.tarea4.util.ListaPaginada;

@ManagedBean(name = "clienteService")
@ViewScoped
public class ClienteService {

	@Inject
	ClienteEjb clienteEjb;

	private Integer paginaActual = 1;

	private Integer cantidadPorPagina = 5;

	private String filtroGeneral;

	private String orden = "ASC";

	private Integer totalPaginas;

	private String nuevoNombre;

	private String nuevoRuc;

	private String nuevaDireccion;

	public void limpiar() {
		nuevaDireccion = "";
		nuevoNombre = "";
		nuevoRuc = "";
	}

	public void cancelar() {
		this.limpiar();
	}

	public void crear() {
		if (nuevoRuc.trim().compareTo("") != 0
				&& nuevoNombre.trim().compareTo("") != 0) {
			if (clienteEjb.persistir(nuevoNombre, nuevoRuc, nuevaDireccion)) {
				addMessage("Success", "Se Guardo correctamente Correctamente");
				this.limpiar();
			} else {
				addMessage("Error", "Ya existe un cliente con el RUC "
						+ nuevoRuc);
			}
		} else {
			addMessage("Error", "RUC y Nombre son obligatorios");
		}
	}

	public ArrayList<Cliente> getClientes() {
		ListaPaginada<Cliente> l = clienteEjb.listar(cantidadPorPagina,
				paginaActual, "nombre", orden, null, filtroGeneral);
		totalPaginas = l.getCantidadDePaginas();
		return l.getLista();
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

	public void edit(String ruc) {
		System.out.println("ENTRO");
		System.out.println(ruc);
		Cliente c = clienteEjb.findById(ruc);
		nuevoRuc = c.getRuc();
		nuevoNombre = c.getNombre();
		nuevaDireccion = c.getDireccion();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg1').show();");
		context.update("form");
	}

	public void remove(String ruc) {
		clienteEjb.eliminar(ruc);
		this.addMessage("Eliminado correctamente", ruc);
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String getFiltroGeneral() {
		return filtroGeneral;
	}

	public Integer getTotalPaginas() {
		return totalPaginas;
	}

	public void setFiltroGeneral(String filtroGeneral) {
		this.filtroGeneral = filtroGeneral;
	}

	public void setTotalPaginas(Integer totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public Integer getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(Integer paginaActual) {
		this.paginaActual = paginaActual;
	}

	public Integer getCantidadPorPagina() {
		return cantidadPorPagina;
	}

	public String getOrden() {
		return orden;
	}

	public void setCantidadPorPagina(Integer cantidadPorPagina) {
		this.cantidadPorPagina = cantidadPorPagina;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getNuevoNombre() {
		return nuevoNombre;
	}

	public String getNuevoRuc() {
		return nuevoRuc;
	}

	public String getNuevaDireccion() {
		return nuevaDireccion;
	}

	public void setNuevoNombre(String nuevoNombre) {
		this.nuevoNombre = nuevoNombre;
	}

	public void setNuevoRuc(String nuevoRuc) {
		this.nuevoRuc = nuevoRuc;
	}

	public void setNuevaDireccion(String nuevaDireccion) {
		this.nuevaDireccion = nuevaDireccion;
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
				   System.out.println("El fichero ha sido borrado satisfactoriamente");
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

			ArrayList<Integer> errores = clienteEjb.cargaMasiva(direccion);
			
			String summary;
			String mensaje;
			Severity s;
			

			if(errores.size() == 0){
				summary = "Exito";
				mensaje = "Se subio correctamente el archivo";
				s=FacesMessage.SEVERITY_INFO;
			}else{
				mensaje = "Hubo errores en las lineas: ";
				for(Integer i:errores)
					mensaje+=i.toString() + ", ";
				summary = "Error";
				s=FacesMessage.SEVERITY_ERROR;
			}
			
			FacesMessage message = new FacesMessage(s,summary,mensaje);
			FacesContext.getCurrentInstance().addMessage("messages", message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlg3').hide();");
			context.update("form");
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
