package enigma;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ExampleLogic extends GUILogic implements IConstantsUI{
	ITest test = null;
	
	public ExampleLogic(ExampleGUI _gui){
		gui = _gui;
		gui.linkLogic(this);
		gui.addKeyListener(this);
		
		reset();
		((ExampleGUI)gui).showInitialUI();
		timer.start();
	}
	
	public void reset(){
		super.reset();
		gui.reset();
	}

	@Override
	public void clickEvent(MouseEvent e, String actionCommand) {
		if(test != null)
			test.clickEvent(e, actionCommand);
		else if(actionCommand != null){
			switch (actionCommand){
			case "gridTest":
				test = new Tests.gridTest(this);
				break;
			case "jldTest":
				test = new Tests.jldTest();
				break;
			case "soundTest":
				test = new Tests.soundTest();
				break;
			case "dialogRenderTest":
				test = new Tests.dialogRenderTest(this);
				break;
			case "dialogPlayerTest":
				test = new Tests.dialogPlayerTest(this);
				break;
			default:
				System.out.println("ExampleLogic.clickEvent received invalid test case: "+actionCommand);
				new Exception().printStackTrace();
				return;
			}
			reset();
			test.run();
			timer.start();
		}
	}

	@Override
	public void dragEvent(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			reset();
			test = null;
			((ExampleGUI)gui).showInitialUI();
			timer.start();
		}else if(test != null)
			test.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(test != null){
			test.keyTyped(e);
		}
	}
	
}
