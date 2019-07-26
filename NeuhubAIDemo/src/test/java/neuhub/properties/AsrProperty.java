package neuhub.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AsrProperty {
    private boolean autoend;
    private AsrEncode encode;
    private String platform;
    private String version;

    public AsrProperty(boolean autoend, AsrEncode encode, String platform, String version) {
        this.autoend = autoend;
        this.encode = encode;
        this.platform = platform;
        this.version = version;
    }

    public boolean isAutoend() {
        return autoend;
    }

    public void setAutoend(boolean autoend) {
        this.autoend = autoend;
    }

    public AsrEncode getEncode() {
        return encode;
    }

    public void setEncode(AsrEncode encode) {
        this.encode = encode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
