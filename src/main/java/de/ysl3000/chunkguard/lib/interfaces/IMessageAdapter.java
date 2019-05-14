package de.ysl3000.chunkguard.lib.interfaces;

public interface IMessageAdapter
{
    String youNowOwnThisChunk();
    
    String youCouldNotBuyYourOwnChunk();
    
    String youAreNowRemovedFromEasyBuy();
    
    String youAreNowAddedToEasyBuy();
    
    String youNeedAnItemOfCoalInYourHandToUseThisMode();
    
    String chunkAlreadyExist();
    
    String chunkIsNowCreated();
    
    String chunkAvailableMessage();
    
    String getChunkOwnerInfo();
    
    String getChunkOwnerInfoOnline();
    
    String notEnoughMoney();
    
    String musBeANumber();
    
    String noChunkAvailable();
    
    String buyingTransactionFailed();
    
    String speedModMessage();
    
    String notYourChunk();
    
    String getFlagSet();
    
    String getMemberRemovedSuccessfully();
    
    String getMemberAddedSuccessfully();
    
    String getCanceled();
    
    String getChunkSoldSuccessfully();
    
    String getKeyAccept();
    
    String getKeyDencline();
}
