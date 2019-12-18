
abstract class Chord {
    private ubyte[] mNotes;
    
    public Chord(ubyte[] notes) {
        mNotes = notes;
    }
    
    public int length() {
        return mNotes.length;
    }
    
    public ubyte noteAt(int idx) {
        return mNotes[idx];
    }
}
