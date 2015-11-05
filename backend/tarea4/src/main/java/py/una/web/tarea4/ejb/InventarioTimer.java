package py.una.web.tarea4.ejb;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TimerService;


@Singleton
public class InventarioTimer {

	@Resource
	TimerService timerService;
	
	@EJB
	private ProductoEjb productoEjb;
	
	@Schedule(minute="*/3", hour="*")
    public void inventarioAutomatico() throws Exception {
        System.out.println("REALIZANDO INVENTARIO DE PRODUCTOS...");
        productoEjb.inventario();
    }
	
	
	
}
