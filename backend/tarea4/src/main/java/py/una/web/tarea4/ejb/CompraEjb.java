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

import py.una.web.tarea4.model.Compra;
import py.una.web.tarea4.model.CompraDetalle;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Proveedor;
import py.una.web.tarea4.util.CompraDetalleJson;
import py.una.web.tarea4.util.CompraJson;
import py.una.web.tarea4.util.ListaPaginada;

import com.google.gson.Gson;

/**
 * Session Bean implementation class VentaEjb
 */
@Stateless
@LocalBean
public class CompraEjb {

	/**
	 * Default constructor.
	 */
	public CompraEjb() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	@Inject
	private ProductoEjb productoEjb;

	public Boolean nuevaCompra(Compra c) {
		try {
			for (CompraDetalle d : c.getCompraDetalles()) {
				Producto p = productoEjb.findById(d.getProducto().getId());
				p.setStock(d.getProducto().getStock() + d.getCantidad());
				d.setProducto(p);
				em.persist(d);
			}
			em.persist(c);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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
					CompraJson nuevaCompra = gson.fromJson(line,
							CompraJson.class);
					List<CompraDetalle> detalles = new ArrayList<CompraDetalle>();
					Integer montoTotal = 0;
					Compra compra = new Compra();
					Proveedor proveedor = em.find(Proveedor.class,
							nuevaCompra.getProveedor());
					compra.setProveedor(proveedor);
					for (CompraDetalleJson d : nuevaCompra.getDetalles()) {
						Producto p = productoEjb.findById(d.getProducto());
						p.setStock(p.getStock() + d.getCantidad());
						CompraDetalle nuevoDetalle = new CompraDetalle(
								d.getCantidad(), d.getPrecio(), p);
						nuevoDetalle.setCompra(compra);
						detalles.add(nuevoDetalle);
						montoTotal += d.getPrecio() * d.getCantidad();
						em.persist(nuevoDetalle);
					}
					compra.edit(nuevaCompra.getFecha(), montoTotal, detalles,
							proveedor);
					em.persist(compra);
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

	@SuppressWarnings("unchecked")
	public ListaPaginada<Compra> listar(Integer cantidadPorPagina,
			Integer pagina,	String orderBy, String orderDir, String campoBusqueda,
			String busqueda) throws Exception {
		try {
			String consulta = "";
			String select = "SELECT id, fecha, monto_total, ruc_proveedor FROM compras ";
			String contar = "SELECT id, fecha, monto_total, ruc_proveedor FROM compras ";
			if (campoBusqueda != null) {
				consulta += "WHERE ";
				if (campoBusqueda.compareTo("by_all_attributes") == 0) {
					consulta += "CAST (id AS TEXT) LIKE '" + busqueda
							+ "%' OR ";
					consulta += "CAST (monto_total AS TEXT) LIKE '" + busqueda
							+ "%' OR ";
					consulta += "ruc_proveedor LIKE '" + busqueda + "%' OR ";
					consulta += "CAST (fecha AS TEXT) LIKE '" + busqueda
							+ "%' ";
				} else {
					consulta += "CAST (" + campoBusqueda + " AS TEXT) LIKE '"
							+ busqueda + "%' ";
				}
			}
			if (orderBy != null) {
				consulta += " ORDER BY " + orderBy + " " + orderDir + " ";
			}
			Query q = em.createNativeQuery(select + consulta, Compra.class);
			Query q2 = em.createNativeQuery(contar + consulta, Compra.class);
			
			q.setFirstResult((pagina - 1) * cantidadPorPagina);
			q.setMaxResults(cantidadPorPagina);

			ListaPaginada<Compra> respuesta = (new ListaPaginada<Compra>());
			respuesta.setLista((ArrayList<Compra>) q.getResultList());
			respuesta.setPaginaActual(pagina);

			Long totalResultados = new Long((int) q2.getResultList().size());
			Double div = ((double) totalResultados / (double) cantidadPorPagina);
			Integer cantPag = div.intValue();
			if (totalResultados % cantidadPorPagina != 0)
				cantPag++;

			respuesta.setCantidadDePaginas(cantPag);

			return respuesta;
		} catch (Exception e) {
			context.setRollbackOnly();
			throw new Exception(e.getMessage());
		}
	}
}
