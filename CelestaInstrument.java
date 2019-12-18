
import femto.sound.Mixer;

import sounds.gm_celesta_C5;
import sounds.gm_celesta_C6;
import sounds.gm_celesta_C7;

public class CelestaInstrument extends Instrument {
    private CelestaC5Sample mC5Sample;
    private CelestaC6Sample mC6Sample;
    private CelestaC7Sample mC7Sample;
    
    private char mChannel;
    private float mGain;
    
    private static final int STATE_STOPPED=0, STATE_PLAYING=1, STATE_RELEASED=2;
    private int mState;
    
    public CelestaInstrument(char channel, float gain) {
        mChannel = channel;
        mGain = gain;
        
        mC5Sample = new CelestaC5Sample(channel);
        mC6Sample = new CelestaC6Sample(channel);
        mC7Sample = new CelestaC7Sample(channel);
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
        
        if(midiKey <= 72 + 6) {
            mC5Sample.volume = volume;
            mC5Sample.delta = freq / mC5Sample.NOTE_FREQUENCY;
            mC5Sample.play();
        }
        else if(midiKey <= 84 + 6) {
            mC6Sample.volume = volume;
            mC6Sample.delta = freq / mC6Sample.NOTE_FREQUENCY;
            mC6Sample.play();
        }
        else {
            mC7Sample.volume = volume;
            mC7Sample.delta = freq / mC7Sample.NOTE_FREQUENCY;
            mC7Sample.play();
        }
        mState = STATE_PLAYING;
    }
    
    public void release() {
        mC5Sample.release();
        mC6Sample.release();
        mC7Sample.release();
    }
    
    public void stop() {
        Mixer.setChannel(mChannel, null);
    }
}

private class CelestaC5Sample extends gm_celesta_C5 {
    private static final float NOTE_FREQUENCY = 523.252;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public CelestaC5Sample(char channel) {
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

private class CelestaC6Sample extends gm_celesta_C6 {
    private static final float NOTE_FREQUENCY = 1046.504;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public CelestaC6Sample(char channel) {
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

private class CelestaC7Sample extends gm_celesta_C7 {
    private static final float NOTE_FREQUENCY = 2093.008;
    private static final int RELEASE_LEN = 8000 / 12;
    
    public float volume = 1.0;
    public float delta = 1.0;
    
    private float mTime = 0.0;
    public uint mReleaseEndTime = 0;
    
    public CelestaC7Sample(char channel) {
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
