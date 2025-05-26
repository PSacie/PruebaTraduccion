package com.myvideogamelist.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.model.Role;
import com.myvideogamelist.backend.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
