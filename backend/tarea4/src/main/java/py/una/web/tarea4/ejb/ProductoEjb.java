package py.una.web.tarea4.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.SolicitudCompra;
import py.una.web.tarea4.util.ListaPaginada;

@Stateless
@LocalBean
public class ProductoEjb {

	/**
	 * Default constructor.
	 */
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public ProductoEjb() {
		// TODO Auto-generated constructor stub
	}

	public Boolean persistir(String nombre, Integer precio) {
		try {
			Producto c = new Producto(nombre, precio); 
			// en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public Boolean modificar(Integer id, String nombre, Integer precio) {
		try {
			Producto c = findById(id);
			c.setNombre(nombre);
			c.setPrecio(precio);
			// en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ListaPaginada<Producto> listar(Integer cantidadPorPagina,
			Integer pagina, String orderCol, String orderDir, String campo,
			String filtro) {
		String query = "SELECT c FROM Producto c ";
		String contar = "SELECT COUNT(c.id) FROM Producto c ";
		if (filtro != null && campo != null) {
			query += " WHERE " + campo + " LIKE '%" + filtro + "%' AND activo = true";
			contar += " WHERE " + campo + " LIKE '%" + filtro + "%' AND activo = true";
		} else if (filtro != null) {
			if (filtro.compareTo("") != 0) {
				//String[] campos = { "id", "stock" };
				query += " WHERE ((nombre LIKE '%" + filtro + "%')";
				contar += " WHERE ((nombre LIKE '%" + filtro + "%')";
				/*
				for (String c : campos) {
					query += " OR (" + c + " LIKE '%" + filtro + "%')";
					contar += " OR (" + c + " LIKE '%" + filtro + "%')";
				}*/
				query += ") AND activo = true";
				contar += ") AND activo = true";
			}else{
				query += " WHERE activo = true";
				contar += " WHERE activo = true";
			}
		}
		
		System.out.println(query);
		
		if (orderDir != null && orderCol != null) {
			query += " ORDER BY c." + orderCol + " " + orderDir;
		}

		Query q = em.createQuery(query, Producto.class);
		Query q2 = em.createQuery(contar, Long.class);

		q.setFirstResult((pagina - 1) * cantidadPorPagina);
		q.setMaxResults(cantidadPorPagina);

		ListaPaginada<Producto> respuesta = (new ListaPaginada<Producto>());
		respuesta.setLista((ArrayList<Producto>) q.getResultList());
		respuesta.setPaginaActual(pagina);

		Long totalResultados = (Long) q2.getSingleResult();
		Double div = ((double) totalResultados / (double) cantidadPorPagina);
		Integer cantPag = div.intValue();
		if (totalResultados % cantidadPorPagina != 0)
			cantPag++;

		respuesta.setCantidadDePaginas(cantPag);

		return respuesta;
	};

	public void eliminar(Integer id){
		try {
			Producto c = em.find(Producto.class, id);
			c.setActivo(false);
			em.merge(c);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Producto findById(Integer id){
		try {
			return em.find(Producto.class, id);
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void inventario() throws Exception{
    	try{
    		Query q= em.createNativeQuery("SELECT * FROM PRODUCTOS WHERE STOCK<10", Producto.class);
            List<Producto> lista= q.getResultList();
            for (Producto p: lista){
            	SolicitudCompra s= new SolicitudCompra();
            	s.setNombre(p.getNombre());
            	s.setFecha(new Date());	
            	if (em.find(SolicitudCompra.class, s.getNombre())== null){
            		em.persist(s);
            	}else{
            		em.merge(s);
            	}
            }
    	}catch(Exception e){
    		context.setRollbackOnly();
    		throw e;
    	}
    }
}
