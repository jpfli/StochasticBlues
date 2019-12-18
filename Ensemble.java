
import femto.sound.Procedural;

public class Ensemble {
    public static final int TICKS_PER_BEAT = 6;
    public static final int BEATS_PER_BAR = 4;
    public static final int NUM_BARS = 12;
    
    public static final Chord CHORD_I7 = new Chord_7(0), CHORD_IV7 = new Chord_7(5), CHORD_V7 = new Chord_7(7), CHORD_v7 = new Chord_m7(7);
    public static final Chord[] CHORD_PROGRESSION_TEMPLATE = new Chord[] {
        CHORD_I7, CHORD_I7, CHORD_IV7, CHORD_IV7, CHORD_I7, CHORD_I7, CHORD_I7, CHORD_I7, 
        CHORD_IV7, CHORD_IV7, CHORD_IV7, CHORD_IV7, CHORD_I7, CHORD_I7, CHORD_I7, CHORD_I7, 
        CHORD_V7, CHORD_V7, CHORD_IV7, CHORD_IV7, CHORD_I7, CHORD_I7, CHORD_V7, CHORD_V7
    };
    
    private Musician[] mMusicians;
    private ubyte mTonicNote = 0;
    
    private int mPatternLen = 16;
    private int mDensity = 50;
    private int mDeviation = 15;
    private int mTempo = 120;
    
    private int mBar = 0;
    private int mBeat = 0;
    private int mTick = 0;
    private int mDeltaMillis = (1000 * 60) / (mTempo * TICKS_PER_BEAT);
    private long mNextUpdateMillis;
    
    public Ensemble() {
        
    }
    
    public ubyte tonicNote() {
        return mTonicNote;
    }
    
    public int patternLength() {
        return mPatternLen;
    }
    
    public void setPatternLength(int length) {
        mPatternLen = length;
    }
    
    public int density() {
        return mDensity;
    }
    
    public void setDensity(int density) {
        mDensity = density;
    }
    
    public int deviation() {
        return mDeviation;
    }
    
    public void setDeviation(int deviation) {
        mDeviation = deviation;
    }
    
    public int tempo() {
        return mTempo;
    }
    
    public void setTempo(int bpm) {
        mTempo = bpm;
        mDeltaMillis = (1000 * 60) / (mTempo * TICKS_PER_BEAT);
    }
    
    public int barNumber() {
        return mBar;
    }
    
    public int beatNumber() {
        return mBeat;
    }
    
    public int tickNumber() {
        return mTick;
    }
    
    public void setUp() {
        mTonicNote = Math.random(0, 12);
        
        mMusicians = new Musician[] {
            new CelestaPlayer(this, 3, Math.random(0, (uint)(1 << 31) - 1), 0.7), 
            new Guitarist(this, 1, 2, Math.random(0, (uint)(1 << 31) - 1), 0.45), 
            new Drummer(0, Math.random(0, (uint)(1 << 31) - 1), 0.6)
        };
        
        for(int idx = 0; idx < mMusicians.length; ++idx) {
            mMusicians[idx].x = 66;
            mMusicians[idx].y = 77 + 11 * idx;
        }
        
        mBar = 0;
        mBeat = 0;
        mTick = 0;
        
        mNextUpdateMillis = System.currentTimeMillis() + 1000;
    }
    
    public boolean update(long now) {
        if(now - mNextUpdateMillis >= 0) {
            for(var musician: mMusicians) {
                if(musician != null) {
                    musician.onTick(mBar, mBeat, mTick);
                }
            }
            
            ++mTick;
            if(mTick >= TICKS_PER_BEAT) {
                mTick = 0;
                ++mBeat;
                if(mBeat >= BEATS_PER_BAR) {
                    mBeat = 0;
                    ++mBar;
                    if(mBar >= NUM_BARS) {
                        mBar = 0;
                    }
                }
            }
            mNextUpdateMillis += mDeltaMillis;
            return true;
        }
        return false;
    }
    
    public void draw() {
        for(var musician: mMusicians) {
            if(musician != null) {
                musician.draw();
            }
        }
    }
}

private class Chord_maj extends Chord {
    public Chord_maj(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 4), (ubyte)(root + 7) });
    }
}

private class Chord_7 extends Chord { // Chord_7alt
    public Chord_7(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 4), (ubyte)(root + 7), (ubyte)(root + 10) });
    }
}

private class Chord_m7 extends Chord {
    public Chord_m7(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 3), (ubyte)(root + 7), (ubyte)(root + 10) });
    }
}

private class Chord_dim extends Chord {
    public Chord_dim(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 3), (ubyte)(root + 6) });
    }
}

private class Chord_7b5 extends Chord {
    public Chord_7b5(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 4), (ubyte)(root + 6), (ubyte)(root + 10) });
    }
}

private class Chord_m7b5 extends Chord { // Chord_0
    public Chord_m7b5(ubyte root) {
        super(new ubyte[] { root, (ubyte)(root + 3), (ubyte)(root + 6), (ubyte)(root + 10) });
    }
}

private class ChordProgression {
    Chord[] chords;
    
    public ChordProgression(Chord[] chords) {
        this.chords = chords;
    }
}
