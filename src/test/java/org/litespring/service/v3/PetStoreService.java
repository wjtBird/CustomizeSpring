package org.litespring.service.v3;

import org.litespring.dao.v3.AccountDao;
import org.litespring.dao.v3.ItemDao;

/**
 * Created by wjt on 2018/6/30.
 */
public class PetStoreService {


    private AccountDao accountDao;
    private ItemDao itemDao;
    private Integer version;


    public PetStoreService() {
    }

    public PetStoreService(AccountDao accountDao, ItemDao itemDao, Integer version) {
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = version;
    }



    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
