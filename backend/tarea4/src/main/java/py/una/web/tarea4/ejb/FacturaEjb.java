package py.una.web.tarea4.ejb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.una.web.tarea4.model.Factura;
import py.una.web.tarea4.model.Venta;
import py.una.web.tarea4.model.VentaDetalle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Singleton
public class FacturaEjb {    
    @EJB 
    VentaEjb vm;
    
    @PersistenceContext
	private EntityManager em;
    
    @Resource
    private SessionContext context;
    
   
     @Resource
    private EJBContext contextBD;
    
    @PersistenceContext(unitName="primary")
    EntityManager entityManager;
   // static ScrollableResults res;
    private Future<String> estadoFacturacion;

    private static FacturaEjb instancia = new FacturaEjb();
    
    private File carpeta;
    @Asynchronous
  //  @Lock(WRITE)
    //@AccessTimeout(-1)
    public Future<String> facturacion() {
  
 
            carpeta = new File("/home/rodrigo/Escritorio/temp/reportes_"+generarNumero());
            carpeta.mkdirs();
            int contador= 0;
            //while(true) {
            List<Venta> ventas = new ArrayList<Venta>();
            ventas = getVentas();
            System.out.println("============ ventas :"+ ventas.size());
            for(Venta venta :  ventas){

                System.out.println("============ venta :"+ venta.getNumero());
                if(context.wasCancelCalled() == false){
                        try {
                            Thread.sleep(SECONDS.toMillis(3));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FacturaEjb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Factura factura = new Factura ();
                        factura.setFecha(new Date());
                        factura.setImpreso(true);
                        factura.setTotal(venta.getMontoTotal());
                        factura.setVenta(venta);
                        
                        entityManager.persist(factura);
                        

                        System.out.println("============ factura total :"+ factura.getTotal());
                        pdf(factura);
                        System.out.println("============ facturado...!");
                        contador++;
                    
                }else{
                    borrarDirectorio(carpeta);
                    carpeta.delete();
                    contextBD.setRollbackOnly();
                    return new AsyncResult<String>("");
                }
            }
            if(contador==0){
            	carpeta.delete();
            }
            instancia.estadoFacturacion =  new AsyncResult<String>("");
            return new AsyncResult<String>("");
    }     
    
    public List<Venta> getVentas(){

    	String consulta = "SELECT v FROM Venta v";
    	Query q = em.createQuery(consulta, Venta.class);
		@SuppressWarnings("unchecked")
		List<Venta> aux = (List<Venta>) q.getResultList();
    	return aux;
    }
    
    public void borrarDirectorio (File directorio)
    {
        System.out.println("BORRANDO");
         File[] ficheros = directorio.listFiles();
          for (int x=0;x<ficheros.length;x++) {
              if (ficheros[x].isDirectory()) {
                    borrarDirectorio(ficheros[x]);
                }
                    ficheros[x].delete();
          }
    }
   

	public void pdf(Factura factura) {
                  
        try
        {       	
        	String fileName= carpeta.getPath()+"/FAC-"+factura.getId().toString();
        	
        	File file = new File(fileName);
            FileOutputStream pdfFileout = new FileOutputStream(file);
            Document document = new Document();
        	
        	PdfWriter.getInstance(document, pdfFileout);
            
        	document.open();
            Paragraph p1 = new Paragraph("Nro Factura : FAC-"+ factura.getId());            
            Paragraph p2 = new Paragraph("Cliente : "+ factura.getVenta().getCliente().getNombre());
            Paragraph p3 = new Paragraph("Fecha : "+ factura.getFecha());
            PdfPTable tab = new PdfPTable(4);
            
            tab.addCell("Cantidad");
            tab.addCell("Producto");
            tab.addCell("Precio");
            tab.addCell("Total");
            List<VentaDetalle> dv = factura.getVenta().getVentaDetalles();
	        for (VentaDetalle detalle : dv)
	        {
	        	tab.addCell(detalle.getCantidad().toString());
	        	tab.addCell(detalle.getProducto().getNombre());
	        	tab.addCell(detalle.getPrecio().toString());
	        	tab.addCell(new Integer(detalle.getCantidad()*detalle.getPrecio()).toString());      	
	        	
	        }

            Paragraph p4 = new Paragraph("Total : "+factura.getTotal().toString());
	        document.add(new Paragraph(""));
	        document.add(p1);
	        document.add(p2);
	        document.add(p3);
            document.add(tab);
            document.add(p4);
            document.close();
        	
          
        }
        catch ( DocumentException ex)
        {
          System.err.println( "Error iReport: " + ex.getMessage() );
        }
        catch(FileNotFoundException ex){
        	System.err.println( "Error iReport: " + ex.getMessage() );
        }
  }
    
   // @Lock(READ)
    public boolean isRun()
    {
        System.out.println(instancia.estadoFacturacion.isDone());
        if(instancia.estadoFacturacion == null)
            return false;
        if(instancia.estadoFacturacion.isCancelled())
            return false;
        return !instancia.estadoFacturacion.isDone();
    }
    
    
    private String generarNumero(){
		Long id=System.currentTimeMillis();
		String s=id.toString();
		return s;
	}
    
   // @Lock(READ)
    public boolean detener()
    {

            System.out.println("LLAMADO A DETENER");
          //  context.setRollbackOnly();
            return instancia.estadoFacturacion.cancel(true);

    }

    @SuppressWarnings("unchecked")
	public List<Factura> findUltima() {
        Query query = entityManager.createNamedQuery("Factura.findUltima");
        return query.getResultList();
    }
}


