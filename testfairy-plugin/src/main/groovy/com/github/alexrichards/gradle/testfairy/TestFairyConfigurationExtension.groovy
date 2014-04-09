package com.github.alexrichards.gradle.testfairy

class TestFairyConfigurationExtension {

    // TODO proguard_file // Proguard mapping file.

    String hostName = 'app.testfairy.com'
    String apiKey
    List<String> testersGroups = []
    TestFairyMetrics metrics
    String maxDuration
    String video
    String videoQuality
    float videoRate
    String iconWatermark
    String comment

    void hostName(final String hostName) {
        this.hostName = hostName
    }

    void apiKey(final String apiKey) {
        this.apiKey = apiKey
    }

    void testersGroups(final String... testersGroups) {
        testersGroups.each { testersGroup ->
            this.testersGroups << testersGroup
        }
    }

    void testersGroup(final String testersGroup) {
        this.testersGroups << testersGroup
    }

    void metrics(final Closure closure) {
        closure.delegate = metrics = new TestFairyMetrics()
        closure.run()
    }

    void maxDuration(final String maxDuration) {
        this.maxDuration = maxDuration
    }

    void video(final String video) {
        this.video = video
    }

    void videoQuality(final String videoQuality) {
        this.videoQuality = videoQuality
    }

    void videoRate(final float videoRate) {
        this.videoRate = videoRate
    }

    void iconWatermark(final String iconWatermark) {
        this.iconWatermark = iconWatermark
    }

    void comment(final String comment) {
        this.comment = comment
    }
}
