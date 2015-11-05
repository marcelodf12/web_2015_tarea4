package py.una.web.tarea4.service;


import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import py.una.web.tarea4.ejb.FacturaEjb2;

@ManagedBean
@SessionScoped
public class FacturaController {
    
    private boolean corriendo;
    @EJB 
    FacturaEjb2 fm;
    public void setCorriendo (boolean corriendo)
    {
        this.corriendo=corriendo;
    }
    public boolean isCorriendo()
    {
        return this.corriendo;
    }
    
    public void facturar()
    {
        
        if(!isRun()){
        	fm.facturar();
         try {
            
            
            this.corriendo=true;
        } catch (Exception ex) {
          //  Logger.getLogger(FileUploadClientesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    public boolean isRun()
    {
        
    	String s = fm.isRun();
         try {
         
             return "Corriendo".equals(s);
        } catch (Exception ex) {
           // Logger.getLogger(FileUploadClientesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }
    
    public String estado()
    {
        if(isRun()==false)
            return "Detenido";
        else
            return "Corriendo";
    }
    
    public void detener()
    {
        
        if(isRun()){
        	String sd = fm.detener();
        try {
            
            FacesMessage msg = new FacesMessage("Detener", sd);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
            this.corriendo=false;
        } catch (Exception ex) {
           // Logger.getLogger(FileUploadClientesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    
}

