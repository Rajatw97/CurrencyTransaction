package com.learning.currency.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.currency.model.Block;
import com.learning.currency.model.GetChainResponse;
import com.learning.currency.model.Transaction;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CurrencyService {
    List<Block> chain;
    List<Transaction> transactions;
    int proof;
    String prevoiusHash;
    Set<String> nodes;

    @Autowired
    Block block;

    @Autowired
    Environment environment;

    public CurrencyService(){
        this.chain=new ArrayList<>();
        this.transactions=new ArrayList<>();
        this.createBlock(1,"0");
        this.nodes=new HashSet<>();
    }

    public Block createBlock(int proof, String previousHash){
        Block newBlock=new Block();
        newBlock.setIndex(chain.size()+1);
        newBlock.setProof(proof);
        newBlock.setPreviousHash(previousHash);
        newBlock.setTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis())));
        newBlock.setCurrentHash(hashBlock(newBlock));
        newBlock.setTransactions(transactions);
        chain.add(newBlock);
        transactions=new ArrayList<Transaction>();

        return newBlock;
    }

    public List<Block> getChain(){
        return chain;
    }
    public Block getPrevBlock(){
        return chain.get(chain.size()-1);
    }
    public int proofOfWork(int prevoiusProof) {
        int newproof = 1;
        boolean checkproof = false;
        while (!checkproof) {
            String hashOperation = DigestUtils.sha256Hex(String.valueOf(newproof - prevoiusProof));
            if (hashOperation.substring(0, 4).equals("0000"))
                checkproof = true;
            else
                newproof += 1;

        }
        return newproof;
    }

    public String hashBlock(Block block){
        return DigestUtils.sha256Hex(String.valueOf(block));
    }


    public Block setBlockValues(Block currentBlock){
        block.setIndex(currentBlock.getIndex());
        block.setTimestamp(currentBlock.getTimestamp());
        block.setPreviousHash(currentBlock.getPreviousHash());
        block.setProof(currentBlock.getProof());
        block.setCurrentHash(currentBlock.getCurrentHash());
        block.setTransactions(currentBlock.getTransactions());
        return block;
    }

    public  boolean isValid(List<Block> chain){
        int index=chain.size()-1;
        while(index>1){
            if(!chain.get(index).getPreviousHash().equalsIgnoreCase(chain.get(index-1).getCurrentHash()))
                return false;
            else
                index--;
        }
        return true;

    }
    public int addTransaction(String sender, String receiver, int amount){
        Transaction transResponse= new Transaction();
        transResponse.setSender(sender);
        transResponse.setReceiver(receiver);
        transResponse.setAmount(amount);
        transactions.add(transResponse);
        return getPrevBlock().getIndex()+1;
    }

    public void addNode(String address) throws MalformedURLException {
        URL url = new URL(address);
        nodes.add(url.getHost()+":"+url.getPort());
    }

    public boolean replaceChain(){

    Set<String> network=nodes;
    System.out.println(network);
    List<Block> longestChain=new ArrayList<>();
    int maxLength=chain.size();
    for(Object nodesLoop : network){
        System.out.println(nodesLoop);
        String response= makeHttpCall("http://"+nodesLoop+"/blockchain/get_blockchain");
        System.out.println(response);
        GetChainResponse responseMap = new Gson().fromJson(response, new TypeToken<GetChainResponse>(){}.getType());
        int chainLength=responseMap.getLength();
        chain=responseMap.getChain();
        if(chainLength>maxLength){
            System.out.println("Long chain detected");
            maxLength=chainLength;
            longestChain=chain;
            }
        }
    if(!longestChain.isEmpty()){
        chain=longestChain;
        return true;
    }
    return false;

    }

    public String makeHttpCall (String url)  {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .version(HttpClient.Version.HTTP_2)
                    .headers("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public  String nodeAdress(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }


}

