package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
@PreAuthorize(value = "hasRole('ADMIN')")
@AllArgsConstructor
public class AdminAccountController {

    private AccountService accountService;

    @GetMapping("/list")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.getAll());

        return "account-list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        Optional<Account> optionalAccount = accountService.getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getAccountRoles().stream().noneMatch(ar -> ar.getName().equals("ADMIN"))) {
                accountService.deleteById(id);
                return "redirect:/admin/account/list";
            } else {
                return "redirect:/admin/account/list";
            }
        }
        return "redirect:/admin/account/list";
    }

    @GetMapping("lock/{id}")
    public String lock (@PathVariable(name = "id") Long id) {
        Optional<Account> optionalAccount = accountService.getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getAccountRoles().stream().noneMatch(ar -> ar.getName().equals("ADMIN"))) {
                accountService.lock(id);
                return "redirect:/admin/account/list";
        } else {
            return "redirect:/admin/account/list";
        }
    }
        return "redirect:/admin/account/list";
    }
    @GetMapping("unlock/{id}")
    public String unlock (@PathVariable(name = "id") Long id) {
        Optional<Account> optionalAccount = accountService.getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getAccountRoles().stream().noneMatch(ar -> ar.getName().equals("ADMIN"))) {
                accountService.unlock(id);
                return "redirect:/admin/account/list";
        } else {
            return "redirect:/admin/account/list";
        }
    }
        return "redirect:/admin/account/list";
    }
}