package br.edu.ufcg.computacao.eureca.as.core.models;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.util.SerializedEntityHolder;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.Set;

public class SystemUser extends User {

    public static final int SERIALIZED_SYSTEM_USER_MAX_SIZE = 2048;

    private String identityProviderId;
    private Set<String> roles; 

    public SystemUser(String userId, String userName, String identityProviderId) {
        super(userId, userName);
        this.identityProviderId = identityProviderId;
    }

    public String getIdentityProviderId() {
        return identityProviderId;
    }

    public Set<String> getUserRoles() {
        return roles;
    }
    
    public void setUserRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemUser that = (SystemUser) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getIdentityProviderId(), that.getIdentityProviderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityProviderId, getId(), getName());
    }

    public static String serialize(SystemUser systemUser) throws InternalServerErrorException {
        SerializedEntityHolder<SystemUser> serializedSystemUserHolder = new SerializedEntityHolder<SystemUser>(systemUser);
        String serializedSystemUser = serializedSystemUserHolder.toString();

        if(serializedSystemUser.length() > SystemUser.SERIALIZED_SYSTEM_USER_MAX_SIZE) {
            throw new InternalServerErrorException(Messages.MAXIMUM_SIZE_EXCEEDED);
        }

        return serializedSystemUser;
    }

    public static SystemUser deserialize(String serializedSystemUser) throws InternalServerErrorException {
        try {
            SerializedEntityHolder<SystemUser> serializedSystemUserHolder = (new Gson()).fromJson(serializedSystemUser, SerializedEntityHolder.class);
            SystemUser systemUser = serializedSystemUserHolder.getSerializedEntity();
            return systemUser;
        } catch (ClassNotFoundException e) {
            throw new InternalServerErrorException(Messages.UNABLE_TO_FIND_SYSTEM_USER_CLASS);
        }
    }
}
