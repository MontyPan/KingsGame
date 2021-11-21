package us.dontcareabout.kingsGame.common;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public 	class TaskManager {
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private ArrayList<Task> list = new ArrayList<>();
	private PriorityQueue<Task> queue = new PriorityQueue<>((t1, t2) -> t1.priority.compareTo(t2.priority));

	public void start() {
		//Slave.sleep() 會繼續霸佔 thread 運行，其他 thread 就沒得跑
		//所以改成 ScheduledExecutorService 才是正道
		service.scheduleAtFixedRate(
			() -> {
				checkList();
				resolveQueue();
			}
			, 0, 2, TimeUnit.SECONDS
		);
	}

	public void add(Task t) {
		list.add(t);
	}

	private void checkList() {
		long now = Util.now();

		for (Task t : list) {
			if (t.getNextTime() < now) {
				queue.add(t);
			}
		}
	}

	private void resolveQueue() {
		while(!queue.isEmpty()) {
			Task t = queue.poll();
			t.start();
		}
	}
}