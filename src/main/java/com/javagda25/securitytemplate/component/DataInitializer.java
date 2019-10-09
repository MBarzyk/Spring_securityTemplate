package com.javagda25.securitytemplate.component;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.AccountRole;
import com.javagda25.securitytemplate.repository.AccountRepository;
import com.javagda25.securitytemplate.repository.AccountRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addDefaultRole("USER");
        addDefaultRole("ADMIN");
        addDefaultAccount("admin", "admin", "ADMIN", "USER");
        addDefaultAccount("user", "user", "USER");
    }

    private void addDefaultAccount(String username, String pass, String... roles) {
        if (!accountRepository.existsByUsername(username)) {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(pass));

            Set<AccountRole> userRoles = findRoles(roles);
            account.setAccountRoles(userRoles);

            accountRepository.save(account);
        }
    }

    private Set<AccountRole> findRoles(String[] roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles) {
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);
        }
        return accountRoles;
    }

    private void addDefaultRole(String role) {
        if (!accountRoleRepository.existsByName(role)) {
            AccountRole newRole = new AccountRole();
            newRole.setName(role);

            accountRoleRepository.save(newRole);
        }
    }
}
