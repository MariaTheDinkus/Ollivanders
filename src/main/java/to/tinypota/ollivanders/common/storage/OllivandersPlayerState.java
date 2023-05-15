package to.tinypota.ollivanders.common.storage;

public class OllivandersPlayerState {
	private String suitedWand = "";
	private String suitedCore = "";
    private String currentSpell = "empty";
	
	public String getSuitedWand() {
		return suitedWand;
	}
	
	String getSuitedCore() {
		return suitedCore;
	}
	
	public void setSuitedWand(String suitedWand) {
		this.suitedWand = suitedWand;
	}
	
	void setSuitedCore(String suitedCore) {
		this.suitedCore = suitedCore;
	}
	
	String getCurrentSpell() {
		return currentSpell;
	}
	
	void setCurrentSpell(String currentSpell) {
		this.currentSpell = currentSpell;
	}
}