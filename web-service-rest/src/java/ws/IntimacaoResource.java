package ws;

import model.Intimacao;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

@Path("intimacao")
public class IntimacaoResource {    

    @Context
    private UriInfo context;

    public IntimacaoResource() {
    }
    
    private DataService dataService = DataService.getInstance();
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIntimacaoList() {
        
        List<Intimacao> intimacaoList = dataService.getIntimacaoList();
        
        GenericEntity<List<Intimacao>> lista = 
                new GenericEntity<List<Intimacao>>(intimacaoList){};
        
        return Response
                .ok()
                .entity(lista)
                .build();        
    }
    
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Intimacao getIntimacaoById(@PathParam("id") Long id) {
        
        List<Intimacao> lista = dataService.getIntimacaoList();
        
        for (Intimacao i : lista) {
            if (i.getId().equals(id)) {
                return i;
            }	    
        }
	return null;        
    }
    
    @POST
    @Path("/lista")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setIntimacaoList(List<Intimacao> intimacao){        

        // dataService.getIntimacaoList().add(intimacao);        
        
        dataService.setIntimacaoList(intimacao);
    }
    
    @POST
    @Path("/objeto")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setIntimacaoList(Intimacao intimacao){        

        dataService.getIntimacaoList().add(intimacao);        
        
    }
    
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Intimacao setIntimacaoByID(@PathParam("id") Long id, Intimacao novo) {
        
        List<Intimacao> lista = dataService.getIntimacaoList();
        
        for (Intimacao i : lista) {
	        if (i.getId().equals(id)) {
	            
                    i.setCpfIntimado(novo.getCpfIntimado());
                    i.setCpfOficial(novo.getCpfOficial());
                    i.setDataExecucaoIntimacao(novo.getDataExecucaoIntimacao());
                    i.setDataIntimacao(novo.getDataIntimacao());
                    i.setEndereçoIntimado(novo.getEndereçoIntimado());
                    i.setIsEfetivada(novo.isIsEfetivada());
                    i.setNomeIntimado(novo.getNomeIntimado());
                    i.setNomeOficialAlocado(novo.getNomeOficialAlocado());
                    i.setProcesso(novo.getProcesso());
                                        
                    return i;
                    
	        }
        }
        return null;        
    }
        
}
