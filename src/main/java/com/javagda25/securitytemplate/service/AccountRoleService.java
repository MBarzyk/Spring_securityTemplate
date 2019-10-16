package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.account.AccountRole;
import com.javagda25.securitytemplate.repository.AccountRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AccountRoleService {
    private final AccountRoleRepository accountRoleRepository;


    @Value("${account.default.roles:USER}")
    private String[] defaultRoles;

    public Set<AccountRole> getDefaultRoles() {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : defaultRoles) {
            Optional<AccountRole> optionalRole = accountRoleRepository.findByName(role);
            optionalRole.ifPresent(accountRoles::add);
        }

        return accountRoles;
    }

    public List<AccountRole> getAll() {
        return accountRoleRepository.findAll();
    }
}
