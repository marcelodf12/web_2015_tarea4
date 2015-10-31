package py.una.web.tarea4.ejb;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.una.web.tarea4.model.Cliente;
import py.una.web.tarea4.util.ListaPaginada;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Session Bean implementation class ClienteEjb
 */
@Stateless
@LocalBean
public class ClienteEjb implements ClienteEjbLocal {

	/**
	 * Default constructor.
	 */
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public ClienteEjb() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Integer> cargaMasiva(String file) throws IOException {
		Boolean fallo = false;
		ArrayList<Integer> errores = new ArrayList<Integer>();
		BufferedReader br = null;
		String line = "";
		String splitBy = ";";
		Boolean formato;
		Integer numeroDeLinea = 0;
		try {

			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				numeroDeLinea++;
				String[] element = line.split(splitBy);
				Cliente clienteNuevo = new Cliente();
				System.out.println(line);
				System.out.print(element[0]);
				System.out.print("--");
				System.out.print(element[1]);
				System.out.print("--");
				System.out.println(element[2]);
				try {
					formato = true;
					if (element[0].compareTo("") != 0
							&& element[1].compareTo("") != 0
							&& element[2].compareTo("") != 0) {
						clienteNuevo.setNombre(element[0]);
						clienteNuevo.setRuc(element[1]);
						clienteNuevo.setDireccion(element[2]);
						clienteNuevo.setActivo(true);
					} else {
						System.out.println("Fallo formato");
						formato = false;
					}
				} catch (Exception e) {
					System.out.println("Salto excepcion por campos vacios");
					formato = false;
				}
				if (formato) {
					System.out.println("Intento Persistir");
					if (!this.persistir(clienteNuevo)) {
						fallo = true;
						errores.add(numeroDeLinea);
					}
				} else {
					fallo = true;
					errores.add(numeroDeLinea);
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

	private Boolean persistir(Cliente c) {
		try {
			// en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public Boolean persistir(String nombre, String ruc, String direccion) {
		try {
			Cliente c = new Cliente(nombre,ruc,direccion); 
			// en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ListaPaginada<Cliente> listar(Integer cantidadPorPagina,
			Integer pagina, String orderCol, String orderDir, String campo,
			String filtro) {
		String query = "SELECT c FROM Cliente c ";
		String contar = "SELECT COUNT(c.id) FROM Cliente c ";
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
				query += " WHERE activo = true";
				contar += " WHERE activo = true";
			}
		}
		
		System.out.println(query);
		
		if (orderDir != null && orderCol != null) {
			query += " ORDER BY c." + orderCol + " " + orderDir;
		}

		Query q = em.createQuery(query, Cliente.class);
		Query q2 = em.createQuery(contar, Long.class);

		q.setFirstResult((pagina - 1) * cantidadPorPagina);
		q.setMaxResults(cantidadPorPagina);

		ListaPaginada<Cliente> respuesta = (new ListaPaginada<Cliente>());
		respuesta.setLista((ArrayList<Cliente>) q.getResultList());
		respuesta.setPaginaActual(pagina);

		Long totalResultados = (Long) q2.getSingleResult();
		Double div = ((double) totalResultados / (double) cantidadPorPagina);
		Integer cantPag = div.intValue();
		if (totalResultados % cantidadPorPagina != 0)
			cantPag++;

		respuesta.setCantidadDePaginas(cantPag);

		return respuesta;
	};

	public void eliminar(String ruc){
		try {
			Cliente c = em.find(Cliente.class, ruc);
			c.setActivo(false);
			em.merge(c);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Cliente findById(String ruc){
		try {
			return em.find(Cliente.class, ruc);
		} catch (Exception e) {
			return null;
		}
	}
}
