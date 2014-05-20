import com.android.build.gradle.AppPlugin
import com.github.alexrichards.gradle.testfairy.TestFairyConfigurationExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class ConfigurationTest {

    @Test
    void testDelegate() {
        Project project = ProjectBuilder.builder().build()

        project.apply plugin: AppPlugin
        project.apply plugin: 'testfairy'

        Assert.assertTrue project.testfairy instanceof TestFairyConfigurationExtension

        Assert.assertTrue project.android.buildTypes.hasProperty('release') as boolean
        Assert.assertTrue project.android.buildTypes.hasProperty('debug') as boolean
        Assert.assertTrue project.android.buildTypes.hasProperty('testfairy') as boolean

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
            videoRate 1.5
            iconWatermark 'on'
            comment 'test comment'
        }

        Assert.assertEquals project.testfairy.hostName, 'test host'
        Assert.assertEquals project.testfairy.apiKey, 'test key'
        Assert.assertEquals project.testfairy.testersGroups.size(), 3
        Assert.assertEquals project.testfairy.metrics.cpu, false
        Assert.assertEquals project.testfairy.metrics.memory, true
        Assert.assertEquals project.testfairy.metrics.network, false
        Assert.assertEquals project.testfairy.metrics.phoneSignal, true
        Assert.assertEquals project.testfairy.metrics.logcat, false
        Assert.assertEquals project.testfairy.metrics.gps, true
        Assert.assertEquals project.testfairy.metrics.battery, false
        Assert.assertEquals project.testfairy.metrics.mic, true
        Assert.assertEquals project.testfairy.maxDuration, '1d'
        Assert.assertEquals project.testfairy.video, 'wifi'
        Assert.assertEquals project.testfairy.videoQuality, 'high'
        Assert.assertEquals project.testfairy.videoRate as double, 1.5, 0.0001
        Assert.assertEquals project.testfairy.iconWatermark, 'on'
        Assert.assertEquals project.testfairy.comment, 'test comment'
    }
}
