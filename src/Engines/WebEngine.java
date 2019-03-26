package Engines;

import Servcies.DIResolver;

abstract class WebEngine {

    final DIResolver diResolver;

    WebEngine(DIResolver diResolver) {
        this.diResolver = diResolver;
    }
}
