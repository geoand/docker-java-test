package geoand.docker.integration.setup.core.model;

import java.io.File;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class ImageBuildInfo {

    final String imageName;
    final String tag;
    final File dockerfileDirectory;

    public String getImageName() {
        return imageName;
    }

    public String getTag() {
        return tag;
    }

    public String getTaggedImageName() {
        return getImageName() + ":" + getTag();
    }

    public File getDockerfileDirectory() {
        return dockerfileDirectory;
    }

    public boolean useDockerfile() {
        return null != dockerfileDirectory;
    }

    public static class Builder {
        final String imageName;
        String tag = "latest";
        File dockerfileDirectory = null;

        public Builder(String imageName) {
            this.imageName = imageName;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder dockerfileDirectory(File dockerfileDirectory) {
            this.dockerfileDirectory = dockerfileDirectory;
            return this;
        }

        public ImageBuildInfo build() {
            return new ImageBuildInfo(this);
        }
    }

    private ImageBuildInfo(Builder builder) {
        this.imageName = builder.imageName;
        this.tag = builder.tag;
        this.dockerfileDirectory = builder.dockerfileDirectory;
    }
}
