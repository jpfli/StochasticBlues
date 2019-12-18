
import java.util.Random;

public class CelestaPlayer extends Musician {
    static final int BEAT_NONE=0, BEAT_SINGLE=1, BEAT_SWING=2, BEAT_TRIPLET=3;
    static final ubyte[] mScale = { 0, 3, 5, 6, 7, 10 };
    
    private Ensemble mEnsemble;
    private CelestaInstrument mInstrument;
    private Random mRandom;
    
    private Pattern mPattern;
    private int mBeatType = BEAT_NONE;
    
    public CelestaPlayer(Ensemble ensemble, char channel, int seed, float gain) {
        mEnsemble = ensemble;
        mInstrument = new CelestaInstrument(channel, gain);
        mRandom = new Random(seed);
        
        mPattern = new Pattern(1, mEnsemble.patternLength());
        generateBasicPattern(mPattern);
    }
    
    void onTick(int bar, int beat, int tick) {
        if((tick & 0x1) != 0) {
            return;
        }
        
        int deviation = 10 * mEnsemble.deviation();
        
        int idx = (bar * Ensemble.BEATS_PER_BAR + beat) % mPattern.length();
        
        int eventType = Pattern.EVENT_NONE;
        int noteValue = mPattern.getMidiKey(0, idx);
        ubyte velocity = 127;
        
        if(tick == 0) {
            mBeatType = BEAT_NONE;
            boolean active = (mPattern.getEventType(0, idx) == Pattern.EVENT_NOTE_ON);
            var propability = (active) ? 1000 - deviation / 2 : deviation / 2;
            // Never play the beats of the last bar (11)
            if(bar != 11 && propability > mRandom.nextInt(1000)) {
                int typeSpec = mRandom.nextInt(1000);
                if(typeSpec < 222) { // * deviation / 100) {
                    // Single quarter note
                    mBeatType = BEAT_SINGLE;
                }
                else if(typeSpec < 333) { // * deviation / 100) {
                    // Triplet
                    mBeatType = BEAT_TRIPLET;
                }
                else {
                    // Swing eighth notes
                    mBeatType = BEAT_SWING;
                }
                eventType = Pattern.EVENT_NOTE_ON;
            }
        }
        else {
            if(mBeatType == BEAT_SWING) {
                if(tick != 2) {
                    eventType = Pattern.EVENT_NOTE_ON;
                }
            }
            else if(mBeatType == BEAT_TRIPLET) {
                eventType = Pattern.EVENT_NOTE_ON;
            }
            velocity = 87;
        }
        
        if(eventType == Pattern.EVENT_NOTE_ON) {
            int randNum = mRandom.nextInt() & 0xf;
            if(randNum < 1) {
                noteValue -= 2;
            }
            else if(randNum < 2) {
                noteValue += 2;
            }
            else if(randNum < 6) {
                --noteValue;
            }
            else if(randNum < 10) {
                ++noteValue;
            }
            
            int scaleLen = mScale.length;
            // Blues scale is in the relative minor key, which is 3 half steps lower than the major
            int scaleRootNote = mEnsemble.tonicNote() - 3;
            int octave = noteValue / scaleLen;
            ubyte midiKey = scaleRootNote + 12 * octave + mScale[(noteValue % scaleLen)];
            velocity -= mRandom.nextInt() & 0xf;
            // +1 because max velocity is 127
            mInstrument.play(midiKey, (velocity + 1) / 128.0);
        }
        else if(eventType == Pattern.EVENT_NOTE_OFF) {
            mInstrument.stop();
        }
    }
    
    // From Drawable
    void draw() {
        var screen = Main.screen;
        
        int patternLen = mPattern.length();
        
        final int CELL_WIDTH = 9;
        final int CELL_HEIGHT = 11;
        
        screen.fillRect(0, super.y, screen.width(), CELL_HEIGHT, 12);
        
        screen.fillRect(super.x, super.y, CELL_WIDTH * patternLen, CELL_HEIGHT, 11);
        
        for(int idx = 0; idx < patternLen; ++idx) {
            if(mPattern.getEventType(0, idx) == Pattern.EVENT_NOTE_ON) {
                int x = super.x + idx * CELL_WIDTH;
                screen.fillRect(x, super.y, CELL_WIDTH, CELL_HEIGHT, 7);
                screen.drawRect(x, super.y, CELL_WIDTH - 1, CELL_HEIGHT - 1, 6);
            }
        }
        
        screen.drawRect(super.x, super.y, CELL_WIDTH * patternLen - 1, CELL_HEIGHT - 1, 0);
        
        screen.textColor = 5;
        screen.setTextPosition(11, super.y + 2);
        screen.println("Celesta");
    }
    
    private void generateBasicPattern(Pattern pattern) {
        // Scale percentage to 1/1000
        int density = 10 * mEnsemble.density();
        
        ubyte velocity = 127;
        
        pattern.clear();
        
        ubyte[] noteArray = new ubyte[4];
        ubyte bottomNote = 6 * mScale.length;
        ubyte nextNote = bottomNote;
        
        // This assumes 4 beats per bar!
        for(int bar = 0; bar < pattern.length() / 4; ++bar) {
            noteArray[0] = nextNote;
            nextNote = bottomNote;
            // Calculate average of neighbouring notes and add a small random value
            noteArray[2] = (noteArray[0] + nextNote) / 2 + mRandom.nextInt(3) - 1;
            noteArray[1] = (noteArray[0] + noteArray[2]) / 2 + mRandom.nextInt(3) - 1;
            noteArray[3] = (noteArray[2] + nextNote) / 2 + mRandom.nextInt(3) - 1;
            
            for(int beat = 0; beat < 4; ++beat) {
                int eventType = Pattern.EVENT_NONE;
                if(mRandom.nextInt(1000) < density) {
                    eventType = Pattern.EVENT_NOTE_ON;
                }
                pattern.setEvent(0, bar * 4 + beat, eventType, noteArray[beat], velocity);
            }
        }
    }
}
