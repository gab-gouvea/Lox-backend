package br.com.lox.config;

import br.com.lox.domain.user.entity.User;
import br.com.lox.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            var admin = new User("Fernanda", "fefergouvea@gmail.com", passwordEncoder.encode("fefe1205"));
            userRepository.save(admin);
        }
    }
}
