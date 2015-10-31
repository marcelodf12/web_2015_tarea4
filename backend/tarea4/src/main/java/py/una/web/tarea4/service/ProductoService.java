package py.una.web.tarea4.service;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import py.una.web.tarea4.ejb.ProductoEjb;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.util.ListaPaginada;

@ManagedBean(name = "productoService")
@ViewScoped
public class ProductoService {
	
	@Inject
	ProductoEjb productoEjb;
	
	private Integer paginaActual=1;
	
	private Integer cantidadPorPagina=5;
	
	private String filtroGeneral;
	
	private String orden = "ASC";
	
	private Integer totalPaginas;
	
	private String nuevoNombre;
	
	private Integer nuevoPrecio;
	
	private String viejoNombre;
	
	private Integer viejoPrecio;
	
	private Integer viejoId;
		
	public void limpiar(){
		viejoId = null;
		nuevoNombre = "";
		nuevoPrecio = 0;
		viejoNombre = "";
		viejoPrecio = 0;
	}
	
	public void cancelar(){
		this.limpiar();
	}
	
	public void crear(){
		if(nuevoPrecio > 0 && nuevoNombre.trim().compareTo("")!=0){
			if(productoEjb.persistir(nuevoNombre, nuevoPrecio)){
				addMessage("Success", "Se Guardo correctamente Correctamente");
				this.limpiar();
			}else{
				addMessage("Error", "Ya existe un producto con el RUC " + viejoId);
			}
		}else{
			addMessage("Error", "Nombre es obligatorio, y el precio debe ser mayor que cero");
		}
	}
	
	public void modificar(){
		if(viejoPrecio > 0 && viejoNombre.trim().compareTo("")!=0){
			if(productoEjb.modificar(viejoId, viejoNombre, viejoPrecio)){
				addMessage("Success", "Se Guardo correctamente Correctamente");
				this.limpiar();
			}else{
				addMessage("Error", "Error inesperado " + viejoId.toString());
			}
		}else{
			addMessage("Error", "Nombre es obligatorio, y el precio debe ser mayor que cero");
		}
	}
	
	public ArrayList<Producto> getProductos(){
		ListaPaginada<Producto> l = productoEjb.listar(cantidadPorPagina, paginaActual, "nombre", orden, null, filtroGeneral);
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
    
    public void edit(Integer id){
    	Producto c = productoEjb.findById(id);
    	viejoPrecio = c.getPrecio();
    	viejoNombre = c.getNombre();
    	viejoId = c.getId();
    	RequestContext context = RequestContext.getCurrentInstance();
    	context.execute("PF('dlg2').show();");
    	context.update("form");
    }

    public void remove(Integer id){
    	productoEjb.eliminar(id);
    	this.addMessage("Eliminado correctamente", id.toString());
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
	
	public void setNuevoNombre(String nuevoNombre) {
		this.nuevoNombre = nuevoNombre;
	}

	public Integer getNuevoPrecio() {
		return nuevoPrecio;
	}

	public void setNuevoPrecio(Integer nuevoPrecio) {
		this.nuevoPrecio = nuevoPrecio;
	}

	public Integer getNuevoId() {
		return viejoId;
	}

	public void setNuevoId(Integer nuevoId) {
		this.viejoId = nuevoId;
	}

	public String getViejoNombre() {
		return viejoNombre;
	}

	public Integer getViejoPrecio() {
		return viejoPrecio;
	}

	public Integer getViejoId() {
		return viejoId;
	}

	public void setViejoNombre(String viejoNombre) {
		this.viejoNombre = viejoNombre;
	}

	public void setViejoPrecio(Integer viejoPrecio) {
		this.viejoPrecio = viejoPrecio;
	}

	public void setViejoId(Integer viejoId) {
		this.viejoId = viejoId;
	}

	
	

    


}
