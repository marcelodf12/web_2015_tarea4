package py.una.web.tarea4.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.una.web.tarea4.dto.VentaDTO;
import py.una.web.tarea4.model.Venta;

/**
 * Session Bean implementation class VentaEjb
 */
@Stateless
@LocalBean
public class VentaEjb {

    /**
     * Default constructor. 
     */
    public VentaEjb() {

    }
    
    @PersistenceContext
    private EntityManager em;
	
	@Resource
    private SessionContext context;
	
	public List<VentaDTO> listar(Integer inicio ,Integer cantidad, String orderBy, String orderDir, String campoBusqueda, String busqueda) throws Exception{
        try{
        	String consulta = "SELECT NUMERO, MONTO_TOTAL, NOMBRE_CLIENTE, RUC_CLIENTE, FECHA, ID_FACTURA FROM VENTAS ";
            if (campoBusqueda != null){
                consulta += "WHERE ";
                if (campoBusqueda.compareTo("by_all_attributes") == 0){
                    consulta += "CAST (NUMERO AS TEXT) LIKE '"  + busqueda + "%' OR ";
                    consulta += "CAST (MONTO_TOTAL AS TEXT) LIKE '" + busqueda + "%' OR ";
                    consulta += "NOMBRE_CLIENTE LIKE '" + busqueda + "%' OR ";
                    consulta += "RUC_CLIENTE LIKE '" + busqueda + "%' OR ";
                    consulta += "CAST (FECHA AS TEXT) LIKE '" + busqueda + "%' ";
                }else{
                    consulta += "CAST (" + campoBusqueda  + " AS TEXT) LIKE '" + busqueda + "%' ";  
                }
            }
            if (orderBy != null){
                consulta += " ORDER BY " + orderBy + " " + orderDir + " ";
            }
            Query q= em.createNativeQuery(consulta, Venta.class);
            q.setFirstResult(inicio);
            q.setMaxResults(cantidad);
            @SuppressWarnings("unchecked")
			List<Venta> lista= (List<Venta>) q.getResultList();
            List<VentaDTO> ret= new ArrayList<VentaDTO>();
            for (Venta v: lista){
            	ret.add(new VentaDTO(v));
            }
            return ret;
        }catch(Exception e){
        	context.setRollbackOnly();
        	throw new Exception(e.getMessage());
        }
		
    }
    
    public Integer total(String campoBusqueda, String busqueda) throws Exception{
        try{
        	String consulta = "SELECT * FROM VENTAS ";
            if (campoBusqueda != null){
                consulta += "WHERE ";
                if (campoBusqueda.compareTo("by_all_attributes") == 0){
                    consulta += "CAST (NUMERO AS TEXT) LIKE '"  + busqueda + "%' OR ";
                    consulta += "CAST (MONTO_TOTAL AS TEXT) LIKE '" + busqueda + "%' OR ";
                    consulta += "NOMBRE_CLIENTE LIKE '" + busqueda + "%' OR ";
                    consulta += "RUC_CLIENTE LIKE '" + busqueda + "%' OR ";
                    consulta += "CAST (FECHA AS TEXT) LIKE '" + busqueda + "%' ";
                }else{
                    consulta += "CAST (" + campoBusqueda  + " AS TEXT) LIKE '" + busqueda + "%' ";  
                }
            }
            Query q= em.createNativeQuery(consulta);
            @SuppressWarnings("unchecked")
			List<Venta> aux = q.getResultList();
            return aux.size();
        }catch(Exception e){
        	context.setRollbackOnly();
        	throw new Exception(e.getMessage());
        }
    }

}
