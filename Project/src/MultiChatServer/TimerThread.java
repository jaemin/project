package MultiChatServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Board.Board;
import VO.Data;

public class TimerThread {
	public static int count = 120;
	public static ScheduledExecutorService service;
	public static boolean timeout = false;

	public TimerThread() {
		service = Executors.newSingleThreadScheduledExecutor();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				count--;
				for (HashMap.Entry<Integer, ChatServerThread> thread : ChatServerThread.chatList.entrySet()) {
					try {
						thread.getValue().output.writeObject(new Data(Data.TIMER, null, count + "√ "));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (ChatServerThread.flag == true) {
					service.shutdownNow();
					ChatServerThread.flag = false;
					count = 180;
				} else if (count <= 0) {
					service.shutdownNow();
					timeout = true;
				}
			}
		};
		service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
	}
}