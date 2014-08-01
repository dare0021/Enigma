package enigma;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ExampleLogic extends GUILogic implements IConstantsUI{
	ITest test = null;
	
	public ExampleLogic(ExampleGUI shipbattlegui){
		gui = shipbattlegui;
		gui.linkLogic(this);
		gui.addKeyListener(this);
		timer.start();
		
		test();
	}
	
	private void test(){
		test = new Tests.dialogRenderTest(this);
		test.run();
	}

	@Override
	public void clickEvent(MouseEvent e, String actionCommand) {
		if(test != null)
			test.clickEvent(e, actionCommand);
	}

	@Override
	public void dragEvent(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(test != null)
			test.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(test != null)
			test.keyTyped(e);
	}
	
}
