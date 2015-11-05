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

import py.una.web.tarea4.model.Proveedor;
import py.una.web.tarea4.util.ListaPaginada;

@Stateless
@LocalBean
public class ProveedorEjb {
	
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public Boolean persistir(String nombre, String ruc, String direccion) {
		try {
			Proveedor c = new Proveedor(nombre,ruc,direccion); 
			// en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ListaPaginada<Proveedor> listar(Integer cantidadPorPagina,
			Integer pagina, String orderCol, String orderDir, String campo,
			String filtro) {
		String query = "SELECT c FROM Proveedor c ";
		String contar = "SELECT COUNT(c.id) FROM Proveedor c ";
		if (filtro != null && campo != null) {
			query += " WHERE " + campo + " LIKE '%" + filtro + "%' AND activo = true";
			contar += " WHERE " + campo + " LIKE '%" + filtro + "%' AND activo = true";
		} else if (filtro != null) {
			if (filtro.compareTo("") != 0) {
				String[] campos = { "ruc", "direccion" };
				query += " WHERE ((nombre LIKE '%" + filtro + "%')";
				contar += " WHERE ((nombre LIKE '%" + filtro + "%')";
				for (String c : campos) {
					query += " OR (" + c + " LIKE '%" + filtro + "%')";
					contar += " OR (" + c + " LIKE '%" + filtro + "%')";
				}
				query += ") AND activo = true";
				contar += ") AND activo = true";
			}else{
				query += "WHERE activo = true";
				contar += "WHERE activo = true";
			}
		}
		if (orderDir != null && orderCol != null) {
			query += " ORDER BY c." + orderCol + " " + orderDir;
		}

		Query q = em.createQuery(query, Proveedor.class);
		Query q2 = em.createQuery(contar, Long.class);

		q.setFirstResult((pagina - 1) * cantidadPorPagina);
		q.setMaxResults(cantidadPorPagina);

		ListaPaginada<Proveedor> respuesta = (new ListaPaginada<Proveedor>());
		respuesta.setLista((ArrayList<Proveedor>) q.getResultList());
		respuesta.setPaginaActual(pagina);

		Long totalResultados = (Long) q2.getSingleResult();
		Double div = ((double) totalResultados / (double) cantidadPorPagina);
		Integer cantPag = div.intValue();
		if (totalResultados % cantidadPorPagina != 0)
			cantPag++;

		respuesta.setCantidadDePaginas(cantPag);

		return respuesta;
	};
	
	public List<Proveedor> listarTodos(){
		ListaPaginada<Proveedor> l = listar(1000, 1, "nombre", "ASC", null, null);
		return l.getLista();
	}

	public void eliminar(String ruc){
		try {
			Proveedor c = em.find(Proveedor.class, ruc);
			c.setActivo(false);
			em.merge(c);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Proveedor findById(String ruc){
		try {
			return em.find(Proveedor.class, ruc);
		} catch (Exception e) {
			return null;
		}
	}
}
