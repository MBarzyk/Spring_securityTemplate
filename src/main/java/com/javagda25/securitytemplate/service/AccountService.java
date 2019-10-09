package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;


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
        accountRepository.deleteById(id);
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
}