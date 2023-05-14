package org.example;

import org.example.interfaces.SomeInterface;
import org.example.interfaces.SomeOtherInterface;
class SomeBean {

    @AutoInjectable
    private SomeInterface someField;
    @AutoInjectable
    private SomeOtherInterface otherField;

    public SomeBean() {}
    public void foo(){
        someField.doSomething();
        otherField.doSomeOther();
    }
}

