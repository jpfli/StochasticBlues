
import femto.Image;
import images.KnobImage;
import images.KnobSelectedImage;

public class Slider extends Property {
    private static final int WIDTH = 143;
    private static final int HEIGHT = 11;
    
    private static var mKnobImage = new KnobImage();
    private static var mKnobSelectedImage = new KnobSelectedImage();
    
    private String mLabel;
    
    public int mMin;
    public int mMax;
    public int mStep;
    
    public int mValue;
    
    private SliderListener mListener;
    
    public Slider(String label, int min, int max, int step, SliderListener listener) {
        mLabel = label;
        
        mMin = min;
        mMax = max;
        mStep = step;
        
        mValue = min;
        
        mListener = listener;
        if(mListener != null) {
            mListener.onValueChanged(this);
        }
    }
    
    public int value() {
        return mValue;
    }
    
    public void setValue(int val) {
        int oldValue = mValue;
        
        if(val < mMin) {
            mValue = mMin;
        }
        else if(val > mMax) {
            mValue = mMax;
        }
        else {
            mValue = val;
        }
        
        if(mListener != null && mValue != oldValue) {
            mListener.onValueChanged(this);
        }
    }
    
    public boolean update(int button) {
        if(button == Buttons.BUTTON_RIGHT) {
            setValue(mValue + mStep);
            return true;
        }
        else if(button == Buttons.BUTTON_LEFT) {
            setValue(mValue - mStep);
            return true;
        }
        return false;
    }
    
    public void draw() {
        var screen = Main.screen;
        
        screen.fillRect(super.x, super.y, WIDTH, HEIGHT, 12);
        screen.drawHLine(super.x, super.y + HEIGHT / 2, WIDTH, 0);
        
        int x = super.x + ((WIDTH - mKnobImage.width()) * (mValue - mMin)) / (mMax - mMin);
        if(super.selected) {
            mKnobSelectedImage.draw(Main.screen, x, super.y);
        }
        else {
            mKnobImage.draw(Main.screen, x, super.y);
        }
        
        screen.textColor = 5;
        screen.setTextPosition(11, super.y + 2);
        screen.println(mLabel);
    }
}
