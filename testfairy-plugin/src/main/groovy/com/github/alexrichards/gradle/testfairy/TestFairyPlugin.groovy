package com.github.alexrichards.gradle.testfairy

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.DefaultHttpClient
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class TestFairyPlugin implements Plugin<Project> {

    static final String TEST_FAIRY = 'testfairy'

    @Override
    void apply(Project project) {
        project.configure(project) {
            if (!project.hasProperty('android')) {
                throw new IllegalArgumentException('only useful for android projects')
            }

            final TestFairyConfigurationExtension testFairy = project.extensions.create TEST_FAIRY, TestFairyConfigurationExtension

            if (!project.android.buildTypes.hasProperty(TEST_FAIRY)) {
                final def testFairyBuildType = project.android.buildTypes.create TEST_FAIRY

                if (project.android.buildTypes.hasProperty('debug')) {
                    testFairyBuildType.initWith project.android.buildTypes.debug
                }

                testFairyBuildType.packageNameSuffix ".${TEST_FAIRY}"
                testFairyBuildType.versionNameSuffix "-${TEST_FAIRY}"
            }

            project.afterEvaluate {
                if (project.android.productFlavors.isEmpty()) {
                    final Task packageTask = project.tasks["package${TEST_FAIRY.capitalize()}"]
                    project.task(TEST_FAIRY) {
                        group 'TestFairy'
                        description 'Uploads the TestFairy APK to TestFairy'
                        dependsOn packageTask
                    } << {
                        packageTask.outputs.files.each { final File apk ->
                            uploadTo testFairy, apk
                        }
                    }
                } else {
                    final Task parentTask = project.task(TEST_FAIRY) {
                        group 'TestFairy'
                        description 'Uploads all TestFairy APKs to TestFairy'
                    }

                    project.android.productFlavors.each() { final flavour ->
                        final String flavourName = flavour.name.capitalize()
                        final Task packageTask = project.tasks["package${flavourName}${TEST_FAIRY.capitalize()}"]
                        project.task("testfairy${flavourName}") {
                            group 'TestFairy'
                            description "Uploads the ${flavourName} TestFairy APK to TestFairy"
                            dependsOn packageTask
                            parentTask.dependsOn task
                        } << {
                            packageTask.outputs.files.each { final File apk ->
                                uploadTo testFairy, apk
                            }
                        }
                    }
                }
            }
        }
    }

    private void uploadTo(final TestFairyConfigurationExtension testFairy, final File apk) {
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create()

        builder.addBinaryBody('apk_file', apk).addTextBody('api_key', testFairy.apiKey)

        if (testFairy.testersGroups != null && !testFairy.testersGroups.isEmpty()) {
            builder.addTextBody('testers-groups', testFairy.testersGroups.join(','))
        }

        if (testFairy.metrics != null) {
            builder.addTextBody('metrics', testFairy.metrics.asString())
        }

        if (testFairy.maxDuration != null && testFairy.maxDuration.length() > 0) {
            builder.addTextBody('max-duration', testFairy.maxDuration)
        }

        if (testFairy.video != null && testFairy.video.length() > 0) {
            builder.addTextBody('video', testFairy.video)
        }

        if (testFairy.videoQuality != null && testFairy.video.length() > 0) {
            builder.addTextBody('video-quality', testFairy.video)
        }

        if (testFairy.videoRate != 0) {
            builder.addTextBody('video-rate', String.valueOf(testFairy.videoRate))
        }

        if (testFairy.iconWatermark != null && testFairy.iconWatermark.length() > 0) {
            builder.addTextBody('icon-watermark', testFairy.iconWatermark)
        }

        if (testFairy.comment != null && testFairy.comment.length() > 0) {
            builder.addTextBody('comment', testFairy.comment)
        }

        final HttpPost post = new HttpPost();
        post.setURI(new URI("https://${testFairy.hostName}/api/upload"))
        post.setEntity(builder.build())

        final HttpResponse response = new DefaultHttpClient().execute(post)

        // TODO error checking
        println response
    }
}
