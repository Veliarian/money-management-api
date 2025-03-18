package com.money.api.controller;

import com.money.api.entity.Safe;
import com.money.api.entity.User;
import com.money.api.entity.dto.CreateSafeRequest;
import com.money.api.entity.dto.SafeMemberRequest;
import com.money.api.entity.dto.SafeResponse;
import com.money.api.service.SafeService;
import com.money.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safes")
@RequiredArgsConstructor
public class SafesController {
    private final SafeService safeService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserSafes() {
        User user = userService.getCurrentUser();
        System.out.println(user.getId());

        List<Safe> safes = safeService.getUserSafes(user.getId());
        List<SafeResponse> safesResponse = safes.stream().map(SafeResponse::new).toList();

        return ResponseEntity.status(HttpStatus.OK).body(safesResponse);
    }

    @PostMapping
    public ResponseEntity<?> createSafe(@RequestBody CreateSafeRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        }

        User user = userService.getCurrentUser();

        Safe safe = safeService.createSafe(request.getName(), user.getId());
        SafeResponse safeResponse = new SafeResponse(safe);

        return ResponseEntity.status(HttpStatus.CREATED).body(safeResponse);
    }

    @GetMapping("/{safeId}")
    public ResponseEntity<?> getSafeInfo(@PathVariable Long safeId) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        User user = userService.getCurrentUser();

        Safe safe = safeService.getSafeById(safeId, user.getId());
        SafeResponse safeResponse = new SafeResponse(safe);

        return ResponseEntity.status(HttpStatus.OK).body(safeResponse);
    }

    @PutMapping("/{safeId}")
    public ResponseEntity<?> updateSafeName(@PathVariable Long safeId, @RequestBody CreateSafeRequest updateSafeRequest) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        if(updateSafeRequest.getName() == null || updateSafeRequest.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        }

        Safe safe = safeService.updateSafeName(safeId, updateSafeRequest.getName());
        SafeResponse safeResponse = new SafeResponse(safe);

        return ResponseEntity.status(HttpStatus.OK).body(safeResponse);
    }

    @DeleteMapping("/{safeId}")
    public ResponseEntity<?> deleteSafe(@PathVariable Long safeId) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        User user = userService.getCurrentUser();
        safeService.deleteSafe(safeId, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body("Safe deleted successfully");
    }
}
