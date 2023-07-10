package cz.fel.cvut.pletirad.engine.inputs;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class InputHandler {

    private boolean[] isKeyDown;
    private Point2D bufferedMouse;

    public InputHandler() {
        isKeyDown = new boolean[6];
    }

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
            default:
                break;
        }

    }

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
            default:
                break;
        }
    }

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
            default:
                keyState = false;
                break;
        }
        return keyState;
    }

    public void bufferMouse(Point2D mouseInput) {
        bufferedMouse = mouseInput;
    }

    public Point2D getMouseInput() {
        Point2D mouseInput = bufferedMouse;
        bufferedMouse = null;
        return mouseInput;
    }
}
