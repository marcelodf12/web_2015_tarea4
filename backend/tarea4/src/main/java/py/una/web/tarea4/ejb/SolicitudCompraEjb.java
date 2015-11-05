package py.una.web.tarea4.ejb;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.una.web.tarea4.model.SolicitudCompra;


@Stateless
@LocalBean
public class SolicitudCompraEjb {

	public SolicitudCompraEjb(){
		
	}
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
    private SessionContext context;
	
	public List<SolicitudCompra> listar() throws Exception{
		try{
			Query q= em.createNativeQuery("SELECT * FROM SOLICITUDCOMPRAS", SolicitudCompra.class);
			return q.getResultList();
		}catch(Exception e){
			context.setRollbackOnly();
			throw e;
		}
	}
	
}
