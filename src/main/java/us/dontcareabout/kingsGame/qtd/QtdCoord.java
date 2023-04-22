package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.XY;
import us.dontcareabout.kingsGame.shared.qtd.Parameter;

public class QtdCoord {
	// ======== BlueStack 區 ======== //
	public static final XY bsDesktopIcon = new XY(50, 30);
	public static final XY bsExit = new XY(900, 25);
	public static final XY bsExitConfirm = new XY(530, 320);
	public static final XY bsSaveEngry = new XY(945, 115);
	public static final XY bsSaveEngryOn = new XY(882, 82);
	public static final XY bsQtdIcon = new XY(170, 200);
	public static final Rect bsMyGame = new Rect(10, 65, 100, 30);

	// ======== 晉升（Ascend）區 ======== //
	public static final XY ascendButton = new XY(100, 340);
	public static final XY ascendConfirm = new XY(335, 495);
	public static final XY ascendJoinConfirm = new XY(70, 495);
	public static final XY ascendOffSeasonEnd = new XY(450, 400);
	public static final XY ascendOnSeasonEnd = new XY(450, 450);
	public static final XY ascendBlood = new XY(80, 320);

	// ======== 升級倍數（LvX）區 ======== //
	public static final XY lvMultiple = new XY(170, 395);
	public static final Rect lvMultipleArea = new Rect(new XY(140, 389), new XY(58, 13));
	public static final Rect lvMultipleArea2 = new Rect(new XY(139, 389), new XY(58, 13));

	// ======== 角色升級區（Crew Upgrade）區 ======== //
	public static final XY[] crewXY = new XY[7];
	static {
		XY crew1 = new XY(25, 520);
		int crewWidth = 140;

		for (int i = 0; i < crewXY.length; i++) {
			crewXY[i] = new XY(crew1.x + i * crewWidth, crew1.y);
		}
	}

	// ======== 切換編隊（Team）區 ======== //
	/** 同時也是返回主畫面的按鈕 */
	public static final XY teamButton = new XY(35, 350);

	public static final Rect stageArea = new Rect(Parameter.STAGE_X, Parameter.STAGE_Y, Parameter.STAGE_WIDTH, Parameter.STAGE_HEIGHT);
}
