package org.ctu.app;

public interface AppLogic {
    default void init() {};
    default void loop() {};
    default void run() {
        init();
        loop();
    };
}
