package us.dontcareabout.kingsGame.common;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public 	class TaskManager {
	private ExecutorService service = Executors.newSingleThreadExecutor();
	private ArrayList<Task> list = new ArrayList<>();
	private PriorityQueue<Task> queue = new PriorityQueue<>((t1, t2) -> t1.priority.compareTo(t2.priority));

	public void start() {
		service.execute(() -> {
			while(true) {
				Slave.call().sleep(2);
				checkList();
				resolveQueue();
			}
		});
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