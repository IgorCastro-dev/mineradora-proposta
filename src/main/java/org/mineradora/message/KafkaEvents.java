package org.mineradora.message;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mineradora.dto.ProposalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaEvents {

    private Logger logger = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("proposal")
    Emitter<ProposalDTO> proposalRequestEmitter;

    public void sendKafkaEvents(ProposalDTO proposalDTO) {
        logger.info("Sending new proposal to Kafka");
        proposalRequestEmitter.send(proposalDTO).toCompletableFuture().join();
    }
}
