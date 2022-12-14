package GameState.OptionState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import GameObject.Library;
import GameState.OptionState.OptionBlock;

public class SpawnOptionBlock extends OptionBlock {

	public SpawnOptionBlock(OptionsState optionsState, Rectangle displayBox) {
		super(optionsState, displayBox);
		
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		String text = "spawn options";
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		g.setColor(Color.BLACK);
		g.drawString(text, getX() + 10, getY() + g.getFontMetrics().getHeight() + 5);
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
			showCheckboxes();
		else
			this.switchBlock(k);

	}
	
	public void showCheckboxes()
	{
		Library library = this.getOptionsState().getLibrary();
		int len = library.getItemCount() + library.getWeaponCount();
		String[] names = new String[len];
		Object[] checkBoxes = new Object[len];
		
		for(int i = 0; i < library.getWeaponCount(); i++)
		{
			names[i] = library.getWeaponNames()[i];
			checkBoxes[i] = new JCheckBox(names[i], library.getWeaponPreset().contains(i));
		}
			
		
		for(int i = library.getWeaponCount(); i < checkBoxes.length; i++)
		{
			names[i] = library.getItemNames()[i - library.getWeaponCount()];
			checkBoxes[i] = new JCheckBox(names[i], library.getItemPreset().contains(i - library.getWeaponCount()));
		}
			
		
		JOptionPane.showConfirmDialog(null, checkBoxes, "Spawn Options", JOptionPane.DEFAULT_OPTION);
		
		ArrayList<Integer> weaponPreset = new ArrayList<Integer>();
		for(int i = 0; i < library.getWeaponCount(); i++)
		{
			JCheckBox box = (JCheckBox) checkBoxes[i];
			if(box.isSelected()) weaponPreset.add(i);
		}
		this.getOptionsState().getLibrary().setWeaponPreset(weaponPreset);
		
		ArrayList<Integer> itemPreset = new ArrayList<Integer>();
		for(int i = library.getWeaponCount(); i < checkBoxes.length; i++)
		{
			JCheckBox box = (JCheckBox) checkBoxes[i];
			if(box.isSelected()) itemPreset.add(i - library.getWeaponCount());
		}
		this.getOptionsState().getLibrary().setItemPreset(itemPreset);
		
	}
}
