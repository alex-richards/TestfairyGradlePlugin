package com.github.alexrichards.gradle.testfairy

import com.android.build.gradle.AppExtension
import com.google.gson.Gson
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

            project.android {
                buildTypes {
                    testfairy.initWith buildTypes.debug
                    testfairy {
                        packageNameSuffix '.' + TEST_FAIRY
                        versionNameSuffix '-' + TEST_FAIRY
                    }
                }
            }

            final AppExtension android = project.android
            final TestFairyConfigurationExtension testFairy = project.extensions.create TEST_FAIRY, TestFairyConfigurationExtension

            project.afterEvaluate {
                if (android.productFlavors.isEmpty()) {
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

                    if (!android.hasProperty('flavorDimensionList')
                            || android.flavorDimensionList == null
                            || android.flavorDimensionList.isEmpty()) {
                        android.productFlavors.each() { final flavour ->
                            final String flavorName = flavour.name.capitalize()
                            final Task packageTask = project.tasks["package${flavorName}${TEST_FAIRY.capitalize()}"]
                            project.task("testfairy${flavorName}") { final Task task ->
                                group 'TestFairy'
                                description "Uploads the ${flavorName} TestFairy APK to TestFairy"
                                dependsOn packageTask
                                parentTask.dependsOn task
                            } << {
                                packageTask.outputs.files.each { final File apk ->
                                    uploadTo testFairy, apk
                                }
                            }
                        }
                    } else {
                        final List<List<String>> dimensions = [];

                        android.flavorDimensionList.each { final String dimensionName ->
                            List<String> dimension = []
                            android.productFlavors.each { final flavor ->
                                if (flavor.flavorDimension == dimensionName) {
                                    dimension << flavor.name
                                }
                            }
                            dimensions << dimension
                        }

                        List<String> names = dimensions.remove(0)
                        while (dimensions.size() > 0) {
                            List<String> newNames = []
                            dimensions.remove(0).each { final String flavorNameTwo ->
                                names.each { final String flavorNameOne ->
                                    newNames << flavorNameOne + flavorNameTwo.capitalize()
                                }
                            }
                            names = newNames
                        }

                        names.each { final String name ->
                            final String flavorName = name.capitalize()
                            final Task packageTask = project.tasks["package${flavorName}${TEST_FAIRY.capitalize()}"]
                            project.task("testfairy${flavorName}") { final Task task ->
                                group 'TestFairy'
                                description "Uploads the ${flavorName} TestFairy APK to TestFairy"
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

        if (response.statusLine.statusCode != 200) {
            throw new RuntimeException('unable to upload: ' + response)
        } else {
            final TestFairyStatus status = new Gson().fromJson(new InputStreamReader(response.entity.content), TestFairyStatus.class)
            if ('ok'.equals(status.getStatus())) {
                // TODO done!!
            } else {
                throw new RuntimeException(status.getMessage())
            }
        }
    }
}
