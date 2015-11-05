package py.una.web.tarea4.ejb;

import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class FacturaEjb2 {
    
    @SuppressWarnings("rawtypes")
	private static Future estadoFacturacion;
   
   @EJB 
   FacturaEjb fm;

   public String facturar()
   {
      
       estadoFacturacion = fm.facturacion();
       return "Facturando";
                
           
   }

   public String isRun()
   {
       if(estadoFacturacion==null)
           return "Detenido";
       if(estadoFacturacion==null || estadoFacturacion.isDone() || estadoFacturacion.isCancelled())
           return "Detenido";
       else
           return "Corriendo";
       
   }
   

   public String detener()
   {
       if(estadoFacturacion!=null){

           estadoFacturacion.cancel(true);
       return "Detenido";}

           return "No se puede detener";
   }
   


}
