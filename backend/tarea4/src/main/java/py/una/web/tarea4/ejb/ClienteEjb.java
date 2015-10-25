package py.una.web.tarea4.ejb;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.una.web.tarea4.model.Cliente;

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
			//en ves de persist
			em.merge(c);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}
