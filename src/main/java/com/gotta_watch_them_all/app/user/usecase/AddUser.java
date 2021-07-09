package com.gotta_watch_them_all.app.user.usecase;

import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AddUser {
    private final UserDao userDao;
    private final RoleDao roleDao;

    public DtoUser execute(
            String username,
            String email,
            String password,
            Set<String> strRoles
    ) throws AlreadyCreatedException, NotFoundException {
        checkIfUsernameOrEmailAlreadyExist(username, email);

        checkIfRoleInListContainManageList(strRoles, Arrays.asList("user", "admin"));
        HashSet<Role> roles = getRoles(strRoles);

        var savedUserId = userDao.createUser(username, email, password, roles);
        var savedUser = userDao.findById(savedUserId);

        return new DtoUser()
                .setId(savedUser.getId())
                .setEmail(savedUser.getEmail())
                .setName(savedUser.getName());
    }

    private void checkIfUsernameOrEmailAlreadyExist(String username, String email) throws AlreadyCreatedException {
        if (userDao.existsByUsername(username)) {
            throwAlreadyCreatedException(username, "User with username '%s' already exists");
        }
        if (userDao.existsByEmail(email)) {
            throwAlreadyCreatedException(email, "User with email '%s' already exists");
        }
    }

    private void throwAlreadyCreatedException(String username, String exceptionFormat) throws AlreadyCreatedException {
        var exceptionMessage = String.format(exceptionFormat, username);
        throw new AlreadyCreatedException(exceptionMessage);
    }

    private void checkIfRoleInListContainManageList(Set<String> strRoles, List<String> managedStrRoles) throws NotFoundException {
        if (strRoles == null) return;
        var notFoundRole = strRoles.stream()
                .filter(role -> !managedStrRoles.contains(role))
                .findFirst();
        if (notFoundRole.isPresent()) {
            var message = String.format("Role name '%s' not found", notFoundRole.get());
            throw new NotFoundException(message);
        }
    }

    private HashSet<Role> getRoles(Set<String> strRoles) {
        var roles = new HashSet<Role>();
        if (strRoles == null) {
            roles.add(roleDao.findByRoleName(RoleName.ROLE_USER));
            return roles;
        }
        var managedStrRoles = new HashMap<String, RoleName>();
        managedStrRoles.put("user", RoleName.ROLE_USER);
        managedStrRoles.put("admin", RoleName.ROLE_ADMIN);

        strRoles.forEach(roleStr -> roles.add(roleDao.findByRoleName(managedStrRoles.get(roleStr))));
        return roles;
    }
}
