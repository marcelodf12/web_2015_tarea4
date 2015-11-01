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

import py.una.web.tarea4.dto.VentaDTO;
import py.una.web.tarea4.model.Producto;
import py.una.web.tarea4.model.Cliente;
import py.una.web.tarea4.model.Venta;
import py.una.web.tarea4.model.VentaDetalle;
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
					Cliente cliente = em.find(Cliente.class, nuevaVenta.getCliente());
					venta.setCliente(cliente);
					em.persist(venta);
					for(VentaDetalleJson d:nuevaVenta.getDetalles()){
						Producto p = productoEjb.findById(d.getProducto());
						p.setStock(p.getStock()-d.getCantidad());
						VentaDetalle nuevoDetalle = new VentaDetalle(d.getCantidad(), d.getPrecio(), p);
						nuevoDetalle.setVenta(venta);
						detalles.add(nuevoDetalle);
						montoTotal+=d.getPrecio()*d.getCantidad();
						nuevoDetalle.setProducto(p);
						em.merge(nuevoDetalle);
					}
					venta.edit(nuevaVenta.getFecha(), montoTotal, detalles, cliente);
					venta.setNombreCliente(nuevaVenta.getNombreCliente());
					em.persist(venta);
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
