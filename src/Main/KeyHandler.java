package Main;


import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class KeyHandler {
	
	JComponent gp;
	
	public KeyHandler(JComponent gp) {
		this.gp = gp;
	}
	
	public void addAction(KeyStroke ks, String actionKey, AbstractAction abstractAction)
	{
		gp.getInputMap().put(ks, actionKey);
		gp.getActionMap().put(actionKey, abstractAction);
	}
	
	public void removeAction(KeyStroke ks, String actionKey)
	{
		gp.getInputMap().remove(ks);
		gp.getActionMap().remove(actionKey);
	}

}
