package geoand.docker.integration.setup.junit.rule;

import geoand.docker.integration.setup.core.launcher.Launcher;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by gandrianakis on 7/12/2015.
 */
public class DockerRule implements TestRule{

    private final String containerName;
    private final String imageName;
    private final String tagName;

    public DockerRule(String containerName, String imageName) {
        this(containerName, imageName, null);
    }

    public DockerRule(String containerName, String imageName, String tagName) {
        this.containerName = containerName;
        this.imageName = imageName;
        this.tagName = tagName;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                new Launcher.Configuration(containerName, imageName).imageTag(tagName).launch();
                base.evaluate();
            }
        };
    }
}
