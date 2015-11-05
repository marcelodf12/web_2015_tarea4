package py.una.web.tarea4.ejb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.gson.Gson;

import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Cliente;
import py.una.web.tarea4.model.Venta;
import py.una.web.tarea4.model.VentaDetalle;
import py.una.web.tarea4.util.ListaPaginada;
import py.una.web.tarea4.util.VentaDetalleJson;
import py.una.web.tarea4.util.VentaJson;

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

	@Inject
	private ProductoEjb productoEjb;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public ListaPaginada<Venta> listar(Integer cantidadPorPagina,
			Integer pagina, String orderCol, String orderDir, String campo,
			String filtro) {
		if (campo != null)
			campo = "c." + campo + ".toString()";
		String query = "SELECT c FROM Venta c ";
		String contar = "SELECT COUNT(c.id) FROM Venta c ";
		if (filtro != null && campo != null) {
			query += " WHERE " + campo + " LIKE '%" + filtro + "%'";
			contar += " WHERE " + campo + " LIKE '%" + filtro + "%'";
		} else if (filtro != null) {
			if (filtro.compareTo("") != 0) {
				String[] campos = { "c.montoTotal" };
				query += " WHERE ((c.id = " + filtro + ")";
				contar += " WHERE ((c.id = " + filtro + ")";
				for (String c : campos) {
					query += " OR (" + c + " = " + filtro + ")";
					contar += " OR (" + c + " = " + filtro + ")";
				}
				query += ") ";
				contar += ") ";
			}
		}

		System.out.println(query);

		if (orderDir != null && orderCol != null) {
			query += " ORDER BY c." + orderCol + " " + orderDir;
		}

		Query q = em.createQuery(query, Venta.class);
		Query q2 = em.createQuery(contar, Long.class);

		q.setFirstResult((pagina - 1) * cantidadPorPagina);
		q.setMaxResults(cantidadPorPagina);

		ListaPaginada<Venta> respuesta = (new ListaPaginada<Venta>());
		respuesta.setLista((ArrayList<Venta>) q.getResultList());
		respuesta.setPaginaActual(pagina);

		Long totalResultados = (Long) q2.getSingleResult();
		Double div = ((double) totalResultados / (double) cantidadPorPagina);
		Integer cantPag = div.intValue();
		if (totalResultados % cantidadPorPagina != 0)
			cantPag++;

		respuesta.setCantidadDePaginas(cantPag);

		return respuesta;
	}

	public Integer total(String campoBusqueda, String busqueda)
			throws Exception {
		try {
			String consulta = "SELECT * FROM VENTAS ";
			if (campoBusqueda != null) {
				consulta += "WHERE ";
				if (campoBusqueda.compareTo("by_all_attributes") == 0) {
					consulta += "CAST (NUMERO AS TEXT) LIKE '" + busqueda
							+ "%' OR ";
					consulta += "CAST (MONTO_TOTAL AS TEXT) LIKE '" + busqueda
							+ "%' OR ";
					consulta += "NOMBRE_CLIENTE LIKE '" + busqueda + "%' OR ";
					consulta += "RUC_CLIENTE LIKE '" + busqueda + "%' OR ";
					consulta += "CAST (FECHA AS TEXT) LIKE '" + busqueda
							+ "%' ";
				} else {
					consulta += "CAST (" + campoBusqueda + " AS TEXT) LIKE '"
							+ busqueda + "%' ";
				}
			}
			Query q = em.createNativeQuery(consulta);
			@SuppressWarnings("unchecked")
			List<Venta> aux = q.getResultList();
			return aux.size();
		} catch (Exception e) {
			context.setRollbackOnly();
			throw new Exception(e.getMessage());
		}
	}

	public ArrayList<Integer> cargaMasiva(String file) throws IOException {
		Boolean fallo = false;
		ArrayList<Integer> errores = new ArrayList<Integer>();
		BufferedReader br = null;
		String line = "";
		Integer numeroDeLinea = 0;
		Gson gson = new Gson();
		try {

			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				numeroDeLinea++;
				try {
					VentaJson nuevaVenta = gson.fromJson(line, VentaJson.class);
					List<VentaDetalle> detalles = new ArrayList<VentaDetalle>();
					Integer montoTotal = 0;
					Venta venta = new Venta();
					Cliente cliente = em.find(Cliente.class,
							nuevaVenta.getCliente());
					venta.setCliente(cliente);
					em.persist(venta);
					for (VentaDetalleJson d : nuevaVenta.getDetalles()) {
						Producto p = productoEjb.findById(d.getProducto());
						p.setStock(p.getStock() - d.getCantidad());
						VentaDetalle nuevoDetalle = new VentaDetalle(
								d.getCantidad(), d.getPrecio(), p);
						nuevoDetalle.setVenta(venta);
						detalles.add(nuevoDetalle);
						montoTotal += d.getPrecio() * d.getCantidad();
						nuevoDetalle.setProducto(p);
						em.merge(nuevoDetalle);
					}
					venta.edit(nuevaVenta.getFecha(), montoTotal, detalles,
							cliente);
					venta.setNombreCliente(nuevaVenta.getNombreCliente());
					em.persist(venta);
				} catch (Exception e) {
					e.getMessage();
					errores.add(numeroDeLinea);
					fallo = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (fallo) {
			context.setRollbackOnly();
		}
		return errores;
	}

	public void nuevaVenta(Venta nuevaVenta) {
		try {
			for (VentaDetalle d : nuevaVenta.getVentaDetalles()) {
				Producto p = productoEjb.findById(d.getProducto().getId());
				p.setStock(d.getProducto().getStock() - d.getCantidad());
				d.setProducto(p);
				em.persist(d);
			}
			em.persist(nuevaVenta);
		} catch (Exception e) {
			context.setRollbackOnly();
			e.printStackTrace();
		}

	}
}
