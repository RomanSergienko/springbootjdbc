package com.springbootjdbc.demo.dao;

import com.springbootjdbc.demo.exception.BankTransactionException;
import com.springbootjdbc.demo.mapper.BankAccountMapper;
import com.springbootjdbc.demo.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
@Repository
@Transactional
public class BankAccountDAO extends JdbcDaoSupport {
    @Autowired
    public BankAccountDAO (DataSource dataSource){
        this.setDataSource(dataSource);
    }

    public List<BankAccountInfo> getBankAccounts() {
        // Select ba.Id, ba.Full_Name, ba.Balance From Bank_Account ba
        String sql = BankAccountMapper.BASE_SQL;

        Object[] params = new Object[]{};
        BankAccountMapper mapper = new BankAccountMapper();
        List<BankAccountInfo> list = this.getJdbcTemplate().query(sql,params,mapper);

        return list;
    }

    public BankAccountInfo findBankAccount (Long id){
        // Select ba.Id, ba.Full_Name, ba.Balance From Bank_Account ba
        // Where ba.Id = ?
        String sql = BankAccountMapper.BASE_SQL + " where ba.id = ?";

        Object[] params = new Object[]{id};
        BankAccountMapper mapper = new BankAccountMapper();
        try {
            BankAccountInfo accountInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return accountInfo;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount (Long id, double amount) throws BankTransactionException {
        BankAccountInfo accountInfo = this.findBankAccount(id);
        if (accountInfo == null){
            throw new BankTransactionException("Account with id:" + id + " not found");
        }
        double newBalance = accountInfo.getBalance() + amount;
        if (accountInfo.getBalance() + amount < 0){
            throw new BankTransactionException("The money in the account '" + id + "' is not enough (" + accountInfo.getBalance() + ")");
        }
        accountInfo.setBalance(newBalance);
        //Update to DB
        String sqlUpdate = "Update Bank_Account set Balance = ? where id = ?";
        this.getJdbcTemplate().update(sqlUpdate,accountInfo.getBalance(),accountInfo.getId());
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankTransactionException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, double amount) throws BankTransactionException{
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, -amount);
    }
}
