package org.example.maosaobra.security;

import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PessoaDetails implements UserDetails {

    private final Pessoa pessoa;

    public PessoaDetails(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String getUsername() {
        return pessoa.getEmail(); // username é o proprio email para identificação de cliente e trabalhador
    }

    @Override
    public @Nullable String getPassword() {
        return pessoa.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (pessoa instanceof Trabalhador)
            return List.of(new SimpleGrantedAuthority("ROLE_TRABALHADOR"));
        return List.of(new  SimpleGrantedAuthority("ROLE_CLIENTE"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
}
