package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.AccountRole;
import com.javagda25.securitytemplate.model.dto.AccountPasswordResetRequest;
import com.javagda25.securitytemplate.repository.AccountRepository;
import com.javagda25.securitytemplate.repository.AccountRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;
    private AccountRoleRepository accountRoleRepository;


    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }

        // encrypting of the password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());

        // save to database
        accountRepository.save(account);


        return true;
    }


    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void deleteById(Long id) {
        Account account = accountRepository.getOne(id);
        if (!account.isAdmin()) {
            accountRepository.deleteById(id);
        }
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public void lock(Long id) {
        Optional<Account> optionalAccount = getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDisabled(true);
            accountRepository.save(account);
        }
    }


    public void unlock(Long id) {
        Optional<Account> optionalAccount = getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDisabled(false);
            accountRepository.save(account);
        }
    }

    public void resetPassword(AccountPasswordResetRequest request) {
        if (accountRepository.existsById(request.getAccountId())) {
            Account account = accountRepository.getOne(request.getAccountId());

            account.setPassword(passwordEncoder.encode(request.getResetpassword()));

            accountRepository.save(account);
        }
    }

    public void editRoles(Long accountId, HttpServletRequest request) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);

            Map<String, String[]> formParameters = request.getParameterMap();
            Set<AccountRole> newCollectionOfRoles = new HashSet<>();

            for (String roleName : formParameters.keySet()) {
                String[] values = formParameters.get(roleName);

                if (values[0].equals("on")) {
                    Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(roleName);

                    if (accountRoleOptional.isPresent()) {
                        newCollectionOfRoles.add(accountRoleOptional.get());
                    }
                }
            }

            account.setAccountRoles(newCollectionOfRoles);

            accountRepository.save(account);
        }
    }
}