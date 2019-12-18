
import femto.sound.Mixer;

import sounds.gm_bassdrum;
import sounds.gm_snare;
import sounds.gm_floortom;
import sounds.gm_midtom;
import sounds.gm_hightom;
import sounds.gm_hihat;
import sounds.gm_jinglebell;

public class DrumKitInstrument extends Instrument {
    public static final int KEY_KICK=36, KEY_SNARE=38, KEY_FLOOR_TOM=43, KEY_MID_TOM=47, KEY_HIGH_TOM=48, KEY_HIHAT=51, KEY_JINGLE_BELL=83;
    
    private KickSample mKick;
    private SnareSample mSnare;
    private FloorTomSample mFloorTom;
    private MidTomSample mMidTom;
    private HighTomSample mHighTom;
    private HihatSample mHihat;
    private JingleBellSample mJingleBell;
    
    private char mChannel;
    private float mGain = 1.0;
    
    public DrumKitInstrument(char channel, float gain) {
        mChannel = channel;
        mGain = gain;
        
        mKick = new KickSample(channel);
        mSnare = new SnareSample(channel);
        mFloorTom = new FloorTomSample(channel);
        mMidTom = new MidTomSample(channel);
        mHighTom = new HighTomSample(channel);
        mHihat = new HihatSample(channel);
        mJingleBell = new JingleBellSample(channel);
    }
    
    public void setGain(float gain) {
        mGain = gain;
    }
    
    public float gain() {
        return mGain;
    }
    
    // From Instrument Class
    void play(int midiKey, float volume) {
        volume *= mGain;
        
        if(midiKey == KEY_SNARE) {
            mSnare.volume = volume;
            mSnare.play();
        }
        else if(midiKey == KEY_KICK) {
            mKick.volume = volume;
            mKick.play();
        }
        else if(midiKey == KEY_FLOOR_TOM) {
            mFloorTom.volume = volume;
            mFloorTom.play();
        }
        else if(midiKey == KEY_MID_TOM) {
            mMidTom.volume = volume;
            mMidTom.play();
        }
        else if(midiKey == KEY_HIGH_TOM) {
            mHighTom.volume = volume;
            mHighTom.play();
        }
        else if(midiKey == KEY_HIHAT) {
            mHihat.volume = volume;
            mHihat.play();
        }
        else if(midiKey == KEY_JINGLE_BELL) {
            mJingleBell.volume = volume;
            mJingleBell.play();
        }
    }
    
    // From Instrument Class
    void stop() {
        
    }
}

private class KickSample extends gm_bassdrum {
    public float volume = 1.0;
    
    public KickSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class SnareSample extends gm_snare {
    public float volume = 1.0;
    
    public SnareSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class FloorTomSample extends gm_floortom {
    public float volume = 1.0;
    
    public FloorTomSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class MidTomSample extends gm_midtom {
    public float volume = 1.0;
    
    public MidTomSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class HighTomSample extends gm_hightom {
    public float volume = 1.0;
    
    public HighTomSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class HihatSample extends gm_hihat {
    public float volume = 1.0;
    
    public HihatSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class JingleBellSample extends gm_jinglebell {
    public float volume = 1.0;
    
    public JingleBellSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
    }
    
    ubyte update() {
        // Read current sample
        int val = (int)super.update() - 128;
        
        // Amplify the sample value
        val = (int)(val * this.volume) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}
