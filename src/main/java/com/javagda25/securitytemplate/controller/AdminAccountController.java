package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.account.Account;
import com.javagda25.securitytemplate.model.dto.AccountPasswordResetRequest;
import com.javagda25.securitytemplate.service.AccountRoleService;
import com.javagda25.securitytemplate.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
@AllArgsConstructor
public class AdminAccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'ACCOUNT_MANAGER')")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.getAll());

        return "account-list";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'ACCOUNT_REMOVER')")
    public String delete(@PathVariable(name = "id") Long id) {
        accountService.deleteById(id);

        return "redirect:/admin/account/list";
    }

    @GetMapping("lock/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'ACCOUNT_MANAGER')")
    public String lock(@PathVariable(name = "id") Long id) {
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
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'ACCOUNT_MANAGER')")
    public String unlock(@PathVariable(name = "id") Long id) {
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

    @GetMapping("/resetPassword/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String resetPassword(Model model,
            @PathVariable(name = "id") Long accountId) {
        Optional<Account> optionalAccount = accountService.getById(accountId);

        if (optionalAccount.isPresent()) {
            model.addAttribute("account", optionalAccount.get());

            return "account-passwordReset";
        } else {
            return "redirect:/admin/account/list";
        }
    }

    @PostMapping("/resetPassword")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String resetPassword (AccountPasswordResetRequest request) {
        accountService.resetPassword(request);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/editRoles")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String editRoles(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.getById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", accountOptional.get());

            return "account-roles";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/editRoles")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);

        return "redirect:/admin/account/list";
    }
}