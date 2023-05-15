package to.tinypota.ollivanders.common.storage;

public class OllivandersPlayerState {
	private String suitedWand = "";
	private String suitedCore = "";
    private String currentSpell = "empty";
	
	String getSuitedCore() {
		return suitedCore;
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