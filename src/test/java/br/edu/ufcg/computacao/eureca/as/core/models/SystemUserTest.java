package br.edu.ufcg.computacao.eureca.as.core.models;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;

import org.junit.Assert;
import org.junit.Test;

public class SystemUserTest {

    public static final String FAKE_USER_ID = "fakeUserId";
    public static final String FAKE_USER_NAME = "fakeUserName";
    public static final String FAKE_IDENTITY_PROVIDER = "fakeProviderId";

    // Test if a SystemUser is correctly serialized and later deserialized
    @Test
    public void testSerializationDeserealizationOfSystemUserObject() throws InternalServerErrorAsException {
        // Setup
        SystemUser systemUser = createSystemUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        SystemUser recoveredSystemUser = SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    // Test if size of content is going to overflow the limit a SystemUSer is allowed
    @Test(expected = InternalServerErrorAsException.class)
    public void testCreateUserWithTooMuchData() throws InternalServerErrorAsException {

        // setup
        String FAKE_USER_NAME = getLongUserName();

        SystemUser systemUser = new SystemUser(FAKE_USER_ID, FAKE_USER_NAME, FAKE_IDENTITY_PROVIDER);

        // exercise/verify
        SystemUser.serialize(systemUser);
    }

    private String getLongUserName() {
        String userName = "";

        for (int i = 0; i < 2 * SystemUser.SERIALIZED_SYSTEM_USER_MAX_SIZE; ++i) {
            userName += "A";
        }

        return userName;
    }

    private SystemUser createSystemUser() {
        return new SystemUser(FAKE_USER_ID, FAKE_USER_NAME, "fakeProviderId");
    }
}
