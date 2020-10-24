package br.edu.ufcg.computacao.eureca.as.core.role.plugins;

import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.core.PropertiesHolder;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Assert;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertiesHolder.class)
public class VanillaSystemRolePluginTest {

    private final String userIdAdmin1 = "userId-admin1";
    private final String userNameAdmin1 = "username-admin1";
    private final String identityProviderIdAdmin1 = "id-provider-admin1";

    private final String userIdAdmin2 = "userId-admin2";
    private final String userNameAdmin2 = "username-admin2";
    private final String identityProviderIdAdmin2 = "id-provider-admin2";

    private final String userIdManager1 = "userId-manager1";
    private final String userNameManager1 = "username-manager1";
    private final String identityProviderIdManager1 = "id-provider-manager1";

    private final String userIdManager2 = "userId-manager2";
    private final String userNameManager2 = "username-manager2";
    private final String identityProviderIdManager2 = "id-provider-manager2";

    private final String userIdNoSpecialRole = "userId-nospecialrole";
    private final String userNameNoSpecialRole = "username-nospecialrole";
    private final String identityProviderIdNoSpecialRole = "id-provider-nospecialrole";

    private final String defaultRole = "user";
    private final String adminString = "admin";
    private final String managerString = "manager";
    private final String roles = String.format("%s,%s", adminString, managerString);
    private final String admins = String.format("%s,%s", userIdAdmin1, userIdAdmin2);
    private final String managers = String.format("%s,%s,%s", userIdManager1, userIdAdmin2, userIdManager2);

    private final String emptyRoles = "";
    private final String emptyAdmins = "";
    private final String emptyManagers = "";

    private VanillaSystemRolePlugin plugin;

    /**
     * Set up methods
     */

