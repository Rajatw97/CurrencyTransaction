package com.learning.currency.controller;

import com.learning.currency.model.*;
import com.learning.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("blockchain")
public class CurrencyController {

    @Autowired
    CurrencyService service;

    @Autowired
    DefaultResponse response;

    @Autowired
    GetChainResponse chainResp;

    @Autowired
    DefaultResponse transResponse;

    @Autowired
    NodeConnection connectNode;

    @Autowired
    ChainResponse chainResponse;


    @Autowired
    Block block;

    @Value("${name}")
    String owner;

    @GetMapping(value = "/mine_block", produces = "application/json")
    public DefaultResponse mineBlock() {

        Block prevBlock = service.getPrevBlock();
        int prevProof = prevBlock.getProof();
        int proof = service.proofOfWork(prevProof);
        service.addTransaction(service.nodeAdress(), owner, 100);
        Block currentBlock = service.createBlock(proof, prevBlock.getCurrentHash());
        block = service.setBlockValues(currentBlock);
        response.setMessage("Congratulations, you just mined a new block!!!");
        response.setBlock(block);
        return response;
    }

    @GetMapping(value = "/get_blockchain", produces = "application/json")
    public GetChainResponse getBlockchain() {
        chainResp.setChain(service.getChain());
        chainResp.setLength(service.getChain().size());
        return chainResp;
    }

    @GetMapping(value = "/is_valid", produces = "application/json")
    public DefaultResponse isChainValid() {
        if (service.isValid(service.getChain()))
            response.setMessage("Blockchain is valid!!!");
        else
            response.setMessage("Blockchain is not valid!!!");
        return response;

    }

    @PostMapping(value = "/add_transaction", produces = "application/json", consumes = "application/json")
    public DefaultResponse addTransaction(@RequestBody Transaction request) {

        if (request.getAmount() == 0 || "".equals(request.getReceiver() ) || "".equals(request.getSender()) ) {
            transResponse.setMessage("Incorrect Input, pls pass all the parameters");
            return transResponse;
        }
        int index = service.addTransaction(request.getSender(), request.getReceiver(), request.getAmount());
        transResponse.setMessage("Transaction will be added to Block " + index);
        return transResponse;
    }

    @PostMapping(value = "/connect_node", produces = "application/json", consumes = "application/json")
    public NodeConnection connectNode(@RequestBody Nodes request) throws MalformedURLException {

        List<String> nodes=request.getNode();
        for (String node :nodes){
            service.addNode(node);
        }

        connectNode.setMessage("All the nodes are now connected."+owner+" Blockchain now contains the following nodes:");
        connectNode.setTotalNodes(nodes);
        return  connectNode;
    }

    @GetMapping(value = "/replace_chain", produces = "application/json")
    public ChainResponse replaceChain()  {

        boolean isChainReplaced = service.replaceChain();
        if(isChainReplaced)
            chainResponse.setMessage("The nodes had different chains so the chain was replaced by the longest one");
        else
            chainResponse.setMessage("All good. The chain is the largest one");
        chainResponse.setChain(service.getChain());
        return  chainResponse;
    }


}
