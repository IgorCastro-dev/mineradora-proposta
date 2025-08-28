package org.mineradora.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mineradora.dto.ProposalDTO;
import org.mineradora.dto.ProposalDetailsDTO;
import org.mineradora.entity.ProposalEntity;
import org.mineradora.message.KafkaEvents;
import org.mineradora.repository.ProposalRepository;

import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements  ProposalService {
    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvents kafkaEvents;

    @Override
    public ProposalDetailsDTO findFullProposal(Long id) {
        ProposalEntity proposalEntity = proposalRepository.findById(id);
        return ProposalDetailsDTO.builder()
                .proposalId(proposalEntity.getId())
                .proposalValidityDays(proposalEntity.getProposalValidityDays())
                .country(proposalEntity.getCountry())
                .priceTonne(proposalEntity.getPriceTonne())
                .customer(proposalEntity.getCustomer())
                .tonnes(proposalEntity.getTonnes())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);
        kafkaEvents.sendKafkaEvents(proposal);
    }

    @Override
    public void removeProposal(Long id) {
        proposalRepository.deleteById(id);
    }

    @Transactional
    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        try{
            ProposalEntity proposalEntity = new ProposalEntity();
            proposalEntity.setCreated(new Date());
            proposalEntity.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());
            proposalEntity.setCountry(proposalDetailsDTO.getCountry());
            proposalEntity.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposalEntity.setCustomer(proposalDetailsDTO.getCustomer());
            proposalEntity.setTonnes(proposalDetailsDTO.getTonnes());

            proposalRepository.persist(proposalEntity);

            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposalEntity.getCustomer()).get().getId())
                    .customer(proposalEntity.getCustomer())
                    .priceTonne(proposalEntity.getPriceTonne())
                    .build();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
