package com.marceljsh.binarfud.user.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.app.exception.PageNotFoundException;
import com.marceljsh.binarfud.user.dto.UserChangePasswordRequest;
import com.marceljsh.binarfud.user.dto.UserResponse;
import com.marceljsh.binarfud.user.dto.UserSearchRequest;
import com.marceljsh.binarfud.user.dto.UserUpdateInfoRequest;
import com.marceljsh.binarfud.user.model.User;
import com.marceljsh.binarfud.user.repositoy.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void deactivate(UUID id) {
    log.trace("Deactivating user with id: {}", id);

    if (userRepo.existsById(id)) {
      log.info("Proceeding to deactivate found user with id: {}", id);
      userRepo.deactivateById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.trace("Restoring user with id: {}", id);

    if (userRepo.existsById(id)) {
      log.info("Proceeding to restore found user with id: {}", id);
      userRepo.restoreById(id);
    }
  }

  @Override
  @Transactional
  public void remove(UUID id) {
    log.trace("Removing user with id: {}", id);

    if (userRepo.existsById(id)) {
      log.info("Proceeding to remove user with id: {}", id);
      userRepo.deleteById(id);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse get(UUID id) {
    log.trace("Fetching user with id: {}", id);

    User user = userRepo.findById(id).orElse(null);

    if (user == null) {
      log.error("User with id: {} not found", id);
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    log.info("Found user: @{}", user.getUsername());

    return UserResponse.from(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getByUsername(String username) {
    log.trace("Fetching user with username: {}", username);

    if (!userRepo.existsByUsername(username)) {
      log.error("User with username: {} not found", username);
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    User user = userRepo.findByUsername(username).orElse(null);

    if (user == null) {
      log.error("User with username: @{} not found", username);
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    log.info("Found user: @{}", user.getUsername());

    return UserResponse.from(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getByEmail(String email) {
    log.trace("Fetching user with email: {}", email);

    if (!userRepo.existsByEmail(email)) {
      log.error("User with email: {} not found", email);
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
      log.error("User with email: {} not found", email);
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    log.info("Found user: {}", user.getEmail());

    return UserResponse.from(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> search(UserSearchRequest request) {
    log.trace(
        "Searching for user: username={}, email={}",
        request.getUsername(),
        request.getEmail());

    Specification<User> specification = ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getUsername())) {
        predicates.add(cb.like(
            cb.lower(root.get("username")),
            "%" + request.getUsername().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getEmail())) {
        predicates.add(cb.like(
            cb.lower(root.get("email")),
            "%" + request.getEmail().toLowerCase() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<User> users = userRepo.findAll(specification, pageable);

    if (users.getTotalElements() == 0) {
      log.info("User search returned no results");
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() > users.getTotalPages() - 1) {
      String error = String.format(
          "page %d out of bounds (%s)",
          request.getPage() + 1,
          users.getTotalPages());

      log.error(error);
      throw new PageNotFoundException(error);
    }

    log.info("Found {} users", users.getTotalElements());

    List<UserResponse> result = users.getContent()
        .stream()
        .map(UserResponse::from)
        .toList();

    return new PageImpl<>(result, pageable, users.getTotalElements());
  }

  @Override
  @Transactional
  public UserResponse updateInfo(UserUpdateInfoRequest request) {
    log.info("Updating user info: {}", request.getId());

    if (!userRepo.existsById(request.getId())) {
      log.error("User with id: {} not found", request.getId());
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      log.error("Username @{} is already taken", request.getUsername());
      throw new DataIntegrityViolationException("Username is already taken");
    }

    User user = userRepo.findById(request.getId()).orElse(null);

    if (user == null) {
      log.error("User with id: {} not found", request.getId());
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    log.info("Updating user info: @{}", user.getUsername());
    user.setUsername(request.getUsername());

    return UserResponse.from(userRepo.save(user));
  }

  @Override
  @Transactional
  public void updatePassword(UserChangePasswordRequest request) {
    log.info("Updating user password: {}", request.getId());

    if (!userRepo.existsById(request.getId())) {
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    User user = userRepo.findById(request.getId()).orElse(null);

    if (user == null) {
      log.error("User with id: {} not found", request.getId());
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepo.save(user);

    log.info("Password updated for user: @{}", user.getUsername());
  }

}
