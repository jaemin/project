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

	public static ArrayList<Point> coor = new ArrayList<>(); // �Ѻױ׸��⵿�� ������ ��ǥ
	public static HashMap<Integer, ArrayList<Point>> record = new HashMap<>(); // �Ѻױ׸���
	public static int count = 0;
	public static String msg = null;
	File file = new File("record");
	FileWriter fw;
	BufferedWriter bw;
	
	public CanvasMouseListener() {
		
	}

	@Override
	public void mousePressed(MouseEvent e) { // ���콺�� ������ ��
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
	public void mouseDragged(MouseEvent e) { // ���콺�� �巡�� �Ǵ� ����
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
	public void mouseReleased(MouseEvent e) { // ���콺�� ������ ��
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

	public static void linedraw(int startX, int startY, int lastX, int lastY, Color c) { // ĵ������ ���콺�� ������ ��ǥ��ŭ �׸� �׸���
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

	public static void erasedraw(int startX, int startY, int lastX, int lastY, Color c) { // ĵ������ ���찳�� ������ ��ǥ��ŭ �׸� �����
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

	public static void redraw() { // �׸� �ٽñ׸���
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