package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotEmpty
    @Size(min = 4)
    private String password;

    private boolean isDisabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private Set<AccountRole> accountRoles;

    public boolean isAdmin() {
        return accountRoles.stream().anyMatch(ar -> ar.getName().equals("ADMIN"));
    }
}