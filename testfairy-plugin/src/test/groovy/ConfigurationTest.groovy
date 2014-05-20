import com.android.build.gradle.AppExtension
import com.github.alexrichards.gradle.testfairy.TestFairyConfigurationExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ConfigurationTest {

    Project project
    TestFairyConfigurationExtension testfairy
    AppExtension android

    @Before
    void before() {
        project = ProjectBuilder.builder().build()

        project.apply plugin: 'android'
        project.apply plugin: 'testfairy'

        testfairy = project.testfairy
        android = project.android
    }

    @Test
    void testDelegate() {
        project.testfairy {
            hostName 'test host'
            apiKey 'test key'
            testersGroups 'one', 'two'
            testersGroup 'three'
            metrics {
                cpu false
                memory true
                network false
                phoneSignal true
                logcat false
                gps true
                battery false
                mic true
            }
            maxDuration '1d'
            video 'wifi'
            videoQuality 'high'
            videoRate 1.5 as float
            iconWatermark 'on'
            comment 'test comment'
        }

        Assert.assertEquals testfairy.hostName, 'test host'
        Assert.assertEquals testfairy.apiKey, 'test key'
        Assert.assertEquals testfairy.testersGroups.size(), 3
        Assert.assertEquals testfairy.metrics.cpu, false
        Assert.assertEquals testfairy.metrics.memory, true
        Assert.assertEquals testfairy.metrics.network, false
        Assert.assertEquals testfairy.metrics.phoneSignal, true
        Assert.assertEquals testfairy.metrics.logcat, false
        Assert.assertEquals testfairy.metrics.gps, true
        Assert.assertEquals testfairy.metrics.battery, false
        Assert.assertEquals testfairy.metrics.mic, true
        Assert.assertEquals testfairy.maxDuration, '1d'
        Assert.assertEquals testfairy.video, 'wifi'
        Assert.assertEquals testfairy.videoQuality, 'high'
        Assert.assertEquals testfairy.videoRate, 1.5, 0
        Assert.assertEquals testfairy.iconWatermark, 'on'
        Assert.assertEquals testfairy.comment, 'test comment'
    }

    @Test
    void testBasicTask() {
        project.evaluate()

        project.tasks.assembleTestfairy

        project.tasks.testfairy
    }

    @Test
    void testFlavorTask() {
        project.android {
            productFlavors {
                one {}
                two {}
            }
        }

        project.evaluate()

        project.tasks.assembleDebug
        project.tasks.assembleRelease
        project.tasks.assembleTestfairy

        project.tasks.assembleOne
        project.tasks.assembleTwo

        project.tasks.assembleOneDebug
        project.tasks.assembleOneRelease
        project.tasks.assembleOneTestfairy

        project.tasks.assembleTwoDebug
        project.tasks.assembleTwoRelease
        project.tasks.assembleTwoTestfairy

        project.tasks.testfairy
        project.tasks.testfairyOne
        project.tasks.testfairyTwo
    }

    @Test
    void testFlavorDimensionTask() {
        project.android {
            flavorDimensions 'one', 'two'

            productFlavors {
                oneone { flavorDimension 'one' }
                onetwo { flavorDimension 'one' }

                twoone { flavorDimension 'two' }
                twotwo { flavorDimension 'two' }
            }
        }

        project.evaluate()

        project.tasks.assembleDebug
        project.tasks.assembleRelease
        project.tasks.assembleTestfairy

        project.tasks.assembleOneone
        project.tasks.assembleOnetwo

        project.tasks.assembleTwoone
        project.tasks.assembleTwotwo

        project.tasks.assembleOneoneTwoone
        project.tasks.assembleOneoneTwotwo

        project.tasks.assembleOnetwoTwoone
        project.tasks.assembleOnetwoTwotwo

        project.tasks.assembleOneoneTwooneRelease
        project.tasks.assembleOneoneTwotwoRelease

        project.tasks.assembleOnetwoTwooneRelease
        project.tasks.assembleOnetwoTwotwoRelease

        project.tasks.assembleOneoneTwooneDebug
        project.tasks.assembleOneoneTwotwoDebug

        project.tasks.assembleOnetwoTwooneDebug
        project.tasks.assembleOnetwoTwotwoDebug

        project.tasks.assembleOneoneTwooneTestfairy
        project.tasks.assembleOneoneTwotwoTestfairy

        project.tasks.assembleOnetwoTwooneTestfairy
        project.tasks.assembleOnetwoTwotwoTestfairy

        project.tasks.testfairy

        project.tasks.testfairyOneoneTwoone
        project.tasks.testfairyOneoneTwotwo

        project.tasks.testfairyOnetwoTwoone
        project.tasks.testfairyOnetwoTwotwo
    }
}
