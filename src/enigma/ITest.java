package enigma;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface ITest {
	public Object run();
	public void clickEvent(MouseEvent e, String actionCommand);
	public void dragEvent(MouseEvent e);
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);
	public void keyTyped(KeyEvent e);
}
