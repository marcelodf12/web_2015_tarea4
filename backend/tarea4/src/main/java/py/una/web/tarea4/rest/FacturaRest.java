package py.una.web.tarea4.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import py.una.web.tarea4.ejb.FacturaEjb2;

@Path("factura")
public class FacturaRest {
    
    
    @EJB 
    FacturaEjb2 fm;
    
    
    @GET
    @Path("facturar")
    @Produces(MediaType.APPLICATION_JSON)
    public String facturar()
    {
       
        return fm.facturar();
                 
            
    }
    
    @GET
    @Path("isRun")
    @Produces(MediaType.APPLICATION_JSON)
    public String isRun()
    {
        return fm.isRun();
        
    }
    
    
    @GET
    @Path("detener")
    @Produces(MediaType.APPLICATION_JSON)
    public String detener()
    {
        return fm.detener();
    }
            
            
}
