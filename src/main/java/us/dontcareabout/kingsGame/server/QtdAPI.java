package us.dontcareabout.kingsGame.server;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.dontcareabout.kingsGame.qtd.QTD;

@RestController
@RequestMapping("qtd")
public class QtdAPI implements DisposableBean {
	private QTD qtd = new QTD();

	public QtdAPI() {
		qtd.start();
	}

	@Override
	public void destroy() throws Exception {
		qtd.shutdown();
	}

	////////////////////////////////

	@GetMapping("/stage")
	public String stage() {
		return toDataUri(state.getPreStageImage(), "jpg");
	}

	@GetMapping("/screen")
	public String screen() {
		return toDataUri(state.getScreenImage(), "jpg");
	}

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
