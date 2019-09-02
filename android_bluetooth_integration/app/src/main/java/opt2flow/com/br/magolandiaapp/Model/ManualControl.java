package opt2flow.com.br.magolandiaapp.Model;

/**
 * Created by Caio on 13/04/2017.
 */

public class ManualControl {

    private int sliderMin;
    private int sliderMax;
    private String buttonOne;
    private String buttonTwo;
    private String buttonThree;
    private String buttonFour;
    private String blinkButton;
    private int delayPisca;

    public ManualControl() {
    }

        public int getSliderMin() {
        return sliderMin;
    }

    public void setSliderMin(int sliderMin) {
        this.sliderMin = sliderMin;
    }

    public int getSliderMax() {
        return sliderMax;
    }

    public void setSliderMax(int sliderMax) {
        this.sliderMax = sliderMax;
    }

    public String getButtonOne() {
        return buttonOne;
    }

    public void setButtonOne(String buttonOne) {
        this.buttonOne = buttonOne;
    }

    public String getButtonTwo() {
        return buttonTwo;
    }

    public void setButtonTwo(String buttonTwo) {
        this.buttonTwo = buttonTwo;
    }

    public String getButtonThree() {
        return buttonThree;
    }

    public void setButtonThree(String buttonThree) {
        this.buttonThree = buttonThree;
    }

    public String getButtonFour() {
        return buttonFour;
    }

    public void setButtonFour(String buttonFour) {
        this.buttonFour = buttonFour;
    }

    public String getBlinkButton() {
        return blinkButton;
    }

    public void setBlinkButton(String blinkButton) {
        this.blinkButton = blinkButton;
    }

    public int getDelayPisca() {
        return delayPisca;
    }

    public void setDelayPisca(int delayPisca) {
        this.delayPisca = delayPisca;
    }
}
