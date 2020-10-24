package br.edu.ufcg.computacao.eureca.as.core.models;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;

import org.junit.Assert;
import org.junit.Test;

public class ShibbolethSystemUserTest {
    // Test if an ShibbolethSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfShibbolethUserObject() throws InternalServerErrorAsException {
        // Setup
        ShibbolethSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        ShibbolethSystemUser recoveredSystemUser = (ShibbolethSystemUser) SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private ShibbolethSystemUser createUser() {
        return new ShibbolethSystemUser("fakeId", "fakeName", "fakeProviderId",
                "fakeAssertionUrl", "fakeSamlAttributes");
    }
}
