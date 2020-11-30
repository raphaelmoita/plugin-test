package org.moita.plugin.enabled;

import org.moita.plugin.disabled.TestAnnotation;
import org.moita.plugin.disabled.TestNonSerializable;
import org.moita.plugin.misc.Class1;
import org.moita.plugin.misc.Class2;
import org.moita.plugin.misc.Class3;

@TestAnnotation(values = {Class1.class, Class2.class, Class3.class})
public class Test extends TestParent {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
