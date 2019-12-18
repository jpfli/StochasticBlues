
abstract class Instrument {
    // Note frequencies from c4 to b4
    private static final float FREQUENCY_TABLE[] = new float[] {
        261.626, 277.183, 293.665, 311.127, 329.628, 349.228, 
        369.994, 391.995, 415.305, 440.000, 466.164, 493.883
    };
    private static final int REF_OCTAVE = 4;
    
    abstract void play(int midiNote, float volume);
    abstract void stop();
}
