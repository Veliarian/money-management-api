package com.money.api.controller;

import com.money.api.entity.Safe;
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
@RequestMapping("/safes/{safeId}/members")
@RequiredArgsConstructor
public class MembersController {

    private final SafeService safeService;

    @PostMapping
    public ResponseEntity<?> addMemberToSafe(@PathVariable Long safeId, @RequestBody SafeMemberRequest memberRequest) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        if (memberRequest.getUsername() == null || memberRequest.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }

        Safe safe = safeService.addUserToSafe(safeId, memberRequest.getUsername());
        SafeResponse safeResponse = new SafeResponse(safe);

        return ResponseEntity.status(HttpStatus.OK).body(safeResponse);
    }

    @GetMapping
    public ResponseEntity<?> getSafeMembers(@PathVariable Long safeId) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        List<String> members = safeService.getUsersFromSafe(safeId);

        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> removeMemberFromSafe(@PathVariable Long safeId, @PathVariable String username) {
        if (safeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Safe ID is required");
        }

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }

        Safe safe = safeService.removeUserFromSafe(safeId, username);
        SafeResponse safeResponse = new SafeResponse(safe);

        return ResponseEntity.status(HttpStatus.OK).body(safeResponse);
    }
}
