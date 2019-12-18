
import femto.input.Button;

import images.ButtonImage;
import images.ButtonSelectedImage;
import images.ButtonPressedImage;

public class UIButton extends Property {
    private static var mButtonImage = new ButtonImage();
    private static var mButtonSelectedImage = new ButtonSelectedImage();
    private static var mButtonPressedImage = new ButtonPressedImage();
    
    private boolean mPressed = false;
    
    private String mLabel;
    
    private UIButtonListener mListener;
    
    public UIButton(String label, UIButtonListener listener) {
        mLabel = label;
        mListener = listener;
    }
    
    public boolean update(int button) {
        if(button == Buttons.BUTTON_A) {
            if(!mPressed) {
                if(mListener != null) {
                    mListener.onButtonPressed(this);
                }
                mPressed = true;
                return true;
            }
        }
        else if(mPressed && !Button.A.isPressed()) {
            if(mListener != null) {
                mListener.onButtonReleased(this);
            }
            mPressed = false;
            return true;
        }
        return false;
    }
    
    public void draw() {
        var screen = Main.screen;
        
        if(super.selected) {
            screen.textColor = 1;
            if(mPressed) {
                mButtonPressedImage.draw(screen, super.x, super.y);
            }
            else {
                mButtonSelectedImage.draw(screen, super.x, super.y);
            }
        }
        else {
            mButtonImage.draw(screen, super.x, super.y);
            screen.textColor = 5;
        }
        int x = super.x + (mButtonImage.width() - screen.textWidth(mLabel)) / 2;
        int y = super.y + 2;
        screen.setTextPosition(x, y);
        screen.println(mLabel);
    }
}
