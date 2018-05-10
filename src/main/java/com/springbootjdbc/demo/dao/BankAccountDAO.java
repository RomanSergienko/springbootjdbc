package com.springbootjdbc.demo.dao;

import com.springbootjdbc.demo.mapper.BankAccountMapper;
import com.springbootjdbc.demo.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount (Long id, double amount){

    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void sendMoney(Long fromAccountId, Long toAccountId, double amount){

    }
}
