package us.dontcareabout.kingsGame.shared.qtd;

public class State {
	private String stageImage;
	private String screenImage;
	private int[] upgradeIndex;

	public String getStageImage() {
		return stageImage;
	}
	public void setStageImage(String stageImage) {
		this.stageImage = stageImage;
	}
	public String getScreenImage() {
		return screenImage;
	}
	public void setScreenImage(String screenImage) {
		this.screenImage = screenImage;
	}
	public int[] getUpgradeIndex() {
		return upgradeIndex;
	}
	public void setUpgradeIndex(int[] upgradeIndex) {
		this.upgradeIndex = upgradeIndex;
	}
}