    private void setUpSystemWithAdminAndManagerRoles() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder properties = Mockito.mock(PropertiesHolder.class);

        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(properties);

        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY)).thenReturn(defaultRole);
        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.ROLES_KEY)).thenReturn(roles);
        Mockito.when(properties.getProperty(adminString)).thenReturn(admins);
        Mockito.when(properties.getProperty(managerString)).thenReturn(managers);

        plugin = new VanillaSystemRolePlugin();
    }

    private void setUpSystemWithNoSpecialRoles() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder properties = Mockito.mock(PropertiesHolder.class);

        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(properties);

        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY)).thenReturn(defaultRole);
        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.ROLES_KEY)).thenReturn(emptyRoles);

        plugin = new VanillaSystemRolePlugin();
    }

    private void setUpSystemWithNoUsersForDefinedRoles() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder properties = Mockito.mock(PropertiesHolder.class);

        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(properties);

        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY)).thenReturn(defaultRole);
        Mockito.when(properties.getProperty(ConfigurationPropertyKeys.ROLES_KEY)).thenReturn(roles);
        Mockito.when(properties.getProperty(adminString)).thenReturn(emptyAdmins);
        Mockito.when(properties.getProperty(managerString)).thenReturn(emptyManagers);

        // Should this be allowed?
        plugin = new VanillaSystemRolePlugin();
    }

    @Test
    public void testSetUserRolesOneRole() {
        setUpSystemWithAdminAndManagerRoles();

        // User is admin
        SystemUser userAdmin1 = new SystemUser(userIdAdmin1, userNameAdmin1, identityProviderIdAdmin1);

        plugin.setUserRoles(userAdmin1);

        Set<String> rolesUserAdmin1 = userAdmin1.getUserRoles();
        Assert.assertEquals(1, rolesUserAdmin1.size());
        Assert.assertTrue(rolesUserAdmin1.contains(adminString));

        // User is manager
        SystemUser userManager1 = new SystemUser(userIdManager1, userNameManager1, identityProviderIdManager1);

        plugin.setUserRoles(userManager1);

        Set<String> rolesUserManager1 = userManager1.getUserRoles();
        Assert.assertEquals(1, rolesUserManager1.size());
        Assert.assertTrue(rolesUserManager1.contains(managerString));

        SystemUser userManager2 = new SystemUser(userIdManager2, userNameManager2, identityProviderIdManager2);

        plugin.setUserRoles(userManager2);

        Set<String> rolesUserManager2 = userManager2.getUserRoles();
        Assert.assertEquals(1, rolesUserManager2.size());
        Assert.assertTrue(rolesUserManager2.contains(managerString));
    }

    @Test
    public void testSetUserRolesTwoRoles() {
        setUpSystemWithAdminAndManagerRoles();

        // User is both admin and manager
        SystemUser userAdmin2 = new SystemUser(userIdAdmin2, userNameAdmin2, identityProviderIdAdmin2);

        plugin.setUserRoles(userAdmin2);

        Set<String> rolesUserAdmin2 = userAdmin2.getUserRoles();
        Assert.assertEquals(2, rolesUserAdmin2.size());
        Assert.assertTrue(rolesUserAdmin2.contains(adminString));
        Assert.assertTrue(rolesUserAdmin2.contains(managerString));
    }

    @Test
    public void testSetUserRolesNoSpecialRoles() {
        setUpSystemWithAdminAndManagerRoles();

        // User has no special roles defined
        SystemUser userNoSpecialRole = new SystemUser(userIdNoSpecialRole, userNameNoSpecialRole,
                identityProviderIdNoSpecialRole);

        plugin.setUserRoles(userNoSpecialRole);

        Set<String> rolesUserNoSpecialRole = userNoSpecialRole.getUserRoles();
        Assert.assertEquals(1, rolesUserNoSpecialRole.size());
        Assert.assertTrue(rolesUserNoSpecialRole.contains(defaultRole));
    }

    @Test
    public void testSystemWithNoSpecialRoles() {
        setUpSystemWithNoSpecialRoles();

        // User has no special role defined
        SystemUser userNoSpecialRole = new SystemUser(userIdNoSpecialRole, userNameNoSpecialRole,
                identityProviderIdNoSpecialRole);

        plugin.setUserRoles(userNoSpecialRole);

        Set<String> rolesUserNoSpecialRole = userNoSpecialRole.getUserRoles();
        Assert.assertEquals(1, rolesUserNoSpecialRole.size());
        Assert.assertTrue(rolesUserNoSpecialRole.contains(defaultRole));

        // User has no special role defined
        SystemUser userAdmin1 = new SystemUser(userIdAdmin1, userNameAdmin1, identityProviderIdAdmin1);

        plugin.setUserRoles(userAdmin1);

        Set<String> rolesUserAdmin1 = userAdmin1.getUserRoles();
        Assert.assertEquals(1, rolesUserAdmin1.size());
        Assert.assertTrue(rolesUserAdmin1.contains(defaultRole));
    }

    @Test
    public void testConfigurationFileDoesNotListUsersForRoles() {
        setUpSystemWithNoUsersForDefinedRoles();

        // User has no special role defined
        SystemUser userNoSpecialRole = new SystemUser(userIdNoSpecialRole, userNameNoSpecialRole,
                identityProviderIdNoSpecialRole);

        plugin.setUserRoles(userNoSpecialRole);

        Set<String> rolesUserNoSpecialRole = userNoSpecialRole.getUserRoles();
        Assert.assertEquals(1, rolesUserNoSpecialRole.size());
        Assert.assertTrue(rolesUserNoSpecialRole.contains(defaultRole));

        // User has no special role defined
        SystemUser userAdmin1 = new SystemUser(userIdAdmin1, userNameAdmin1, identityProviderIdAdmin1);

        plugin.setUserRoles(userAdmin1);

        Set<String> rolesUserAdmin1 = userAdmin1.getUserRoles();
        Assert.assertEquals(1, rolesUserAdmin1.size());
        Assert.assertTrue(rolesUserAdmin1.contains(defaultRole));
    }
}
