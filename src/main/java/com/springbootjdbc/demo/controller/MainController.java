package com.springbootjdbc.demo.controller;

import com.springbootjdbc.demo.dao.BankAccountDAO;
import com.springbootjdbc.demo.exception.BankTransactionException;
import com.springbootjdbc.demo.form.SendMoneyForm;
import com.springbootjdbc.demo.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private BankAccountDAO bankAccountDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showBankAccounts (Model model){
        List<BankAccountInfo> list = bankAccountDAO.getBankAccounts();

        model.addAttribute("accountInfos", list);
        return "accountsPage";
    }

    @RequestMapping(value = "/sendMoney", method = RequestMethod.GET)
    public String viewSendMoneyPage(Model model){
        SendMoneyForm sendMoneyForm = new SendMoneyForm(1L, 2L, 700d);

        model.addAttribute("sendMoneyForm",sendMoneyForm);
        return "sendMoneyPage";
    }

    @RequestMapping(value = "/sendMoney", method = RequestMethod.POST)
    public String processSendMoney(Model model, SendMoneyForm sendMoneyForm){
        System.out.println("Send money: " + sendMoneyForm.getAmount());
        try{
            bankAccountDAO.sendMoney(sendMoneyForm.getFromAccountId(),sendMoneyForm.getToAccountId(),sendMoneyForm.getAmount());
        }catch (BankTransactionException e){
            model.addAttribute("errorMessage","Error: " + e.getMessage());
            return "/sendMoneyPage";
        }

        return "redirect:/";
    }
}
