package neuhub.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TtsParameters {
    private String tte;
    private String aue;
    private String tim;
    private String vol;
    private String sp;
    private String sr;

    public TtsParameters(String tte, String aue, String tim, String vol, String sp, String sr) {
        this.tte = tte;
        this.aue = aue;
        this.tim = tim;
        this.vol = vol;
        this.sp = sp;
        this.sr = sr;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTte() {
        return tte;
    }

    public void setTte(String tte) {
        this.tte = tte;
    }

    public String getAue() {
        return aue;
    }

    public void setAue(String aue) {
        this.aue = aue;
    }

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }
}
