package org.thivernale.springbootstarter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDatasourceConfig {
    final Environment environment;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public DevDatasourceConfig(Environment environment) {
        this.environment = environment;
    }

    public void exploreEnvironment() {
        for (String profileName : environment.getActiveProfiles()) {
            //
        }
    }
}
