
// public class PatternEvent {
//     private ubyte mType = Pattern.EVENT_NONE;
//     private ubyte mMidiKey = 0;
//     private ubyte mVelocity = 0;
    
//     public PatternEvent() {
        
//     }
    
//     public void clear() {
//         mType = Pattern.EVENT_NONE;
//         mMidiKey = 0;
//         mVelocity = 0;
//     }
    
//     public void setNoteOn(ubyte midiKey, ubyte velocity) {
//         mType = Pattern.EVENT_NOTE_ON;
//         mMidiKey = midiKey;
//         mVelocity = velocity;
//     }
    
//     public void setNoteOff(ubyte midiKey) {
//         mType = Pattern.EVENT_NOTE_OFF;
//         mMidiKey = midiKey;
//         mVelocity = 0;
//     }
// }

public class Pattern {
    public static final int EVENT_NONE=0, EVENT_NOTE_ON=1, EVENT_NOTE_OFF=2;
    
    private int[] mData;
    private int mTrackCount;
    
    public Pattern(int trackCount, int rowCount) {
        mData = new int[trackCount * rowCount];
        mTrackCount = trackCount;
    }
    
    public int length() {
        return mData.length / mTrackCount;
    }
    
    public void clear() {
        for(int idx=0; idx < mData.length; ++idx) {
            mData[idx] = 0;
        }
    }
    
    public void setEvent(int track, int step, int type, ubyte midiKey, ubyte velocity) {
        if(track < mTrackCount && (step * mTrackCount) < mData.length) {
            if(midiKey > 127) {
                midiKey = 127;
            }
            if(velocity > 127) {
                velocity = 127;
            }
            ubyte hi = (type == EVENT_NONE) ? midiKey : midiKey | 0x80;
            ubyte lo = (type == EVENT_NOTE_ON) ? velocity : velocity | 0x80;
            mData[mTrackCount * step + track] = (hi << 8) | lo;
        }
    }
    
    public int getEventType(int track, int step) {
        ubyte note = (mData[mTrackCount * step + track] >> 8) & 0xff;
        if((note & 0x80) == 0) {
            return EVENT_NONE;
        }
        return ((mData[mTrackCount * step + track] & 0x80) == 0) ? EVENT_NOTE_ON : EVENT_NOTE_OFF;
    }
    
    public ubyte getMidiKey(int track, int step) {
        return (mData[mTrackCount * step + track] >> 8) & 0x7f;
    }
    
    public ubyte getVelocity(int track, int step) {
        return mData[mTrackCount * step + track] & 0x7f;
    }
}
