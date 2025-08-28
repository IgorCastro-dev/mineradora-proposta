package org.mineradora.services;

import org.mineradora.dto.ProposalDTO;
import org.mineradora.dto.ProposalDetailsDTO;

public interface ProposalService {

    ProposalDetailsDTO findFullProposal(Long id);
    void createNewProposal(ProposalDetailsDTO proposalDetailsDTO);
    void removeProposal(Long id);
}
