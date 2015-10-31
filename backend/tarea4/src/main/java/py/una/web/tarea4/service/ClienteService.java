package py.una.web.tarea4.service;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import py.una.web.tarea4.ejb.ClienteEjb;
import py.una.web.tarea4.model.Cliente;
import py.una.web.tarea4.util.ListaPaginada;

@ManagedBean(name = "clienteService")
@ViewScoped
public class ClienteService {
	
	@Inject
	ClienteEjb clienteEjb;
	
	private Integer paginaActual=1;
	
	private Integer cantidadPorPagina=5;
	
	private String filtroGeneral;
	
	private String orden = "ASC";
	
	private Integer totalPaginas;
	
	private String nuevoNombre;
	
	private String nuevoRuc;
	
	private String nuevaDireccion;
	
	public void limpiar(){
		nuevaDireccion = "";
		nuevoNombre = "";
		nuevoRuc = "";
	}
	
	public void cancelar(){
		this.limpiar();
	}
	
	public void crear(){
		if(nuevoRuc.trim().compareTo("")!=0 && nuevoNombre.trim().compareTo("")!=0){
			if(clienteEjb.persistir(nuevoNombre, nuevoRuc, nuevaDireccion)){
				addMessage("Success", "Se Guardo correctamente Correctamente");
				this.limpiar();
			}else{
				addMessage("Error", "Ya existe un cliente con el RUC " + nuevoRuc);
			}
		}else{
			addMessage("Error", "RUC y Nombre son obligatorios");
		}
	}
	
	public ArrayList<Cliente> getClientes(){
		ListaPaginada<Cliente> l = clienteEjb.listar(cantidadPorPagina, paginaActual, "nombre", orden, null, filtroGeneral);
		totalPaginas = l.getCantidadDePaginas();
		return l.getLista();
	}
	
    public void primeraPagina() {
    	paginaActual=1;
    }
    public void ultimaPagina() {
    	paginaActual=totalPaginas;
    }
    public void siguientePagina() {
    	if (this.getPaginaActual()<this.getTotalPaginas()){
    		paginaActual++;
    	}
    }
    public void anteriorPagina() {
    	if (getPaginaActual()>1){
    		paginaActual+=-1;
    	}
    }
    
    public void edit(String ruc){
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

    public void remove(String ruc){
    	clienteEjb.eliminar(ruc);
    	this.addMessage("Eliminado correctamente", ruc);
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
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
    
    

}
