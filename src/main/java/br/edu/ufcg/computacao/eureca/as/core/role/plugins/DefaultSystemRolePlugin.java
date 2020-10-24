package br.edu.ufcg.computacao.eureca.as.core.role.plugins;

import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;

public class DefaultSystemRolePlugin implements SystemRolePlugin {

    @Override
    public void setUserRoles(SystemUser user) {
        user.setUserRoles(null);
    }
}
