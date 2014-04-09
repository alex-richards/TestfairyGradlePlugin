package com.github.alexrichards.gradle.testfairy

class TestFairyMetrics {

    boolean cpu
    boolean memory
    boolean network
    boolean phoneSignal
    boolean logcat
    boolean gps
    boolean battery
    boolean mic

    void cpu(final boolean cpu = true) {
        this.cpu = cpu
    }

    void memory(final boolean memory = true) {
        this.memory = memory
    }

    void network(final boolean network = true) {
        this.network = network
    }

    void phoneSignal(final boolean phoneSignal = true) {
        this.phoneSignal = phoneSignal
    }

    void logcat(final boolean logcat = true) {
        this.logcat = logcat
    }

    void gps(final boolean gps = true) {
        this.gps = gps
    }

    void battery(final boolean battery = true) {
        this.battery = battery
    }

    void mic(final boolean mic = true) {
        this.mic = mic
    }

    String asString(){
        return cpu ? 'cpu,' : '' +
        memory  ? 'memory,' : '' +
        network  ? 'network,' : '' +
        phoneSignal  ? 'phone-signal,' : '' +
        logcat  ? 'logcat,' : '' +
        gps  ? 'gps,' : '' +
        battery  ? 'battery,' : '' +
        mic ? 'mic,' : ''
    }
}
