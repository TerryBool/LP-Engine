package cz.fel.cvut.pletirad.engine.inputs;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

/**
 * This class is used as a buffer for inputs.
 * Both mouse and keyboard
 */

public class InputHandler {

    private boolean[] isKeyDown;
    private Point2D bufferedMouse;

    public InputHandler() {
        isKeyDown = new boolean[7];
    }

    /**
     * Called on button press
     * Marks key as true in isKeyDown
     *
     * @param keyCode Code of the key pressed
     */

    public void updateKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case ESCAPE:
                isKeyDown[0] = true;
                break;
            case W:
                isKeyDown[1] = true;
                break;
            case A:
                isKeyDown[2] = true;
                break;
            case S:
                isKeyDown[3] = true;
                break;
            case D:
                isKeyDown[4] = true;
                break;
            case E:
                isKeyDown[5] = true;
                break;
            case Q:
                isKeyDown[6] = true;
                break;
            default:
                break;
        }

    }

    /**
     * Called on button release
     * Marks key as false in isKeyDown
     *
     * @param keyCode Code of the key released
     */

    public void updateKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case ESCAPE:
                isKeyDown[0] = false;
                break;
            case W:
                isKeyDown[1] = false;
                break;
            case A:
                isKeyDown[2] = false;
                break;
            case S:
                isKeyDown[3] = false;
                break;
            case D:
                isKeyDown[4] = false;
                break;
            case E:
                isKeyDown[5] = false;
                break;
            case Q:
                isKeyDown[6] = false;
                break;

            default:
                break;
        }
    }

    /**
     * Checks if key with keyCode is pressed or not
     *
     * @param keyCode Code of key to check state of
     * @return State of the key (true if down, false otherwise)
     */
    public boolean isDown(KeyCode keyCode) {
        boolean keyState;

        switch (keyCode) {
            case ESCAPE:
                keyState = isKeyDown[0];
                break;
            case W:
                keyState = isKeyDown[1];
                break;
            case A:
                keyState = isKeyDown[2];
                break;
            case S:
                keyState = isKeyDown[3];
                break;
            case D:
                keyState = isKeyDown[4];
                break;
            case E:
                keyState = isKeyDown[5];
                break;
            case Q:
                keyState = isKeyDown[6];
                break;
            default:
                keyState = false;
                break;
        }
        return keyState;
    }

    /**
     * Buffers where mouse was clicked on the screen
     *
     * @param mouseInput Position of mouse on canvas upon mouse input
     */
    public void bufferMouse(Point2D mouseInput) {
        bufferedMouse = mouseInput;
    }

    public Point2D getMouseInput() {
        Point2D mouseInput = bufferedMouse;
        bufferedMouse = null;
        return mouseInput;
    }
}
