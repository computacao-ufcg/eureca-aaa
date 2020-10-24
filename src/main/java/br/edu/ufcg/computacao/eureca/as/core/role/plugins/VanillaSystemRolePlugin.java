package br.edu.ufcg.computacao.eureca.as.core.role.plugins;

import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.PropertiesHolder;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VanillaSystemRolePlugin implements SystemRolePlugin {

    /**
     * This class requires a configuration file using a mapping role -> users. 
     * However, since normally we want to know the roles for a given user 
     * rather than the list of users that have a given role, 
     * for performance reasons, we use a user -> roles map.
     */
    private Map<String, Set<String>> usersWithSpecialRoles;
    private String defaultRole;

    public VanillaSystemRolePlugin() {
        defaultRole = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY);
        getUsersWithSpecialRoles();
    }

    private void getUsersWithSpecialRoles() {
        usersWithSpecialRoles = new HashMap<String, Set<String>>();
        String rolesNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ROLES_KEY);

        if (!rolesNamesString.isEmpty()) {
            for (String roleName : rolesNamesString.trim().split(SystemConstants.ROLE_NAMES_SEPARATOR)) {
                getUserNamesWithRole(roleName);
            }
        }
    }

    private void getUserNamesWithRole(String roleName) {
        String userNamesWithRoleString = PropertiesHolder.getInstance().getProperty(roleName);

        if (!userNamesWithRoleString.isEmpty()) {
            for (String userId : userNamesWithRoleString.trim().split(SystemConstants.ROLE_NAMES_SEPARATOR)) {
                if (!usersWithSpecialRoles.containsKey(userId)) {
                    usersWithSpecialRoles.put(userId, new HashSet<String>());
                }

                usersWithSpecialRoles.get(userId).add(roleName);
            }
        }
    }

    @Override
    public void setUserRoles(SystemUser user) {
        String userId = user.getId();

        if (usersWithSpecialRoles.containsKey(userId)) {
            user.setUserRoles(usersWithSpecialRoles.get(userId));
        } else {
            HashSet<String> defaultRoleSet = new HashSet<String>();
            defaultRoleSet.add(defaultRole);
            user.setUserRoles(defaultRoleSet);
        }
    }
}
