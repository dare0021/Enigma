package enigma;

import java.awt.RenderingHints;

public interface IConstantsUI {
	public final Object graphicsQuality = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
	public final int APPWIDTH = 800;
	public final int APPHEIGHT = 450; //16:9
	public final int REFRESHRATE = 17; //~60Hz
	
	///////////////////////////////////////////////////////////////////////////////
	//Enum substitutes
	///////////////////////////////////////////////////////////////////////////////
	public final int NULL = 0;
	public final int NAME = 1;
	public final int GROUP= 2;
	public final int BOTH = 3;
}
