package com.thanhthbm.restaurant.service;

import com.thanhthbm.restaurant.domain.User;
import com.thanhthbm.restaurant.domain.response.ResultPaginationDTO;
import com.thanhthbm.restaurant.repository.UserRepository;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
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
}
