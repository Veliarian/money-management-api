package com.money.api.service;

import com.money.api.entity.Safe;
import com.money.api.entity.User;
import com.money.api.repository.SafeRepository;
import com.money.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SafeService {
    private final SafeRepository safeRepository;
    private final UserRepository userRepository;

    public Safe createSafe(String name, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + ownerId + " not found"));

        Safe safe = Safe.builder()
                .name(name)
                .amount(0.0)
                .users(new HashSet<>(Set.of(owner)))
                .build();

        return safeRepository.save(safe);
    }

    public List<Safe> getUserSafes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        return safeRepository.findByUsersContains(user);
    }

    public Safe getSafeById(Long safeId, Long userId) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        if (safe.getUsers().stream().noneMatch(user -> user.getId().equals(userId))) {
            throw new AccessDeniedException("User does not have access to this safe");
        }

        return safe;
    }

    public Safe addUserToSafe(Long safeId, String username) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));

        safe.getUsers().add(user);
        return safeRepository.save(safe);
    }

    public List<String> getUsersFromSafe(Long safeId) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        return safe.getUsers().stream().map(User::getUsername).toList();
    }

    public Safe removeUserFromSafe(Long safeId, String username) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));

        safe.getUsers().remove(user);
        return safeRepository.save(safe);
    }

    public Safe updateSafeName(Long safeId, String newName) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        safe.setName(newName);
        return safeRepository.save(safe);
    }

    public void deleteSafe(Long safeId, Long userId) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        if (safe.getUsers().stream().noneMatch(user -> user.getId().equals(userId))) {
            throw new AccessDeniedException("User does not have access to this safe");
        }

        safeRepository.delete(safe);
    }
}
