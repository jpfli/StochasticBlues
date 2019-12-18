
import femto.mode.HiRes16Color;
import femto.sound.Mixer;
import femto.input.Button;
import femto.Game;
import femto.State;
import java.util.Random;

import femto.palette.Cthulhu16;
import femto.font.TIC80;

class Main extends State {
    private static final HiRes16Color screen = new HiRes16Color(Cthulhu16.palette(), fonts.MamboFont.bin());
    
    private Property mProperties[];
    private int mActivePropIndex = 2;
    
    private Slider mLength;
    private Slider mDensity;
    private Slider mDeviation;
    private Slider mTempo;
    
    private Ensemble mEnsemble;
    private int mTickNum = 0;
    private long mNextTickTime;
    
    private int mPendingButton = Buttons.BUTTON_NONE;
    
    private boolean mRedrawScreen = true;
    
    // start the game using Main as the initial state
    // and TIC80 as the menu's font
    public static void main(String[] args) {
        Mixer.init(); // Initialize audio
        Game.run(TIC80.font(), new Main());
    }
    
    // Avoid allocation in a State's constructor.
    // Allocate on init instead.
    public void init() {
        mEnsemble = new Ensemble();
        mEnsemble.setUp();
        
        initializeProperties(mEnsemble);
        
        screen.clear(12);
        draw();
        
        mNextTickTime = System.currentTimeMillis() + 1000;
    }
    
    // Update is called by femto.Game every frame
    public void update() {
        var btn = Buttons.poll();
        if(btn != Buttons.BUTTON_NONE) {
            mPendingButton = btn;
        }
        
        var now = System.currentTimeMillis();
        if(mEnsemble.update(now)) {
            handleInput();
            draw();
        }
    }
    
    private void handleInput() {
        var activeProp = mProperties[mActivePropIndex];
        mRedrawScreen = true;
        if(!activeProp.update(mPendingButton)) {
            if(mPendingButton == Buttons.BUTTON_UP) {
                --mActivePropIndex;
                if(mActivePropIndex < 0) {
                    mActivePropIndex = mProperties.length - 1;
                }
                activeProp.selected = false;
                mProperties[mActivePropIndex].selected = true;
            }
            else if(mPendingButton == Buttons.BUTTON_DOWN) {
                ++mActivePropIndex;
                if(mActivePropIndex >= mProperties.length) {
                    mActivePropIndex = 0;
                }
                activeProp.selected = false;
                mProperties[mActivePropIndex].selected = true;
            }
            else {
                // Nothing changed so no need to redraw
                mRedrawScreen = false;
            }
        }
        mPendingButton = Buttons.BUTTON_NONE;
    }
    
    private void draw() {
        if(mRedrawScreen) {
            mRedrawScreen = false;
            
            for(var prop: mProperties) {
                prop.draw();
            }
            
            mEnsemble.draw();
            
            // Update the screen with everything that was drawn
            screen.flush();
        }
    }
    
    private void initializeProperties(Ensemble ensemble) {
        mProperties = new Property[5];
        
        mLength = new Slider("LENGTH", 4, 16, 4, new LengthSliderListener(ensemble));
        mLength.x = 66;
        mLength.y = 11;
        mLength.setValue(8 * 12);
        mProperties[0] = mLength;
        
        mDensity = new Slider("DENSITY", 0, 100, 5, new DensitySliderListener(ensemble));
        mDensity.x = 66;
        mDensity.y = 22;
        mDensity.setValue(50);
        mProperties[1] = mDensity;
        
        var prop = new UIButton("GENERATE", new ButtonListener(ensemble));
        prop.x = 66;
        prop.y = 44;
        mProperties[2] = prop;
        
        mDeviation = new Slider("DEVIATION", 0, 100, 5, new DeviationSliderListener(ensemble));
        mDeviation.x = 66;
        mDeviation.y = 143;
        mDeviation.setValue(50);
        mProperties[3] = mDeviation;
        
        mTempo = new Slider("TEMPO", 60, 240, 10, new TempoSliderListener(ensemble));
        mTempo.x = 66;
        mTempo.y = 154;
        mTempo.setValue(120);
        mProperties[4] = mTempo;
        
        mProperties[mActivePropIndex].selected = true;
    }
}

private class ButtonListener extends UIButtonListener {
    private Ensemble mEnsemble;
    
    public ButtonListener(Ensemble ensemble) {
        mEnsemble = ensemble;
    }
    
    public void onButtonPressed(UIButton button) {
        
    }
    
    public void onButtonReleased(UIButton button) {
        mEnsemble.setUp();
    }
}

private class LengthSliderListener extends SliderListener {
    private Ensemble mEnsemble;
    
    public LengthSliderListener(Ensemble ensemble) {
        mEnsemble = ensemble;
    }
    
    public void onValueChanged(Slider slider) {
        mEnsemble.setPatternLength(slider.value());
    }
}

private class DensitySliderListener extends SliderListener {
    private Ensemble mEnsemble;
    
    public DensitySliderListener(Ensemble ensemble) {
        mEnsemble = ensemble;
    }
    
    public void onValueChanged(Slider slider) {
        mEnsemble.setDensity(slider.value());
    }
}

private class DeviationSliderListener extends SliderListener {
    private Ensemble mEnsemble;
    
    public DeviationSliderListener(Ensemble ensemble) {
        mEnsemble = ensemble;
    }
    
    public void onValueChanged(Slider slider) {
        mEnsemble.setDeviation(slider.value());
    }
}

private class TempoSliderListener extends SliderListener {
    private Ensemble mEnsemble;
    
    public TempoSliderListener(Ensemble ensemble) {
        mEnsemble = ensemble;
    }
    
    public void onValueChanged(Slider slider) {
        mEnsemble.setTempo(slider.value());
    }
}
