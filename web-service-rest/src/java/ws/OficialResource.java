package ws;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import model.OficialJustica;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

@Path("oficial")
public class OficialResource {    

    @Context
    private UriInfo context;

    public OficialResource() {
    }
    
    private DataService dataService = DataService.getInstance();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOficialList() {
        
        List<OficialJustica> oficialList = dataService.getOficialList();
        
        GenericEntity<List<OficialJustica>> lista = 
                new GenericEntity<List<OficialJustica>>(oficialList){};
        
        return Response
                .ok()
                .entity(lista)
                .build();        
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OficialJustica getOficialByID(@PathParam("id") Long id) {
        
        List<OficialJustica> lista = dataService.getOficialList();
        
        for (OficialJustica o : lista) {
	        if (o.getId().equals(id)) {
	            return o;
	        }
	    }
	    return null;        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void setOficialList(List<OficialJustica> lista){        
        
        dataService.setOficialList(lista);        
    }
    
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OficialJustica setOficialByID(@PathParam("id") Long id, OficialJustica novo) {
        
        List<OficialJustica> lista = dataService.getOficialList();
        
        for (OficialJustica o : lista) {
	        if (o.getId().equals(id)) {
	            
                    o.setNome(novo.getNome());
                    o.setCPF(novo.getCPF());
                    o.setEmail(novo.getEmail());
                    
                    return o;
                    
	        }
	    }
	    return null;        
    }
    
}
