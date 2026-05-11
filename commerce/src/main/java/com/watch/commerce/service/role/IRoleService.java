package com.watch.commerce.service.role;

import com.watch.commerce.model.Role;
import com.watch.commerce.model.User;

public interface  IRoleService {
    

    Role getByRoleName(String roleName);

    void assignRoleToUser(User user,String roleName);

    

}
