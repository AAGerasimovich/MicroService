package app;


import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.Collections;

public class ServiceNodeApp {
    public static void main(String[] args) throws InterruptedException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setUserAttributes(Collections.singletonMap("operation", true));
        Ignite ignite = Ignition.start(cfg);

    }
}