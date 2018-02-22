package io.hybridtheory.mutalisk.webservice.resource;


import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClzsResourceApplication extends Application {
    private Set<Class<?>> clzs = new HashSet<>();

    public ClzsResourceApplication(Class<?>... clxs) {
        Collections.addAll(clzs, clxs);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return clzs;
    }


}
