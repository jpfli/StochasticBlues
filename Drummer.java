
import java.util.Random;

public class Drummer extends Musician {
    private Random mRandGen;
    private DrumKitInstrument mDrumKit;
    
    private Pattern mPattern;
    
    public Drummer(char channel, int seed, float gain) {
        mDrumKit = new DrumKitInstrument(channel, gain);
        mRandGen = new Random(seed);
        
        mPattern = new Pattern(1, 12);
        generateBasicPattern(mPattern);
    }
    
    // From Musician Class
    void onTick(int bar, int beat, int tick) {
        if((tick & 0x1) != 0) {
            return;
        }
        
        int idx = (tick + (beat & 0x3) * Ensemble.TICKS_PER_BEAT) / 2;
        int eventType = mPattern.getEventType(0, idx);
        ubyte midiKey = mPattern.getMidiKey(0, idx);
        ubyte velocity = mPattern.getVelocity(0, idx);
        
        if(eventType == Pattern.EVENT_NOTE_ON) {
            // +1 because max velocity is 127
            mDrumKit.play(midiKey, (velocity + 1) / 128.0);
        }
        else if(eventType == Pattern.EVENT_NOTE_OFF) {
            mDrumKit.stop();
        }
    }
    
    // From Drawable
    void draw() {
        var screen = Main.screen;
        
        int patternLen = mPattern.length();
        
        screen.fillRect(0, super.y, screen.width(), 11, 12);
        
        screen.textColor = 5;
        screen.setTextPosition(super.x, super.y + 2);
        for(int idx = 0; idx < patternLen; ++idx) {
            if(mPattern.getEventType(0, idx) == Pattern.EVENT_NOTE_ON) {
                var midiKey = mPattern.getMidiKey(0, idx);
                if(midiKey == DrumKitInstrument.KEY_KICK) {
                    screen.print("K");
                }
                else if(midiKey == DrumKitInstrument.KEY_SNARE) {
                    screen.print("S");
                }
                else if(midiKey == DrumKitInstrument.KEY_HIHAT) {
                    screen.print("H");
                }
                else if(midiKey == DrumKitInstrument.KEY_JINGLE_BELL) {
                    screen.print("J");
                }
                else {
                    screen.print("?");
                }
            }
            else {
                screen.print("-");
            }
        }
        
        screen.textColor = 5;
        screen.setTextPosition(11, super.y + 2);
        screen.println("Drum Kit");
    }
    
    private void generateBasicPattern(Pattern pattern) {
        final ubyte[] jingleBellProp = { 8, 0, 1, 0, 0, 2, 6, 0, 1, 0, 0, 4 };
        final ubyte[] snareProp = { 0, 0, 1, 6, 0, 2, 0, 0, 1, 8, 0, 4 };
        
        pattern.clear();
        
        for(int idx = 0; idx < 12; ++idx) {
            int randNum = mRandGen.nextInt();
            
            ubyte midiKey = 0;
            ubyte velocity = ((idx % 3) == 0) ? 127 : 79;
            
            if(((randNum >> 3) & 0x7) < jingleBellProp[idx]) {
                midiKey = DrumKitInstrument.KEY_JINGLE_BELL;
            }
            else if((randNum & 0x7) < snareProp[idx]) {
                midiKey = DrumKitInstrument.KEY_SNARE;
            }
            else {
                continue;
            }
            
            velocity -= (randNum >> 6) & 0x1f;
            pattern.setEvent(0, idx, Pattern.EVENT_NOTE_ON, midiKey, velocity);
        }
    }
}
