package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.exhandling.PageNotFoundException;
import com.marceljsh.binarfud.model.User;
import com.marceljsh.binarfud.payload.request.UserChangePasswordRequest;
import com.marceljsh.binarfud.payload.request.UserSearchRequest;
import com.marceljsh.binarfud.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.UserResponse;
import com.marceljsh.binarfud.repository.UserRepository;
import com.marceljsh.binarfud.service.spec.UserService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public void deactivate(UUID id) {
    userRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void restore(UUID id) {
    userRepo.restore(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    userRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse get(UUID id) {
    User user = userRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("user not found"));

    return UserResponse.of(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findByUsername(String username) {
    if (!userRepo.existsByUsername(username)) {
      throw new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND);
    }

    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND));

    return UserResponse.of(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findByEmail(String email) {
    if (!userRepo.existsByEmail(email)) {
      throw new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND);
    }

    User user = userRepo.findByEmail(email)
      .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND));

    return UserResponse.of(user);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<UserResponse> search(UserSearchRequest request) {
    Specification<User> specification = ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getUsername())) {
        predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("username")),
          "%" + request.getUsername().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getEmail())) {
        predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("email")),
          "%" + request.getEmail().toLowerCase() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<User> users = userRepo.findAll(specification, pageable);

    if (users.getTotalElements() == 0) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() > users.getTotalPages() - 1) {
      throw new PageNotFoundException(
          String.format("page %d out of bounds (%s)", request.getPage() + 1, users.getTotalPages()));
    }

    List<UserResponse> result = users.getContent()
        .stream()
        .map(UserResponse::of)
        .toList();

    return new PageImpl<>(result, pageable, users.getTotalElements());
  }

  @Transactional
  @Override
  public UserResponse updateInfo(UserUpdateInfoRequest request) {
    UUID id = UUID.fromString(request.getId());
    if (!userRepo.existsById(id)) {
      throw new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND);
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      throw new DataIntegrityViolationException("username is already taken");
    }

    User user = userRepo.updateInfo(id, request.getUsername());

    return UserResponse.of(user);
  }

  @Transactional
  @Override
  public void updatePassword(UserChangePasswordRequest request) {
    UUID id = UUID.fromString(request.getId());
    if (!userRepo.existsById(id)) {
      throw new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND);
    }

    User user = userRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND));

    if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new IllegalArgumentException("old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepo.save(user);
  }
}
