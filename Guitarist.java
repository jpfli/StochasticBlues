
import java.util.Random;

public class Guitarist extends Musician {
    private Ensemble mEnsemble;
    private GuitarInstrument mInstrument1;
    private GuitarInstrument mInstrument2;
    private Random mRandom;
    
    private Chord[] mChordProgression;
    
    private Pattern mPattern;
    
    public Guitarist(Ensemble ensemble, char channel1, char channel2, int seed, float gain) {
        mEnsemble = ensemble;
        mInstrument1 = new GuitarInstrument(channel1, gain);
        mInstrument2 = new GuitarInstrument(channel2, gain);
        mRandom = new Random(seed);
        
        mChordProgression = new Chord[Ensemble.CHORD_PROGRESSION_TEMPLATE.length];
        for(int idx = 0; idx < mChordProgression.length; ++idx) {
            mChordProgression[idx] = Ensemble.CHORD_PROGRESSION_TEMPLATE[idx];
        }
        
        mPattern = new Pattern(2, Ensemble.BEATS_PER_BAR * (Ensemble.TICKS_PER_BEAT / 2));
        generateBasicPattern(mChordProgression, mPattern);
    }
    
    void onTick(int bar, int beat, int tick) {
        if((tick & 0x1) != 0) {
            return;
        }
        
        int idx = (tick + (beat & 0x3) * Ensemble.TICKS_PER_BEAT) / 2;
        int eventType = Pattern.EVENT_NONE;
        int midiKey1 = mPattern.getMidiKey(0, idx);
        int midiKey2 = mPattern.getMidiKey(1, idx);
        ubyte velocity = mPattern.getVelocity(0, idx);
        
        if(bar < 11) {
            eventType = mPattern.getEventType(0, idx);
        }
        else {
            if(idx == 0) {
                eventType = mPattern.getEventType(0, idx);
            }
        }
        
        if(eventType == Pattern.EVENT_NOTE_ON) {
            int rootNote = mChordProgression[(bar * Ensemble.BEATS_PER_BAR + beat) / 2].noteAt(0);
            if(rootNote >= 6) {
                rootNote -= 12;
            }
            
            // +1 because max velocity is 127
            mInstrument1.play(midiKey1 + rootNote, (velocity + 1) / 128.0);
            mInstrument2.play(midiKey2 + rootNote, (velocity + 1) / 128.0);
        }
        else if(eventType == Pattern.EVENT_NOTE_OFF) {
            mInstrument1.stop();
            mInstrument2.stop();
        }
    }
    
    // From Drawable
    void draw() {
        var screen = Main.screen;
        
        final int CELL_WIDTH = 9;
        final int CELL_HEIGHT = 11;
        
        screen.fillRect(0, super.y, screen.width(), CELL_HEIGHT, 12);
        
        screen.fillRect(super.x, super.y, CELL_WIDTH * 16, CELL_HEIGHT, 12);
        
        screen.textColor = 5;
        screen.setTextPosition(11, super.y + 2);
        screen.println("Guitar");
    }
    
    private void generateBasicPattern(Chord[] chordProgression, Pattern pattern) {
        final ubyte[] strumPatterns = {
            7, 7, 7, 7, 
            7, 9, 7, 9, 
            7, 7, 9, 9, 
            7, 9, 10, 9
        };
        
        int octave = 2 + mRandom.nextInt(2);
        
        int randNum = mRandom.nextInt();
        final int strumPatternIndex = 4 * (randNum & 0x3);
        
        pattern.clear();
        
        for(int beat = 0; beat < pattern.length() / 3; ++beat) {
            for(int idx = 0; idx < 3; ++idx) {
                int randNum = mRandom.nextInt();
                
                int eventType = Pattern.EVENT_NONE;
                ubyte midiKey1 = mEnsemble.tonicNote() + (octave + 1) * 12;
                ubyte midiKey2 = midiKey1 + strumPatterns[strumPatternIndex + (beat & 0x3)];
                ubyte velocity = (idx == 0) ? 127 : 87;
                
                if(idx != 1) {
                    eventType = Pattern.EVENT_NOTE_ON;
                }
                else {
                    eventType = Pattern.EVENT_NOTE_OFF;
                }
                
                velocity -= randNum & 0xf;
                if(eventType != Pattern.EVENT_NONE) {
                    pattern.setEvent(0, 3 * beat + idx, eventType, midiKey1, velocity);
                    pattern.setEvent(1, 3 * beat + idx, eventType, midiKey2, velocity);
                }
            }
        }
    }
}
