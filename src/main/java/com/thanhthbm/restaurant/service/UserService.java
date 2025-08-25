package com.thanhthbm.restaurant.service;

import com.thanhthbm.restaurant.domain.User;
import com.thanhthbm.restaurant.domain.response.ResCreateUserDTO;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.repository.UserRepository;
import com.thanhthbm.restaurant.util.exception.ResourceAlreadyExistsException;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        return userOptional.get();
    }

    public ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());

        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(userPage.getContent());
        return resultPaginationDTO;
    }

    public void updateUserToken(String token, String username) {
        User user = this.getUserByUsername(username);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setUsername(user.getUsername());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setCreatedAt(user.getCreatedAt());
        return resCreateUserDTO;

    }

    public User handleCreateUser(User reqUser) {
        if (this.userRepository.existsByUsername(reqUser.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        if (this.userRepository.existsByEmail(reqUser.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        return this.userRepository.save(reqUser);
    }
}
