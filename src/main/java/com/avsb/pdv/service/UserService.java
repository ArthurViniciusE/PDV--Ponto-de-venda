package com.avsb.pdv.service;

import com.avsb.pdv.dto.UserDTO;
import com.avsb.pdv.entity.User;
import com.avsb.pdv.exceptions.NoItemException;
import com.avsb.pdv.repository.UserRepository;
import com.avsb.pdv.security.SecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private ModelMapper mapper = new ModelMapper();

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserDTO(user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getPassword(),
                        user.isEneabled())).collect(Collectors.toList());
    }

    public UserDTO save(UserDTO user) {
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        User userToSave = mapper.map(user, User.class);
        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(),
                userToSave.getName(),
                userToSave.getUsername(),
                userToSave.getPassword(),
                userToSave.isEneabled());
    }

    public UserDTO findById(long id) {
        Optional<User> optional = userRepository.findById(id);

        if (!optional.isPresent()) {
            throw new NoItemException("Usuário não encontrado!");
        }
        User user = optional.get();
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.isEneabled());
    }

    public UserDTO update(UserDTO user) {
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        User userToSave = mapper.map(user, User.class);
        Optional<User> userToEdit = userRepository.findById(userToSave.getId());

        if (!userToEdit.isPresent()) {
            throw new NoItemException("Usuário não encontrado!");
        }

        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(),
                userToSave.getName(),
                userToSave.getUsername(),
                userToSave.getPassword(),
                userToSave.isEneabled());
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
