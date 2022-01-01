package us.dontcareabout.kingsGame.server;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.dontcareabout.kingsGame.qtd.QTD;
import us.dontcareabout.kingsGame.qtd.QtdSlave;
import us.dontcareabout.kingsGame.shared.Log;
import us.dontcareabout.kingsGame.shared.qtd.Action;
import us.dontcareabout.kingsGame.shared.qtd.State;

@RestController
@RequestMapping("qtd")
public class QtdAPI implements DisposableBean {
	private QTD qtd = new QTD();

	public QtdAPI() {
		qtd.start();
		Logger.log("QTD API start");
	}

	@Override
	public void destroy() throws Exception {
		qtd.shutdown();
	}

	////////////////////////////////

	@GetMapping("/action/{type}")
	public Action action(@PathVariable Action type) {
		switch(type) {
		case clearLog:
			Logger.clear();
			break;
		case restart:
			QtdSlave.restartQTD();
			break;
		}
		return type;
	}

	@GetMapping("/state")
	public State state() {
		State result = new State();
		result.setStageImage(toDataUri(state.getPreStageImage(), "jpg"));
		result.setScreenImage(toDataUri(state.getScreenImage(), "jpg"));
		result.setUpgradeIndex(state.getUpgradeIndex());
		return result;
	}

	@GetMapping("/log")
	public List<Log> log() {
		return Logger.getList();
	}

	////////////////////////////////

	@PostMapping("/upgradeIndex")
	public void upgradeIndex(@RequestBody int[] index) {
		QtdSlave.state.setUpgradeIndex(index);
	}

	////////////////////////////////

	private static String toDataUri(BufferedImage image, String type) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, type, baos);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "data:image/" + type + ";base64," + Base64Utils.encodeToString(baos.toByteArray());
	}
}
