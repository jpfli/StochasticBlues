
import femto.sound.Mixer;

import sounds.gm_jazzgt_C2;
import sounds.gm_jazzgt_C3;
import sounds.gm_jazzgt_C4;
import sounds.gm_jazzgt_C5;

public class GuitarInstrument extends Instrument {
    private GuitarC2Sample mC2Sample;
    private GuitarC3Sample mC3Sample;
    private GuitarC4Sample mC4Sample;
    private GuitarC5Sample mC5Sample;
    
    private char mChannel;
    private float mGain;
    
    private static final int STATE_STOPPED=0, STATE_PLAYING=1, STATE_RELEASED=2;
    private int mState;
    
    public GuitarInstrument(char channel, float gain) {
        mChannel = channel;
        mGain = gain;
        
        mC2Sample = new GuitarC2Sample(channel);
        mC3Sample = new GuitarC3Sample(channel);
        mC4Sample = new GuitarC4Sample(channel);
        mC5Sample = new GuitarC5Sample(channel);
    }
    
    public void setGain(float gain) {
        mGain = gain;
    }
    
    public float gain() {
        return mGain;
    }
    
    public void play(int midiKey, float volume) {
        int octave = midiKey / 12 - 1;
        float freq = Instrument.FREQUENCY_TABLE[midiKey % 12] * (1 << octave) / (1 << Instrument.REF_OCTAVE);
        volume *= mGain;
        
        if(midiKey <= 36 + 6) {
            mC2Sample.volume = volume;
            mC2Sample.delta = freq / mC2Sample.NOTE_FREQUENCY;
            mC2Sample.play();
        }
        else if(midiKey <= 48 + 6) {
            mC3Sample.volume = volume;
            mC3Sample.delta = freq / mC3Sample.NOTE_FREQUENCY;
            mC3Sample.play();
        }
        else if(midiKey <= 60 + 6) {
            mC4Sample.volume = volume;
            mC4Sample.delta = freq / mC4Sample.NOTE_FREQUENCY;
            mC4Sample.play();
        }
        else {
            mC5Sample.volume = volume;
            mC5Sample.delta = freq / mC5Sample.NOTE_FREQUENCY;
            mC5Sample.play();
        }
        mState = STATE_PLAYING;
    }
    
    public void release() {
        mC2Sample.release();
        mC3Sample.release();
        mC4Sample.release();
        mC5Sample.release();
    }
    
    public void stop() {
        Mixer.setChannel(mChannel, null);
    }
}

private class GuitarC2Sample extends gm_jazzgt_C2 {
    private static final float NOTE_FREQUENCY = 65.407;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public GuitarC2Sample(char channel) {
        super(channel);
    }
    
    public void release() {
        if(mReleaseEndTime <= 0) {
            mReleaseEndTime = super.t + RELEASE_LEN;
        }
    }
    
    public void reset() {
        super.reset();
        mTime = 0.0;
        mReleaseEndTime = 0;
    }
    
    ubyte update() {
        // Increase time by delta
        mTime += this.delta;
        super.t = (int) mTime;
        
        // Read current sample
        int val = (int)super.update() - 128;
        
        float gain = this.volume;
        // Calculate correct gain value if note has been released
        if(mReleaseEndTime > 0) {
            int dt = mReleaseEndTime - super.t;
            gain = (dt > 0) ? (gain * dt) / RELEASE_LEN : 0.0;
        }
        
        // Amplify the sample value
        val = (int)(val * gain) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class GuitarC3Sample extends gm_jazzgt_C3 {
    private static final float NOTE_FREQUENCY = 130.813;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public GuitarC3Sample(char channel) {
        super(channel);
    }
    
    public void release() {
        if(mReleaseEndTime <= 0) {
            mReleaseEndTime = super.t + RELEASE_LEN;
        }
    }
    
    public void reset() {
        super.reset();
        mTime = 0.0;
        mReleaseEndTime = 0;
    }
    
    ubyte update() {
        // Increase time by delta
        mTime += this.delta;
        super.t = (int) mTime;
        
        // Read current sample
        int val = (int)super.update() - 128;
        
        float gain = this.volume;
        // Calculate correct gain value if note has been released
        if(mReleaseEndTime > 0) {
            int dt = mReleaseEndTime - super.t;
            gain = (dt > 0) ? (gain * dt) / RELEASE_LEN : 0.0;
        }
        
        // Amplify the sample value
        val = (int)(val * gain) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class GuitarC4Sample extends gm_jazzgt_C4 {
    private static final float NOTE_FREQUENCY = 261.626;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public GuitarC4Sample(char channel) {
        super(channel);
    }
    
    public void release() {
        if(mReleaseEndTime <= 0) {
            mReleaseEndTime = super.t + RELEASE_LEN;
        }
    }
    
    public void reset() {
        super.reset();
        mTime = 0.0;
        mReleaseEndTime = 0;
    }
    
    ubyte update() {
        // Increase time by delta
        mTime += this.delta;
        super.t = (int) mTime;
        
        // Read current sample
        int val = (int)super.update() - 128;
        
        float gain = this.volume;
        // Calculate correct gain value if note has been released
        if(mReleaseEndTime > 0) {
            int dt = mReleaseEndTime - super.t;
            gain = (dt > 0) ? (gain * dt) / RELEASE_LEN : 0.0;
        }
        
        // Amplify the sample value
        val = (int)(val * gain) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}

private class GuitarC5Sample extends gm_jazzgt_C5 {
    private static final float NOTE_FREQUENCY = 523.252;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public GuitarC5Sample(char channel) {
        super(channel);
    }
    
    public void release() {
        if(mReleaseEndTime <= 0) {
            mReleaseEndTime = super.t + RELEASE_LEN;
        }
    }
    
    public void reset() {
        super.reset();
        mTime = 0.0;
        mReleaseEndTime = 0;
    }
    
    ubyte update() {
        // Increase time by delta
        mTime += this.delta;
        super.t = (int) mTime;
        
        // Read current sample
        int val = (int)super.update() - 128;
        
        float gain = this.volume;
        // Calculate correct gain value if note has been released
        if(mReleaseEndTime > 0) {
            int dt = mReleaseEndTime - super.t;
            gain = (dt > 0) ? (gain * dt) / RELEASE_LEN : 0.0;
        }
        
        // Amplify the sample value
        val = (int)(val * gain) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}
