package com.bettercloud.vault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The code used to load environment variables is encapsulated within an inner class,
 * so that a mock version of that environment loader can be used by unit tests.
 */
public class EnvironmentLoader {

    public String loadVariable(final String name) {
        String value = null;
        if (VaultConfig.VAULT_TOKEN.equals(name)) {

            // Special handling for the VAULT_TOKEN variable, since it can be read from the filesystem if it's not
            // found in the environment
            if (System.getenv(VaultConfig.VAULT_TOKEN) != null) {
                // Found it in the environment
                value = System.getenv(name);
            } else {
                // Not in the environment, looking for a ".vault-token" file in the executing user's home directory instead
                try {
                    final byte[] bytes = Files.readAllBytes(
                            Paths.get(System.getProperty("user.home")).resolve(".vault-token"));
                    value = new String(bytes, "UTF-8").trim();
                } catch (IOException e) {
                    // No-op... there simply isn't a token value available
                }
            }
        } else {

            // Normal handling for all other variables.  We just check the environment.
            value = System.getenv(name);
        }
        return value;
    }

}
