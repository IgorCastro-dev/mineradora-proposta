package org.mineradora.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.mineradora.dto.ProposalDetailsDTO;
import org.mineradora.services.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/api/proposal")
public class ProposalController {

    @Inject
    private ProposalService proposalService;

    Logger logger = LoggerFactory.getLogger(ProposalController.class);

    @GET
    @Path("/{id}")
    public ProposalDetailsDTO findDetailsProposal(@PathParam("id") Long id){
        return proposalService.findFullProposal(id);
    }

    @POST
    public Response createProposal(ProposalDetailsDTO proposalDetailsDTO){
        logger.info("createProposal");
        try {
            proposalService.createNewProposal(proposalDetailsDTO);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProposal(@PathParam("id") Long id){
        try {
            proposalService.removeProposal(id);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }
}
