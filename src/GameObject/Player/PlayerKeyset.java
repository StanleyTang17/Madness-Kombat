package GameObject.Player;

public class PlayerKeyset {
	
	private int keys[];
	
	public PlayerKeyset(int up, int down, int left, int right, int shoot) {
		int k[] = {up, down, left, right, shoot};
		keys = k;
	}
	
	public int[] getAllKeys() { return keys; }
	public int getKey(int index) { return keys[index]; }
	public int getUpKey() { return keys[0]; }
	public int getDownKey() { return keys[1]; }
	public int getLeftKey() { return keys[2]; }
	public int getRightKey() { return keys[3]; }
	public int getShootKey() { return keys[4]; }
	public void setAllKeys(int[] k) { keys = k; }
	public void setKey(int index, int key) { keys[index] = key; }
	public void setUpKey(int key) { keys[0] = key; }
	public void setDownKey(int key) { keys[1] = key; }
	public void setLeftKey(int key) { keys[2] = key; }
	public void setRightKey(int key) { keys[3] = key; }
	public void setShootKey(int key) { keys[4] = key; }
	
}
