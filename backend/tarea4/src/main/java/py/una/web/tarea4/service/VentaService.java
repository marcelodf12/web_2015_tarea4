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

import py.una.web.tarea4.ejb.VentaEjb;

@ManagedBean(name = "ventaService")
@ViewScoped
public class VentaService {
	@Inject
	VentaEjb ventaEjb;

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

			ArrayList<Integer> errores = ventaEjb.cargaMasiva(direccion);
			
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
