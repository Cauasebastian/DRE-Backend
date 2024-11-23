package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.model.Usuario;
import org.moscabranca.drebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public Usuario salvarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return userRepository.save(usuario);
    }
    public Optional<Usuario> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<Usuario> findById(Long id) {
        return userRepository.findById(id);
    }
}
