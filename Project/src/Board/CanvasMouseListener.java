package Board;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import VO.Data;
import VO.Point;

public class CanvasMouseListener implements MouseListener, MouseMotionListener {
	public int startX, startY, lastX, lastY;

	public static ArrayList<Point> coor = new ArrayList<>(); // 한붓그리기동안 움직인 좌표
	public static HashMap<Integer, ArrayList<Point>> record = new HashMap<>(); // 한붓그리기
	public static int count = 0;
	public static String msg = null;
	File file = new File("record");
	FileWriter fw;
	BufferedWriter bw;
	
	public CanvasMouseListener() {
		
	}

	@Override
	public void mousePressed(MouseEvent e) { // 마우스가 눌렸을 때
		startX = lastX = e.getX();
		startY = lastY = e.getY();
		Graphics g = ((Canvas) e.getSource()).getGraphics();
		Point p = null;

		if (Board.selected.equals("pen")) {
			linedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.penColor, Board.selected);
		} else {
			erasedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.eraseColor, Board.selected);
		}
		coor = new ArrayList<>();
		coor.add(p);
		msg = coor.get(coor.size() - 1).toString();

		Data data = new Data(Data.PICTURE_MESSAGE, null, msg);
		try {
			Board.getThread().getOutput().writeObject(data);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		msg = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) { // 마우스가 드래그 되는 동안
		Point p = null;
		startX = lastX;
		startY = lastY;
		lastX = e.getX();
		lastY = e.getY();

		Graphics g = ((Canvas) e.getSource()).getGraphics();

		if (Board.selected.equals("pen")) {
			linedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.penColor, Board.selected);
		} else {
			erasedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.eraseColor, Board.selected);
		}
		coor.add(p);
		msg = coor.get(coor.size() - 1).toString();

		Data data = new Data(Data.PICTURE_MESSAGE, null, msg);
		try {
			Board.getThread().getOutput().writeObject(data);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		msg = null;
	}

	@Override
	public void mouseReleased(MouseEvent e) { // 마우스가 떼어질 때
		Point p = null;
		startX = lastX;
		startY = lastY;
		lastX = e.getX();
		lastY = e.getY();

		Graphics g = ((Canvas) e.getSource()).getGraphics();
		if (Board.selected.equals("pen")) {
			linedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.penColor, Board.selected);
		} else {
			erasedraw(startX, startY, lastX, lastY, Board.getpenColor());
			p = new Point(startX, startY, lastX, lastY, Board.eraseColor, Board.selected);
		}
		coor.add(p);

		record.put(count, coor);
		count++;
		msg = coor.get(coor.size() - 1).toString();
		Data data = new Data(Data.PICTURE_MESSAGE, null, msg);
		try {
			Board.getThread().getOutput().writeObject(data);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		msg = null;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public static void linedraw(int startX, int startY, int lastX, int lastY, Color c) { // 캔버스에 마우스가 움직인 좌표만큼 그림 그리기
		Graphics gg = Board.canvas.getGraphics();
		for (int j = 1; j < 2; j++) {
			gg.setColor(c);
			gg.drawLine(startX - j, startY, lastX - j, lastY);
			gg.drawLine(startX, startY - j, lastX, lastY - j);
			gg.drawLine(startX + j, startY, lastX + j, lastY);
			gg.drawLine(startX, startY + j, lastX, lastY + j);
			gg.drawLine(startX, startY, lastX, lastY);
		}
	}

	public static void erasedraw(int startX, int startY, int lastX, int lastY, Color c) { // 캔버스에 지우개가 움직인 좌표만큼 그림 지우기
		Graphics gg = Board.canvas.getGraphics();
		for (int j = 1; j < 4; j++) {
			gg.setColor(c);
			gg.setColor(Color.white);
			gg.drawLine(startX - j, startY, lastX - j, lastY);
			gg.drawLine(startX, startY - j, lastX, lastY - j);
			gg.drawLine(startX + j, startY, lastX + j, lastY);
			gg.drawLine(startX, startY + j, lastX, lastY + j);
			gg.drawLine(startX, startY, lastX, lastY);
		}
	}

	public static void redraw() { // 그림 다시그리기
		for (Entry<Integer, ArrayList<Point>> coordinate : record.entrySet()) {
			for (int i = 0; i < coordinate.getValue().size(); i++) {
				Point p = (Point) coordinate.getValue().get(i);
				if (p.getState().equals("pen"))
					linedraw(p.getStartX(), p.getStartY(), p.getLastX(), p.getLastY(), p.getColor());
				else
					for (int j = 1; j < 4; j++)
						erasedraw(p.getStartX(), p.getStartY(), p.getLastX(), p.getLastY(), p.getColor());
			}
		}
	}
}