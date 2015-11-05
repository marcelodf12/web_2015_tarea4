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

import py.una.web.tarea4.model.Compra;
import py.una.web.tarea4.model.CompraDetalle;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Proveedor;
import py.una.web.tarea4.util.CompraDetalleJson;
import py.una.web.tarea4.util.CompraJson;

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
					CompraJson nuevaCompra = gson.fromJson(line, CompraJson.class);
					List<CompraDetalle> detalles = new ArrayList<CompraDetalle>();
					Integer montoTotal = 0;
					Compra compra = new Compra();
					Proveedor proveedor = em.find(Proveedor.class, nuevaCompra.getProveedor());
					compra.setProveedor(proveedor);
					for(CompraDetalleJson d:nuevaCompra.getDetalles()){
						Producto p = productoEjb.findById(d.getProducto());
						p.setStock(p.getStock()+d.getCantidad());
						CompraDetalle nuevoDetalle = new CompraDetalle(d.getCantidad(), d.getPrecio(), p);
						nuevoDetalle.setCompra(compra);
						detalles.add(nuevoDetalle);
						montoTotal+=d.getPrecio()*d.getCantidad();
						em.persist(nuevoDetalle);
					}
					compra.edit(nuevaCompra.getFecha(), montoTotal, detalles, proveedor);
					em.persist(compra);
				} catch (Exception e) {
					e.getMessage();
					errores.add(numeroDeLinea);
					fallo=true;
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

}
